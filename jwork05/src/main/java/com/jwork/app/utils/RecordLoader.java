package com.jwork.app.utils;

import com.jwork.app.App;
import com.jwork.app.asciiPanel.AsciiPanel;
import com.jwork.app.screen.PlayScreen;
import com.jwork.app.screen.Screen;
import com.jwork.app.screen.StartScreen;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.io.File;

public class RecordLoader extends StartScreen {

    private int recordNum = 0;
    String[] records;

    public RecordLoader() {
        File dir = new File("record");
        records = new String[dir.listFiles().length];
        int i = 0;
        for (File f: dir.listFiles()) {
            // System.out.println(f.getName());
            if (f.isFile()) {
                recordNum += 1;
                records[i++] = f.getPath();
            }
        }
        frameHeight = 8 + recordNum * 2;
        frameWidth = 40;
        frameTop = (App.terminalHeight - frameHeight) / 2 - 1;
        frameLeft = (App.terminalWidth - frameWidth) / 2 - 1;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        displayFrame(terminal);
        terminal.write("Calabash Knight", frameLeft + (frameWidth - 15) / 2, frameTop + 2);
        terminal.write("Use W/S to select record:", frameLeft + 2, frameTop + 4);
        terminal.write("Press Enter to Load record", frameLeft + 2, frameTop + 6);
        for(int i = 0; i < recordNum; i++) {
            if (selector == i) {
                terminal.write(records[i].split("/")[1], frameLeft + 2, frameTop + 8 + i * 2, Color.BLACK, Color.YELLOW);
            } else {
                terminal.write(records[i].split("/")[1], frameLeft + 2, frameTop + 8 + i * 2, Color.WHITE);
            }
        }
    }
    

    @Override
    public Screen respondToUserInput(KeyEvent key){
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new PlayScreen(records[selector]);
            case KeyEvent.VK_UP: case KeyEvent.VK_LEFT: case KeyEvent.VK_W: case KeyEvent.VK_A:
                selector = (selector + recordNum - 1) % recordNum;
                break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_RIGHT: case KeyEvent.VK_S: case KeyEvent.VK_D:
                selector = (selector + 1) % recordNum;
                break;
            case KeyEvent.VK_Q: case KeyEvent.VK_ESCAPE:
                return new StartScreen();
            default:
                break;
        }
        return this;
    }
}
