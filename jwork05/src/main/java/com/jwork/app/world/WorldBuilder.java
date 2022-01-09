package com.jwork.app.world;

// import java.util.Random;

import com.jwork.app.map.MapGenerator;
// import com.jwork.app.maze.*;

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
    private int [][] map;

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

    private WorldBuilder mapTiles(String mapFile) {
        MapGenerator mapGenerator = new MapGenerator(mapFile);
        mapGenerator.generateMap();
        this.height = mapGenerator.getHeight();
        this.width = mapGenerator.getWidth();
        this.tiles = new Tile[width][height];
        this.map = mapGenerator.getMap();
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
                    case 7:
                        tiles[x][y] = Tile.BEGINNING;
                        break;
                    default:
                        tiles[x][y] = Tile.FLOOR;
                        break;
                }
            }
        }
        return this;
    }

    public WorldBuilder makeMap(String mapFile) {
        return mapTiles(mapFile);
    }

    public int[][] getMap() {
        return map;
    }
}
