package com.jwork.app.map;

import com.jwork.app.screen.*;
import com.jwork.app.world.*;
import com.opencsv.CSVWriter;
import com.jwork.app.asciiPanel.AsciiPanel;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class MapEditer extends PlayScreen {

    protected int[][] tmpMap;
    protected boolean isNew;

    public MapEditer(int mapSelector, boolean isNew) {
        super(mapSelector);
        this.isNew = isNew;
    }

    @Override
    protected void initWorld() {
        createWorld();
    }

    @Override
    protected void createWorld() {
        WorldBuilder wb;
        if (isNew) {
            wb = new WorldBuilder(worldWidth, worldHeight).makeMap("map/map0.csv");
        } else {
            wb = new WorldBuilder(worldWidth, worldHeight).makeMap(map);
        }
        world = wb.build();
        tmpMap = wb.getMap();
        if (world != null) {
            this.player = new Cursor(world, null, (char)2, 1, AsciiPanel.brightWhite, 100, 20, 5, 9);
            world.addAtBeginning(player);
            new CreatureAI((Creature)player);
            // new PlayerAI(player, messages);
        }
    }

    @Override
    protected void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < screenWidth && x < world.width(); x++) {
            for (int y = 0; y < screenHeight && y < world.height(); y++) {
                int wx = x + left;
                int wy = y + top;
                // Function<Color, Color> colorToContrary = (Color color) -> {return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()); };
                if (wx == player.x() && wy == player.y()) {
                    if (world.color(wx, wy) != null) {
                        terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop, world.color(wx, wy), Color.red);//colorToContrary.apply(world.color(wx, wy)));
                    } else {
                        terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop, Color.white, Color.red);
                    }
                } else {
                    if (world.color(wx, wy) != null) {
                        terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop, world.color(wx, wy));
                    } else {
                        terminal.write(world.glyph(wx, wy), x + screenFrameLeft, y + screenFrameTop);
                    }
                }
            }
        }
    }

    private void saveMap() {
        // System.out.println(MapEditer.class.getClassLoader().getResource(".").getPath() + "/" + map);
        File file = new File(MapEditer.class.getClassLoader().getResource(".").getPath() + "/" + map);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
            Writer writer = new FileWriter(MapEditer.class.getClassLoader().getResource(map).getPath());
            CSVWriter csvWriter = new CSVWriter(writer);
        ) {
            for (int i = 0; i < tmpMap.length; i++) {
                csvWriter.writeNext(Arrays.stream(tmpMap[i]).mapToObj(String::valueOf).toArray(String[]::new));
            }
            // csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile getTileFromInt(int x) {
        tmpMap[player.y()][player.x()] = x;
        switch(x) {
            case 0:
                return Tile.FLOOR;
            case 1:
                return Tile.WALL;
            case 2:
                return Tile.ROCK;
            case 3:
                return Tile.TREE;
            case 4:
                return Tile.GRASS;
            case 5:
                return Tile.FLOWER;
            case 6:
                return Tile.ENDING;
            default:
                tmpMap[player.y()][player.x()] = 0;
                return Tile.FLOOR;
        }
    }

    private Tile getNextTile(Tile t) {
        if (t == Tile.BEGINNING) {
            tmpMap[player.y()][player.x()] = 7;
            return Tile.BEGINNING;
        } else if (t == Tile.ENDING) {
            tmpMap[player.y()][player.x()] = 6;
            return Tile.ENDING;
        } else if (t == Tile.FLOOR) {
            tmpMap[player.y()][player.x()] = 1;
            return Tile.WALL;
        } else if (t == Tile.WALL) {
            tmpMap[player.y()][player.x()] = 2;
            return Tile.ROCK;
        } else if (t == Tile.ROCK) {
            tmpMap[player.y()][player.x()] = 3;
            return Tile.TREE;
        } else if (t == Tile.TREE) {
            tmpMap[player.y()][player.x()] = 4;
            return Tile.GRASS;
        } else if (t == Tile.GRASS) {
            tmpMap[player.y()][player.x()] = 5;
            return Tile.FLOWER;
        } else if (t == Tile.FLOWER) {
            tmpMap[player.y()][player.x()] = 0;
            return Tile.FLOOR;
        }
        return Tile.FLOOR;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_Q:

                return new StartScreen();
            case KeyEvent.VK_ENTER:
                if (world.tile(player.x(), player.y()) == Tile.ENDING) {
                    saveMap();
                    return new StartScreen();
                } else {
                    world.setTile(getNextTile(world.tile(player.x(), player.y())), player.x(), player.y());
                }
                break;
            case KeyEvent.VK_0:
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
                if (world.tile(player.x(), player.y()) != Tile.ENDING && world.tile(player.x(), player.y()) != Tile.BEGINNING) {
                    world.setTile(getTileFromInt(key.getKeyCode() - KeyEvent.VK_0), player.x(), player.y());
                }
                break;
            case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP: case KeyEvent.VK_W:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
                player.moveBy(0, 1);
                break;
        }
        // for (int i = 0; i < tmpMap.length; i++) {
        //     for (int j = 0; j < tmpMap[i].length; j++) {
        //         System.out.print(tmpMap[i][j]);
        //     }
        //     System.out.println("");
        // }
        return this;
    }
}
