package com.jwork.app.utils;


import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jwork.app.App;

public class EventManager extends Thread {
    
    private static Queue<Pair<Integer,Integer>> eventList = new LinkedList<>();
    private static HashMap<String, Queue<Pair<Integer,Integer>>> eventCaches = new HashMap<String, Queue<Pair<Integer,Integer>>>();
    // private static Queue<Pair<Integer,Integer>> eventCache = new LinkedList<>();
    private static Lock keyLock = new ReentrantLock();

    private static InetSocketAddress listenAddress;
    private static Selector selector;
    private static SocketChannel client;

    // private Socket client;

    public EventManager() throws UnknownHostException, IOException, InterruptedException{
        try {
            listenAddress = new InetSocketAddress("127.0.0.1", 8989);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        eventCaches.put("0", new LinkedList<>());
        if (App.isServer()) {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(listenAddress);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } else {
            selector = Selector.open();
            while(true) {
                try {
                    client = SocketChannel.open();
                    client.connect(listenAddress);
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    break;
                } catch (IOException e) {
                    System.out.println("waiting server...");
                    sleep(1000);
                }
            }
        }
    }

    public static void addEvent(int id, KeyEvent k) {
        EventManager.keyLock.lock();
        try{
            for (Queue<Pair<Integer,Integer>> q: eventCaches.values()){
                q.add(new Pair<>(id, k.getKeyCode()));
            }
        } finally {
            EventManager.keyLock.unlock();
        }
    }

    public static void synchronize() throws IOException {
        if (App.isServer()) {
            eventList.addAll(eventCaches.get("0"));
            eventCaches.get("0").clear();
        }
        int readyCount = selector.selectNow();
        if (readyCount > 0) {
            // process selected keys...
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    String s = read(key);
                    if (s != null) {
                        if (App.isServer()) {
                            for (Queue<Pair<Integer,Integer>> q: eventCaches.values()){
                                q.addAll(stringToList(s));
                            }
                        } else {
                            eventList.addAll(stringToList(s));
                        }
                    }
                } else if (key.isWritable()) {
                    write(key);
                }
            }
        }
    }

    // accept client connection
	private static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to: " + remoteAddr);

		/*
		 * Register channel with selector for further IO (record it for read/write
		 * operations, here we have used read operation)
		 */
		channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        eventCaches.put(remoteAddr.toString(), new LinkedList<>());
	}

	// read from the socket channel
	private static String read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return null;
		}

		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);
		System.out.println("Got: " + new String(data));
        return new String(data);
	}

	// write to the socket channel
	private static void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		ByteBuffer buffer = ByteBuffer.allocate(1024);

        if (App.isServer()) {
            buffer = ByteBuffer.wrap(listToString(eventCaches.get(remoteAddr.toString())).getBytes());
        } else {
            buffer = ByteBuffer.wrap(listToString(eventCaches.get("0")).getBytes());
        }
        try {
            int numWrite = channel.write(buffer);
    
            if (numWrite == -1) {
                channel.close();
                key.cancel();
                return;
            }
            if (App.isServer()) {
                eventCaches.get(remoteAddr.toString()).clear();
            } else {
                eventCaches.get("0").clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    private static String listToString(Queue<Pair<Integer, Integer>> q) {
        String tmp = "";
        if (q != null) {
            for (Pair<Integer, Integer> p: q) {
                tmp += p.key().toString() + "," + p.value().toString() + ";";
            }
        }
        return tmp;
    }

    private static Queue<Pair<Integer, Integer>> stringToList(String s) {
        Queue<Pair<Integer, Integer>> tmp = new LinkedList<>();
        String[] steps = s.split(";");
        if (steps.length > 0 && !steps[0].equals("")) {
            for (String step: steps) {
                String[] pair = step.split(",");
                Integer k = Integer.valueOf(pair[0]);
                Integer v = Integer.valueOf(pair[1]);
                tmp.add(new Pair<>(k, v));
            }
        }
        return tmp;
    }

    public static Integer getEvent(int id) {
        EventManager.keyLock.lock();
        try{
            if (eventList.isEmpty()) {
                return null;
            }
            Pair<Integer, Integer> p = eventList.peek();
            // eventList.clear(); // 每次清空
            if (p.key() == id) {
                eventList.poll();
                return p.value();
            } else {
                return null;
            }
        } finally {
            EventManager.keyLock.unlock();
        }
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        while(true) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime > 100) {
                try {
                    lastTime = nowTime;
                    EventManager.synchronize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class Pair<K, V> {
    private K key;
    private V value;

    Pair(K k, V v) {
        key = k;
        value = v;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }
}
