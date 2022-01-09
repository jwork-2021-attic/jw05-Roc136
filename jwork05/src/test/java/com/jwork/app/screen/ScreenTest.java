package com.jwork.app.screen;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.jwork.app.App;
import com.jwork.app.utils.RecordLoader;

public class ScreenTest {
    
    private Screen screen;
    App app = new App();

    @Before
    public void init() {
        App.initMapFile();
        screen = new StartScreen();
    }

    @Test
    public void testScreen() throws Exception {
        screen = new PlayScreen(0);
        KeyEvent key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_R, 'R');
        screen.respondToUserInput(key);
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_D, 'D');
        screen.respondToUserInput(key);
        Thread.sleep(2000);
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, ' ');
        screen.respondToUserInput(key);
        Thread.sleep(2000);
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_S, 'S');
        screen.respondToUserInput(key);
        Thread.sleep(2000);
        screen.displayOutput(app.getTerminal());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_R, 'R');
        screen.respondToUserInput(key);

        File dir = new File("record");
        File f = dir.listFiles()[0];
        try(BufferedReader reader =  new BufferedReader(new FileReader(f.getPath()))) {
            String line = reader.readLine();
            assertNotEquals(line, "");
        } catch (Exception e) {
            throw e;
        }

        screen = new RecordLoader();
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, ' ');
        screen = screen.respondToUserInput(key);
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_N, 'N');
        screen.respondToUserInput(key);

        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, ' ');
        screen = new LoseScreen();
        screen.displayOutput(app.getTerminal());
        assertSame(screen.respondToUserInput(key).getClass(), new StartScreen().getClass());

        screen = new WinScreen();
        screen.displayOutput(app.getTerminal());
        assertSame(screen.respondToUserInput(key).getClass(), new StartScreen().getClass());



    }
}
