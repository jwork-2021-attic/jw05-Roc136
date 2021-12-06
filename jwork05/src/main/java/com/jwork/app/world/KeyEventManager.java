package com.jwork.app.world;


import java.awt.event.KeyEvent;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class KeyEventManager {
    
    private static Stack<KeyEvent> eventList = new Stack<>();
    private static Lock keyLock = new ReentrantLock();

    public KeyEventManager(){}

    public static void addEvent(KeyEvent k) {
        KeyEventManager.keyLock.lock();
        try{
            eventList.add(k);
        } finally {
            KeyEventManager.keyLock.unlock();
        }
    }

    public static KeyEvent getEvent() {
        KeyEventManager.keyLock.lock();
        try{
            if (eventList.empty()) {
                return null;
            }
            KeyEvent k = eventList.pop();
            eventList.clear(); // 每次清空
            return k;
        } finally {
            KeyEventManager.keyLock.unlock();
        }
    }
}
