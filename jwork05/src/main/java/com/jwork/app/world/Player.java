package com.jwork.app.world;

import java.awt.Color;

public class Player extends Creature {

    public Player(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
        this.score = 0;
    }

    @Override
    public boolean moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (other != null) {
            pick(other);
        }
        // setFootPrints(mx, my);
        return ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
    }

    public void pick(Creature other) {
        this.notify("You pick a %s.", other.glyph);
        this.score++;
        other.notify("The '%s' picks you.", glyph);
        other.modifyHP(0x8fffffff);
    }
    
    @Override
    public boolean win() {
        return false;
    }
}
