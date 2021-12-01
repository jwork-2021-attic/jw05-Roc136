package com.jwork.app.world;

import java.util.Random;

public class MonsterAI extends CreatureAI {

    private Random random;

    public MonsterAI(Creature creature) {
        super(creature);
        random = new Random();
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
    public void onUpdate() {
        int target = random.nextInt(4);
        switch(target) {
            case 0:
                creature.moveBy(0, 1);
                break;
            case 1:
                creature.moveBy(0, -1);
                break;
            case 2:
                creature.moveBy(1, 0);
                break;
            case 3:
                creature.moveBy(-1, 0);
                break;
        }
        System.out.println("(Monster)onUpdate");
    }
    
}
