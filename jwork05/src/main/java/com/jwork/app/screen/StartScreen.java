/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jwork.app.screen;

import com.jwork.app.App;
import com.jwork.app.asciiPanel.AsciiPanel;
import com.jwork.app.map.MapEditer;

import java.awt.event.KeyEvent;
import java.io.File;
// import java.net.JarURLConnection;
// import java.net.URL;
// import java.util.Enumeration;
// import java.util.jar.JarEntry;
// import java.util.jar.JarFile;
import java.awt.Color;

/**
 *
 * @author Aeranythe Echosong
 */
public class StartScreen extends RestartScreen {

    private int mapNum = 0;
    private int selector = 0;

    public StartScreen() {
        File dir = new File("map");
        for (File f: dir.listFiles()) {
            // System.out.println(f.getName());
            if (f.isFile()) {
                mapNum += 1;
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int frameHeight = 14 + mapNum * 2;
        int frameWidth = 32;
        int frameTop = (App.terminalHeight - frameHeight) / 2 - 1;
        int frameLeft = (App.terminalWidth - frameWidth) / 2 - 1;
        Color frameColor = Color.WHITE;
        char frameGlyph = (char)8;
        for(int i = 0; i < frameWidth; i++) {
            terminal.write(frameGlyph, frameLeft + i + 1, frameTop, frameColor);
            terminal.write(frameGlyph, frameLeft + i, frameTop + frameHeight, frameColor);
        }
        for(int i = 0; i < frameHeight; i++) {
            terminal.write(frameGlyph, frameLeft, frameTop + i, frameColor);
            terminal.write(frameGlyph, frameLeft + frameWidth, frameTop + i + 1, frameColor);
        }

        terminal.write("Calabash Knight", frameLeft + (frameWidth - 15) / 2, frameTop + 2);
        terminal.write("Use up/down to select map:", frameLeft + 2, frameTop + 4);
        terminal.write("Press E to Edit Map", frameLeft + 2, frameTop + 6);
        terminal.write("Press Enter to Start Game...", frameLeft + 2, frameTop + 8);
        terminal.write("Select NEW MAP to add new map", frameLeft + 2, frameTop + 10);
        for(int i = 0; i <= mapNum; i++) {
            if(i == mapNum) {
                terminal.write(String.format("NEW MAP"), frameLeft + (frameWidth - 7) / 2, frameTop + 12 + i * 2, Color.GREEN);
            } else if (selector == i) {
                terminal.write(String.format("MAP-%d", i), frameLeft + (frameWidth - 5) / 2, frameTop + 12 + i * 2, Color.YELLOW);
            } else {
                terminal.write(String.format("MAP-%d", i), frameLeft + (frameWidth - 5) / 2, frameTop + 12 + i * 2);
            }
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key){
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            System.out.printf("%d %d", selector, mapNum);
                if (selector == mapNum) {
                    return new MapEditer(selector, true);
                } else {
                    return new PlayScreen(selector);
                }
            case KeyEvent.VK_UP: case KeyEvent.VK_LEFT: case KeyEvent.VK_W: case KeyEvent.VK_A:
                selector = (selector + mapNum) % (mapNum + 1);
                break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_RIGHT: case KeyEvent.VK_S: case KeyEvent.VK_D:
                selector = (selector + 1) % (mapNum + 1);
                break;
            case KeyEvent.VK_E:
                if (selector == mapNum) {
                    return new MapEditer(selector, true);
                } else {
                    return new MapEditer(selector, false);
                }
            default:
                break;
        }
        return this;
    }

}
