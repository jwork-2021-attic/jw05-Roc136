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
import com.jwork.app.asciiPanel.AsciiPanel;
// import com.jwork.app.maze.MazeSolution;

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
    private int maxKeysNum;
    // private MazeSolution solution;

    public PlayScreen() {
        this.screenWidth = 40;
        this.screenHeight = 40;
        this.worldWidth = 50;
        this.worldHeight = 50;
        this.maxKeysNum = 10;
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
            this.player = creatureFactory.newPlayer(this.messages, maxKeysNum);
            exec.submit(player);
            for (int i = 0; i < maxKeysNum; i++) {
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
                terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                // if (player.canSee(creature.x(), creature.y())) {
                //     terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                // }
                terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
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
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        String stats = String.format("%02d/%02d keys collected.", player.score(), maxKeysNum);
        terminal.write(stats, 0, screenHeight);
        // Messages
        displayMessages(terminal, this.messages);
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
