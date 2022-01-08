package com.jwork.app.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Recorder {
    private static boolean recording = false;
    private static BufferedWriter writer;
    private static Lock lock = new ReentrantLock();

    Recorder() {
    }

    public static boolean isRecording() {
        return recording;
    }

    public static void beginRecord(String kind, String status) {
        File dir = new File("record");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filename = String.format("record/%s-%s.txt", kind, (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(System.currentTimeMillis()));
        try {
            File tmpFile = new File(filename);
            tmpFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        recording = true;
    }

    public static void endRecording() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recording = false;
    }

    public static void saveStatus(String status) {
        beginRecord("status", status);
        endRecording();
    }

    public static void saveOperation(String operation) {
        lock.lock();
        try {
            writer.write("\n" + operation);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
