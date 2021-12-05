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

import com.jwork.app.world.*;
import com.jwork.app.App;
import com.jwork.app.asciiPanel.AsciiPanel;
// import com.jwork.app.maze.MazeSolution;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private int worldWidth;
    private int worldHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private ExecutorService exec;
    private int screenFrameTop;
    private int screenFrameLeft;
    // private MazeSolution solution;

    public PlayScreen() {
        this.screenFrameLeft = 1;
        this.screenFrameTop = 9;
        this.screenWidth = App.terminalWidth - 16;
        this.screenHeight = App.terminalHeight - screenFrameTop - 1;
        this.worldWidth = screenWidth;
        this.worldHeight = screenHeight;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();

        this.exec = Executors.newCachedThreadPool();
        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(this.exec, creatureFactory);

        // int [][] maze = new int[worldWidth][worldHeight];
        // for (int i = 0; i < worldHeight; i++) {
        //     for (int j = 0; j < worldWidth; j++) {
        //         if (world.tile(j, i).isGround()) {
        //             maze[i][j] = 1;
        //         } else {
        //             maze[i][j] = 0;
        //         }
        //     }
        // }
        // for (Creature c: world.getCreatures()) {
        //     if (c.glyph() == (char)13) {
        //         maze[c.y()][c.x()] = 2;
        //     }
        // }
        // this.solution = new MazeSolution(maze, maxKeysNum);
        // this.solution.calculate();
    }

    private void createCreatures(ExecutorService exec, CreatureFactory creatureFactory) {
        if (world != null) {
            this.player = creatureFactory.newPlayer(this.messages);
            exec.submit(player);
            for (int i = 0; i < 10; i++) {
                Creature monster = creatureFactory.newMonster();
                exec.submit(monster);
            }
        }
    }

    private void createWorld() {
        // world = new WorldBuilder(screenWidth, screenHeight).makeMaze().build();
        world = new WorldBuilder(worldWidth, worldHeight).makeMap("map/map1.csv").build();
        this.worldHeight = world.height();
        this.worldWidth = world.width();
        // world = new WorldBuilder(worldWidth, worldHeight).makeMaze().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth && x < world.width(); x++) {
            for (int y = 0; y < screenHeight && y < world.height(); y++) {
                int wx = x + left;
                int wy = y + top;

                // if (player.canSee(wx, wy)) {
                //     terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                // } else {
                //     terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                // }
                terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop, world.color(wx, wy));
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                // if (player.canSee(creature.x(), creature.y())) {
                //     terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                // }
                terminal.write(creature.glyph(), creature.x() - left + screenFrameLeft, creature.y() - top + screenFrameTop, creature.color());
            }
        }
        // Creatures can choose their next action now
        // world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            // 先把上一条用空格覆盖掉
            terminal.write(String.format("%40s", " "), 0, top + i + 1);
            // TODO 消息长度不能超过屏幕宽度，否则报错
            terminal.write(messages.get(i), 0, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX() + screenFrameLeft, player.y() - getScrollY() + screenFrameTop, player.color());
        // Stats
        int statsTop = screenFrameTop + 15, statsLeft = screenWidth + screenFrameLeft + 2;
        String stats = String.format("SCORE: %04d", player.score());
        terminal.write(stats, statsLeft, statsTop);
        stats = String.format("HP: %03d/%03d", player.hp(), player.maxHP());
        terminal.write(stats, statsLeft, statsTop + 2);
        stats = String.format("ATK: %02d", player.attackValue());
        terminal.write(stats, statsLeft, statsTop + 4);
        stats = String.format("DEF: %02d", player.defenseValue());
        terminal.write(stats, statsLeft, statsTop + 6);
        // Frame
        Color frameColor = Color.RED;
        char frameGlyph = (char)8;
        Color fontColor = Color.WHITE;
        char fontGlyph = (char)8;
        char fontSpaceGlyph = (char)0;
        for (int i = 0; i < App.terminalHeight; i++) {
            terminal.write(frameGlyph, App.terminalWidth - 1, i, frameColor);
            terminal.write(frameGlyph, 0, i, frameColor);
        }
        for (int i = screenFrameTop; i < App.terminalHeight; i++) {
            terminal.write(frameGlyph, screenWidth + screenFrameLeft, i, frameColor);
        }
        for (int i = 0; i < App.terminalWidth; i++) {
            terminal.write(frameGlyph, i, 0, frameColor);
            terminal.write(frameGlyph, i, screenFrameTop - 1, frameColor);
            terminal.write(frameGlyph, i, App.terminalHeight - 1, frameColor);
        }
        int fontLeft = 4, fontTop = 2;
        // Gensoul Knight 《原 气 骑 士》
        terminal.write("111101110100010111101111010010100001001010001011101111010010111".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop, fontColor);
        terminal.write("100001000110010100001001010010100001010011001001001000010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 1, fontColor);
        terminal.write("101101110101010111101001010010100001100010101001001011011110010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 2, fontColor);
        terminal.write("100101000100110000101001010010100001010010011001001001010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 3, fontColor);
        terminal.write("111101110100010111101111011110111001001010001011101111010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 4, fontColor);

        // Messages
        // displayMessages(terminal, this.messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (player.moveBy(-1, 0)) {
                    // solution.checkStep(player.x(), player.y(), -1, 0);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (player.moveBy(1, 0)) {
                    // solution.checkStep(player.x(), player.y(), 1, 0);
                }
                break;
            case KeyEvent.VK_UP:
                if (player.moveBy(0, -1)) {
                    // solution.checkStep(player.x(), player.y(), 0, -1);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (player.moveBy(0, 1)) {
                    // solution.checkStep(player.x(), player.y(), 0, 1);
                }
                break;
            case KeyEvent.VK_ENTER:
                // int step = solution.getStep(player.x(), player.y());
                // switch(step) {
                //     case 0:
                //         player.moveBy(0, -1);
                //         break;
                //     case 1:
                //         player.moveBy(0, 1);
                //         break;
                //     case 2:
                //         player.moveBy(-1, 0);
                //         break;
                //     case 3:
                //         player.moveBy(1, 0);
                //         break;
                // }
                break;
        }
        if (player.win()) {
            return new WinScreen();
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
