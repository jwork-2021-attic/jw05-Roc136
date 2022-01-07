package com.jwork.app.utils;

import java.io.File;

import com.jwork.app.screen.PlayScreen;

public class Recorder {
    private static boolean recording = false;

    Recorder() {
    }

    public static boolean isRecording() {
        return recording;
    }

    public static void beginRecord() {
        recording = true;
    }

    public static void endRecording() {
        recording = false;
    }

    public static void switchStatus() {
        if (recording) {
            endRecording();
        } else {
            beginRecord();
        }
    }

    public static void saveStatus() {
        File file = new File("");
        saveStatus(file);
    }

    private static void saveStatus(File file) {

    }

    public static PlayScreen load(String recordFile) {
        return new PlayScreen(0);
    }
}
