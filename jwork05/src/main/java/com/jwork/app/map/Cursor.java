package com.jwork.app.map;

import com.jwork.app.world.*;

import java.awt.Color;

public class Cursor extends Player {
    public Cursor(World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
    }
    
    @Override
    public boolean moveBy(int mx, int my) {
        return this.ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
    }

    @Override
    public boolean showable() {
        return false;
    }
}
