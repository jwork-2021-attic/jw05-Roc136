package com.jwork.app.screen;

import javax.swing.JFrame;

public class Flasher extends Thread {
    
    private JFrame app;
    private int fps;

    public Flasher(JFrame app) {
        this.app = app;
        this.fps = 60;
    }

    public Flasher(JFrame app, int fps) {
        this.app = app;
        this.fps = fps;
    }

    @Override
    public void run() {
        try {
            while(true) {
                app.repaint();
                int t = 1000 / fps;
                Thread.sleep(t);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
