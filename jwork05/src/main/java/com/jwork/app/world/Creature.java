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

import java.awt.Color;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature {

    protected World world;

    protected int x;

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    protected int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    protected char glyph;

    public char glyph() {
        return this.glyph;
    }

    protected Color color;

    public Color color() {
        return this.color;
    }

    protected CreatureAI ai;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    protected int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    protected int hp;

    public int hp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        this.hp += amount;

        if (this.hp < 1) {
            world.remove(this);
        }
    }

    protected int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    protected int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    protected int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    protected int score;

    public int score() {
        return score;
    }

    public boolean canSee(int wx, int wy) {
        return ai.canSee(wx, wy);
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public boolean moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (other == null) {
            setFootPrints(mx, my);
            return ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        } else {
            attack(other);
            return false;
        }
    }

    public boolean win() {
        if (this.world.tile(x, y).isEnding()) {
            return true;
        } else {
            return false;
        }
    }

    protected void setFootPrints(int mx, int my) {
        if (world.tile(x, y).isBeginning() || world.tile(x, y).isEnding()) {
            return;
        }
        if (mx == -1) {
            this.world.setTile(Tile.LEFT, x, y);
        } else if (mx == 1) {
            this.world.setTile(Tile.RIGHT, x, y);
        } else if (my == -1) {
            this.world.setTile(Tile.UP, x, y);
        } else if (my == 1) {
            this.world.setTile(Tile.DOWN, x, y);
        }
    }

    public void attack(Creature other) {
        int damage = Math.max(0, this.attackValue() - other.defenseValue());
        damage = (int) (Math.random() * damage) + 1;

        this.notify("You attack the '%s' for %d damage.", other.glyph, damage);
        other.notify("The '%s' attacks you for %d damage.", glyph, damage);

        other.modifyHP(-damage);
    }

    public void update() {
        if (this.ai != null) {
            this.ai.onUpdate();
        }
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public Creature(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.ai = null;
        this.score = 0;
    }
}
