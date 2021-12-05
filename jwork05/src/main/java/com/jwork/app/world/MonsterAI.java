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

    private boolean moveRandomly() {
        int target = random.nextInt(4);
        boolean res = false;
        switch(target) {
            case 0:
                res = creature.moveBy(0, 1);
                break;
            case 1:
                res = creature.moveBy(0, -1);
                break;
            case 2:
                res = creature.moveBy(1, 0);
                break;
            case 3:
                res = creature.moveBy(-1, 0);
                break;
        }
        return res;
    }

    @Override
    public void onUpdate() {
        boolean flag = false;
        if (flag) {
            //
        } else {
            moveRandomly();
        }
    }
    
}
