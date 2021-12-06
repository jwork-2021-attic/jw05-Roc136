package com.jwork.app.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public class World {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Creature> bullets;
    private Creature player;
    public static int maxMonsterNum = 10;
    Lock lockForCreatureChange = new ReentrantLock();
    Lock lockForBulletChange = new ReentrantLock();

    public static final int TILE_TYPES = 1;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public void setTile(Tile t, int x, int y) {
        tiles[x][y] = t;
    }

    public char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        lockForCreatureChange.lock();
        try {
            this.creatures.add(creature);
        } finally {
            lockForCreatureChange.unlock();
        }
    }

    public void addAtBeginning(Creature creature) {
        creature.setX(1);
        creature.setY(1);
        lockForCreatureChange.lock();
        try {
            this.creatures.add(creature);
        } finally {
            lockForCreatureChange.unlock();
        }
    }

    public boolean addAtCertainLocation(Creature creature, int x, int y) {
        if (tile(x, y).isGround() || this.creature(x, y) != null) {
            creature.setX(x);
            creature.setY(y);
            lockForCreatureChange.lock();
            try {
                this.creatures.add(creature);
            } finally {
                lockForCreatureChange.unlock();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean addBullletAtCertainLocation(Creature bullet, int x, int y) {
        if (tile(x, y).isGround() || this.creature(x, y) != null) {
            bullet.setX(x);
            bullet.setY(y);
            lockForBulletChange.lock();
            try {
                this.bullets.add(bullet);
            } finally {
                lockForBulletChange.unlock();
            }
            return true;
        } else {
            return false;
        }
    }

    public void setPlayer(Creature creature) {
        this.player = creature;
    }

    public Creature player() {
        return this.player;
    }

    public Creature getNearlyMonster(Creature player) {
        int minDistance2 = 0x7fffffff;
        int x = player.x();
        int y = player.y();
        Creature monster = null;
        lockForCreatureChange.lock();
        try {
            for(Creature c: this.creatures) {
                if (c.camp() == 2 && player.canSee(c.x(), c.y())) {
                    int distance2 = (x - c.x()) * (x - c.x()) + (y - c.y()) * (y - c.y());
                    if (distance2 < minDistance2) {
                        monster = c;
                        minDistance2 = distance2;
                    }
                }
            }
            return monster;
        } finally {
            lockForCreatureChange.unlock();
        }
    }

    public Creature creature(int x, int y) {
        lockForCreatureChange.lock();
        try {
            for (Creature c : this.creatures) {
                if (c.x() == x && c.y() == y) {
                    return c;
                }
            }
        } finally {
            lockForCreatureChange.unlock();
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public List<Creature> getBullets() {
        lockForCreatureChange.lock();
        try {
            return this.bullets;
        } finally {
            lockForCreatureChange.unlock();
        }
    }

    public void remove(Creature target) {
        lockForCreatureChange.lock();
        try {
            this.creatures.remove(target);
        } finally {
            lockForCreatureChange.unlock();
        }
    }

    public void removeBullet(Creature target) {
        lockForBulletChange.lock();
        try {
            this.bullets.remove(target);
        } finally {
            lockForBulletChange.unlock();
        }
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }
    }
}
