package com.jwork.app.world;

import java.util.Random;

public class MonsterAI extends CreatureAI {

    private Random random;
    // private CreatureFactory factory;

    public MonsterAI(Creature creature) {
        super(creature);
        random = new Random();
    }

    // public MonsterAI(Creature creature, CreatureFactory factory) {
    //     super(creature);
    //     random = new Random();
    //     this.factory = factory;
    // }

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
        // System.out.println("[" + Thread.currentThread().getName() + "](MonsterAI)onUpdate");
        boolean flag = false;
        try {
            flag = creature.canSee(creature.world.player().x(), creature.world.player().y());
        } catch (Exception e) {
            System.out.println("[" + Thread.currentThread().getName() + "](MonsterAI)canSee error");
            e.printStackTrace();
        }
        if (flag) {
            // System.out.println("[" + Thread.currentThread().getName() + "](MonsterAI)send bullet");
            moveRandomly();
            if (random.nextInt(100) < 50) {
                creature.shot(creature.world.player());
            }
        } else {
            // System.out.println("[" + Thread.currentThread().getName() + "](MonsterAI)move randomly");
            moveRandomly();
            if (random.nextInt(100) < 0) {
                creature.shot();
            }
        }
    }
}
