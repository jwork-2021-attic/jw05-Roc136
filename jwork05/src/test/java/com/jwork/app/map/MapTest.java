package com.jwork.app.map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;

import com.jwork.app.App;
import com.jwork.app.screen.StartScreen;


public class MapTest {

    App app = new App();

    @Before
    public void init() {
        App.initMapFile();
    }

    @Test
    public void testKeyPress() {
        MapEditer mapEditer = new MapEditer(0, false);
        KeyEvent key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_Q, 'Q');
        assertSame(mapEditer.respondToUserInput(key).getClass(), new StartScreen().getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_X, 'X');
        assertSame(mapEditer.respondToUserInput(key).getClass(), new StartScreen().getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_RIGHT, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_LEFT, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_DOWN, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_UP, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_DOWN, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '0');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '1');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '2');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '3');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '4');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_1, '5');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_DOWN, ' ');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, '\n');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, '\n');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, '\n');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, '\n');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
        key = new KeyEvent(app, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,  KeyEvent.VK_ENTER, '\n');
        assertSame(mapEditer.respondToUserInput(key).getClass(), mapEditer.getClass());
    }
    
}
