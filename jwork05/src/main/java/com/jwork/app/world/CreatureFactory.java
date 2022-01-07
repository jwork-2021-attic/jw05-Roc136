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
package com.jwork.app.world;

import java.util.List;

import com.jwork.app.asciiPanel.AsciiPanel;
import com.jwork.app.utils.RandomUtil;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Player(0, this.world, this, (char)2, 1, AsciiPanel.brightWhite, 100, 20, 5, 9, 20);
        // world.addAtEmptyLocation(player);
        world.addAtBeginning(player);
        new PlayerAI(player, messages);
        return player;
    }

    public Creature newMonster() {
        Creature monster = new Monster(RandomUtil.getRandom(10000, 99999), this.world, this, (char)13, 2, AsciiPanel.brightRed, 5, 5, 3, 9);
        world.addAtEmptyLocation(monster);
        new MonsterAI(monster);
        // new MonsterAI(monster, this);
        return monster;
    }

    public Creature newMonster(int actionTime) {
        Creature monster = new Monster(RandomUtil.getRandom(10000, 99999), this.world, this, (char)13, 2, AsciiPanel.brightRed, 5, 5, 3, 9, actionTime);
        world.addAtEmptyLocation(monster);
        new MonsterAI(monster);
        // new MonsterAI(monster, this);
        return monster;
    }

    public Creature newBullet(int camp, int x, int y, int xSpeed, int ySpeed, int actionTime, int attack, Creature host) {
        Creature bullet = new Bullet(-1, this.world, this, (char)7, camp, AsciiPanel.brightYellow, 10000, attack, 0, 0, actionTime, host);
        if (world.addBullletAtCertainLocation(bullet, x, y)) {
            new BulletAI(bullet, xSpeed, ySpeed);
            return bullet;
        } else {
            return null;
        }
    }
}
