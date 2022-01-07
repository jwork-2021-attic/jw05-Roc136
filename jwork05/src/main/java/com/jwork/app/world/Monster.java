package com.jwork.app.world;

import java.awt.Color;

public class Monster extends Creature {

    public Monster(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
    }

    public Monster(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius, int actionTime) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
        this.actionTime = actionTime;
    }
    
}
