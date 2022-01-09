package com.jwork.app;

import static org.junit.Assert.*;
import java.io.File;


import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testApp() {
        File file = new File("map");

        App.initMapFile();
        assertTrue(file.exists());

        App app = new App();
        assertSame(app, app);

        App.initMapFile();
        assertTrue(file.exists());
    }
}
