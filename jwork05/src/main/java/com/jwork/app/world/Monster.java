package com.jwork.app.world;

import java.awt.Color;

public class Monster extends Creature {

    public Monster(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
    }

    public Monster(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius, int actionTime) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
        this.actionTime = actionTime;
    }
    
}
