package com.jwork.app.world;

import com.jwork.app.asciiPanel.AsciiPanel;
import java.awt.Color;
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

/**
 *
 * @author Aeranythe Echosong
 */
public enum Tile {

    FLOOR((char) 0, AsciiPanel.yellow), // 0
    WALL((char) 177), // 1
    ROCK((char) 241, AsciiPanel.brightBlack), // 2
    TREE((char) 240, AsciiPanel.green), // 3
    GRASS((char) 242, AsciiPanel.brightGreen), // 4
    FLOWER((char) 243, AsciiPanel.brightYellow), // 5
    BOUNDS('x', AsciiPanel.magenta),

    UP((char)24, AsciiPanel.brightGreen),
    DOWN((char)25, AsciiPanel.brightGreen),
    RIGHT((char)26, AsciiPanel.brightGreen),
    LEFT((char)27, AsciiPanel.brightGreen),

    BEGINNING((char)6, AsciiPanel.red),
    ENDING((char)15, AsciiPanel.green); // 6

    private char glyph;

    public char glyph() {
        return glyph;
    }

    private Color color;

    public Color color() {
        return color;
    }

    public boolean isDiggable() {
        return this != Tile.WALL && this != Tile.BOUNDS;
    }

    public boolean isGround() {
        return this != Tile.WALL && this != Tile.BOUNDS && this != Tile.ROCK;
    }

    public boolean isBeginning() {
        return this == Tile.BEGINNING;
    }

    public boolean isEnding() {
        return this == Tile.ENDING;
    }

    Tile(char glyph) {
        this.glyph = glyph;
        this.color = null;
    }

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }
}
