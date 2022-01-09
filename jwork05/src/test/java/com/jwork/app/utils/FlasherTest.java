package com.jwork.app.utils;

import static org.junit.Assert.*;

import com.jwork.app.App;

import org.junit.Before;
import org.junit.Test;

public class FlasherTest {

    App app = new App();

    @Before
    public void init() {
        App.initMapFile();
    }
    
    @Test
    public void testFlasher(){
        Flasher flasher = new Flasher(app);
        flasher = new Flasher(app, 30);
        flasher.start();
        assertNotEquals(Thread.currentThread().getName(), flasher.getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flasher.interrupt();
    }
}
