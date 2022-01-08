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
import com.jwork.app.utils.KeyEventManager;
import com.jwork.app.utils.Recorder;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private ExecutorService exec;

    protected World world;
    protected Creature player;
    protected int screenWidth;
    protected int screenHeight;
    protected int worldWidth;
    protected int worldHeight;
    protected List<String> messages;
    protected int screenFrameTop;
    protected int screenFrameLeft;
    protected String map;
    protected boolean needLoad;
    protected boolean loading;
    protected String file;
    protected BufferedReader reader;

    public PlayScreen(int mapSelector) {
        this.screenFrameLeft = 1;
        this.screenFrameTop = 9;
        this.screenWidth = App.terminalWidth - 16; // 48
        this.screenHeight = App.terminalHeight - screenFrameTop - 1; // 30
        this.worldWidth = screenWidth;
        this.worldHeight = screenHeight;
        this.map = String.format("map/map%d.csv", mapSelector);
        this.messages = new ArrayList<String>();
        this.needLoad = false;
        this.file = "";
        initWorld();
    }

    public PlayScreen(String file) {
        this.file = file;
        this.screenFrameLeft = 1;
        this.screenFrameTop = 9;
        this.screenWidth = App.terminalWidth - 16; // 48
        this.screenHeight = App.terminalHeight - screenFrameTop - 1; // 30
        this.worldWidth = screenWidth;
        this.worldHeight = screenHeight;
        this.messages = new ArrayList<String>();
        try {
            this.reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] status = line.split(";");

            this.map = String.format("map/map%d.csv", Integer.valueOf(status[0]));
            createWorld();

            this.exec = Executors.newCachedThreadPool();
            CreatureFactory creatureFactory = new CreatureFactory(this.world);

            // Player
            String[] playerStatus = status[1].split(",");
            this.player = new Player(Integer.valueOf(playerStatus[0]), this.world, creatureFactory, (char)2, 1, AsciiPanel.brightWhite, Integer.valueOf(playerStatus[4]), Integer.valueOf(playerStatus[5]), Integer.valueOf(playerStatus[6]), Integer.valueOf(playerStatus[7]), 20);
            this.world.addAtCertainLocation(player, Integer.valueOf(playerStatus[1]), Integer.valueOf(playerStatus[2]));
            new PlayerAI(player, messages);
            this.world.setPlayer(this.player);
            // exec.submit(player);

            // Monster
            for (int i = 2; i < status.length; i++) {
                String[] monsterStatus = status[i].split(",");
                Creature monster = new Monster(Integer.valueOf(monsterStatus[0]), this.world, creatureFactory, (char)13, 2, AsciiPanel.brightRed, 5, Integer.valueOf(monsterStatus[3]), 3, 9);
                world.addAtCertainLocation(monster, Integer.valueOf(monsterStatus[1]), Integer.valueOf(monsterStatus[2]));
                new MonsterAI(monster);
                // exec.submit(monster);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.startsWith("record", 7)) {
            this.needLoad = true;
        } else {
            this.needLoad = false;
        }
    }

    public void loadOperationFromFile() {
        try {
            // Operation
            String line = reader.readLine();
            if (line != null) {
                System.out.println(line);
                String[] ops = line.split(",");
                if (ops.length > 1) {
                    int id = Integer.valueOf(ops[0]);
                    int op = Integer.valueOf(ops[1]);
                    int targetX = Integer.valueOf(ops[2]);
                    int targetY = Integer.valueOf(ops[3]);
                    switch (op) {
                        case 0:
                            world.creature(id).moveBy(targetX, targetY);
                            break;
                        case 1:
                            world.creature(id).shot(targetX, targetY);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                try {
                    exec.submit(this.player);
                    for (Creature c: world.getCreatures()) {
                        exec.submit(c);
                    }
                    this.needLoad = false;
                    this.loading = false;
                    this.reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initWorld() {
        createWorld();

        this.exec = Executors.newCachedThreadPool();
        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(this.exec, creatureFactory);

    }

    private void createCreatures(ExecutorService exec, CreatureFactory creatureFactory) {
        if (world != null) {
            this.player = creatureFactory.newPlayer(this.messages);
            this.world.setPlayer(this.player);
            exec.submit(player);
            for (int i = 0; i < World.maxMonsterNum; i++) {
                Creature monster = creatureFactory.newMonster();
                exec.submit(monster);
            }
        }
    }

    protected void createWorld() {
        world = new WorldBuilder(worldWidth, worldHeight).makeMap(map).build();
        this.worldHeight = world.height();
        this.worldWidth = world.width();
    }

    protected void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth && x < world.width(); x++) {
            for (int y = 0; y < screenHeight && y < world.height(); y++) {
                int wx = x + left;
                int wy = y + top;
                if (world.color(wx, wy) != null) {
                    terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop);
                }
            }
        }
    }

    protected void displayCreatures(AsciiPanel terminal, int left, int top) {
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.showable() && creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                terminal.write(creature.glyph(), creature.x() - left + screenFrameLeft, creature.y() - top + screenFrameTop, creature.color());
            }
        }
        // Show bullets
        for (Creature bullet : world.getBullets()) {
            if (bullet.x() >= left && bullet.x() < left + screenWidth && bullet.y() >= top
                    && bullet.y() < top + screenHeight) {
                terminal.write(bullet.glyph(), bullet.x() - left + screenFrameLeft, bullet.y() - top + screenFrameTop, bullet.color());
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        displayCreatures(terminal, getScrollX(), getScrollY());
        // Player
        // terminal.write(player.glyph(), player.x() - getScrollX() + screenFrameLeft, player.y() - getScrollY() + screenFrameTop, player.color());
        // Stats
        int statsTop = screenFrameTop + (screenHeight - 13 ) / 2, statsLeft = screenWidth + screenFrameLeft + 2;
        String stats = String.format("SCORE: %04d", player.score());
        terminal.write(stats, statsLeft, statsTop);
        stats = String.format("HP: %03d/%03d", player.hp(), player.maxHP());
        terminal.write(stats, statsLeft, statsTop + 2);
        stats = String.format("ATK: %02d", player.attackValue());
        terminal.write(stats, statsLeft, statsTop + 4);
        stats = String.format("DEF: %02d", player.defenseValue());
        terminal.write(stats, statsLeft, statsTop + 6);
        terminal.write("Q->Quit", statsLeft, statsTop + 8);
        if (this.needLoad) {
            terminal.write("N->Next", statsLeft, statsTop + 10);
        } else {
            terminal.write("X->Save", statsLeft, statsTop + 10);
            if (Recorder.isRecording()) {
                terminal.write("R->Stop", statsLeft, statsTop + 12);
                Color tmpColor;
                if (System.currentTimeMillis() / 500 % 2 == 1) {
                    tmpColor = Color.BLACK;
                } else {
                    tmpColor = Color.YELLOW;
                }
                terminal.write("Recording..", statsLeft, statsTop + 14, Color.RED, tmpColor);
            } else {
                terminal.write("R->Record", statsLeft, statsTop + 12);
            }
        }
        // Frame
        Color frameColor = Color.WHITE;
        char frameGlyph = (char)8;
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
        // Color fontColor = Color.WHITE;
        char fontGlyph = (char)8;
        char fontSpaceGlyph = (char)0;
        int fontLeft = 4, fontTop = 2;
        // Calabash Knight 《葫 芦 骑 士》
        terminal.write("11100100100011110111101001001001010001011101111010010111".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop);
        terminal.write("10001010100001010100001001001010011001001001000010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 1);
        terminal.write("10001110100001110111101111001100010101001001011011110010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 2);
        terminal.write("10001010100001010000101001001010010011001001001010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 3);
        terminal.write("11101010111011110111101001001001010001011101111010010010".replace('1', fontGlyph).replace('0', fontSpaceGlyph), fontLeft, fontTop + 4);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_Q || key.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return new StartScreen();
        }
        if (this.needLoad && key.getKeyCode() == KeyEvent.VK_N) {
            loadOperationFromFile();
            return this;
        }
        switch(key.getKeyCode()) {
            case KeyEvent.VK_R: // record
                Recorder.switchStatus();
                System.out.println("start/stop record");
                break;
            case KeyEvent.VK_X: // save
                Recorder.saveStatus();
                System.out.println("save status to disk");
                break;
            default:
                KeyEventManager.addEvent(key);
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
