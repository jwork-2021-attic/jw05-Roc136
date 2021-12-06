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
import java.awt.event.KeyEvent;
import java.awt.Color;

/**
 *
 * @author Aeranythe Echosong
 */
public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int frameHeight = 12;
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

        terminal.write("Calabash Knight", frameLeft + (frameWidth - 15) / 2, frameTop + 5);
        terminal.write("You Win!", frameLeft + 2, frameTop + 7);
        terminal.write("Press Enter to Restart Game...", frameLeft + 2, frameTop + 9);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new StartScreen();
            default:
                return this;
        }
    }

}
