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

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages, int maxKeysNum) {
        Creature player = new Player(this.world, (char)2, AsciiPanel.brightWhite, 100, 20, 5, 9, maxKeysNum);
        // world.addAtEmptyLocation(player);
        world.addAtBeginning(player);
        new PlayerAI(player, messages);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(this.world, (char)3, AsciiPanel.green, 10, 0, 0, 0);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }

    public Creature newKey() {
        Creature key = new Creature(this.world, (char)13, AsciiPanel.yellow, 10, 0, 0, 0);
        world.addAtEmptyLocation(key);
        new KeysAI(key);
        return key;
    }

    public Creature newFungus(int x, int y) {
        Creature fungus = new Creature(this.world, (char)3, AsciiPanel.green, 10, 0, 0, 0);
        if (world.addAtCertainLocation(fungus, x, y)) {
            new FungusAI(fungus, this);
            return fungus;
        } else {
            return null;
        }
    }

    public Creature newKey(int x, int y) {
        Creature key = new Creature(this.world, (char)13, AsciiPanel.yellow, 10, 0, 0, 0);
        if (world.addAtCertainLocation(key, x, y)) {
            new KeysAI(key);
            return key;
        } else {
            return null;
        }
    }
}
