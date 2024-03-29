package com.jwork.app.world;

import java.util.Random;

import com.jwork.app.map.MapGenerator;
import com.jwork.app.maze.*;

/*
 * Copyright (C) 2015 s-zhouj
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
 * @author s-zhouj
 */
public class WorldBuilder {

    private int width;
    private int height;
    private Tile[][] tiles;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private WorldBuilder randomizeTiles() {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                Random rand = new Random();
                switch (rand.nextInt(World.TILE_TYPES)) {
                    case 0:
                        tiles[width][height] = Tile.FLOOR;
                        break;
                    case 1:
                        tiles[width][height] = Tile.WALL;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder mazeTiles() {
        MazeGenerator mazeGenerator = new MazeGenerator(this.width, this.height);
        int[][] maze;
        while(true) {
            mazeGenerator.generateMaze();
            maze = mazeGenerator.getMaze();
            if (maze[height-1][width-1] == 1) {
                break;
            }
        }
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                switch (maze[y][x]) {
                    case 0:
                        tiles[x][y] = Tile.WALL;
                        break;
                    case 1:
                        tiles[x][y] = Tile.FLOOR;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder mapTiles(String mapFile) {
        MapGenerator mapGenerator = new MapGenerator(mapFile);
        mapGenerator.generateMap();
        this.height = mapGenerator.getHeight();
        this.width = mapGenerator.getWidth();
        this.tiles = new Tile[width][height];
        int [][] map = mapGenerator.getMap();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                switch (map[y][x]) {
                    case 0:
                        tiles[x][y] = Tile.FLOOR;
                        break;
                    case 1:
                        tiles[x][y] = Tile.WALL;
                        break;
                    case 2:
                        tiles[x][y] = Tile.ROCK;
                        break;
                    case 3:
                        tiles[x][y] = Tile.TREE;
                        break;
                    case 4:
                        tiles[x][y] = Tile.GRASS;
                        break;
                    case 5:
                        tiles[x][y] = Tile.FLOWER;
                        break;
                    case 6:
                        tiles[x][y] = Tile.ENDING;
                        break;
                    default:
                        tiles[x][y] = Tile.FLOOR;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder smooth(int factor) {
        Tile[][] newtemp = new Tile[width][height];
        if (factor > 1) {
            smooth(factor - 1);
        }
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                // Surrounding walls and floor
                int surrwalls = 0;
                int surrfloor = 0;

                // Check the tiles in a 3x3 area around center tile
                for (int dwidth = -1; dwidth < 2; dwidth++) {
                    for (int dheight = -1; dheight < 2; dheight++) {
                        if (width + dwidth < 0 || width + dwidth >= this.width || height + dheight < 0
                                || height + dheight >= this.height) {
                            continue;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.FLOOR) {
                            surrfloor++;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.WALL) {
                            surrwalls++;
                        }
                    }
                }
                Tile replacement;
                if (surrwalls > surrfloor) {
                    replacement = Tile.WALL;
                } else {
                    replacement = Tile.FLOOR;
                }
                newtemp[width][height] = replacement;
            }
        }
        tiles = newtemp;
        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(8);
    }

    public WorldBuilder makeMaze() {
        return mazeTiles();
    }

    public WorldBuilder makeMap(String mapFile) {
        return mapTiles(mapFile);
    }
}
