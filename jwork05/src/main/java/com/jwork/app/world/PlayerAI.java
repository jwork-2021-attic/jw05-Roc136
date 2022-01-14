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

import com.jwork.app.App;
import com.jwork.app.utils.EventManager;

import java.awt.event.KeyEvent;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayerAI extends CreatureAI {

    private List<String> messages;

    public PlayerAI(Creature creature, List<String> messages) {
        super(creature);
        this.messages = messages;
    }

    @Override
    public boolean onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            return true;
        } else if (tile.isDiggable()) {
            creature.dig(x, y);
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void onNotify(String message) {
        this.messages.add(message);
    }

    private boolean win = false;

    public boolean win() {
        return win;
    }

    @Override
    public void onUpdate() {
        // System.out.println("[" + Thread.currentThread().getName() + "](PlayerAI)onUpdate");
        Integer key = EventManager.getEvent(creature.id());
        if (key != null) {
            switch (key) {
                case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
                    creature.moveBy(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                    creature.moveBy(1, 0);
                    break;
                case KeyEvent.VK_UP: case KeyEvent.VK_W:
                    creature.moveBy(0, -1);
                    break;
                case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
                    creature.moveBy(0, 1);
                    break;
                case KeyEvent.VK_ENTER: case KeyEvent.VK_J:
                    if (creature.world.tile(creature.x(), creature.y()).isEnding()) {
                        // if (creature.score() == World.maxMonsterNum) {
                        if (creature.score() == App.playerNum()) {
                            this.win = true;
                        }
                    } else {
                        creature.shot();
                    }
                    break;
            }
        }
    }
}
