package com.jwork.app.world;

import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bullet extends Creature {

    private static Lock attackLock = new ReentrantLock();
    private Creature host;

    public Bullet(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius, Creature host) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
        this.host = host;
    }

    public Bullet(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius, int actionTime, Creature host) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
        this.actionTime = actionTime;
        this.host = host;
    }

    @Override
    public void die() {
        world.removeBullet(this);
        this.alive = false;
    }

    @Override
    public boolean moveBy(int mx, int my) {
        if (
            x + mx < 0 || y + my < 0 ||
            x + mx > this.world.width() - 1 ||
            y + my > this.world.height() - 1 ||
            !this.world.tile(x + mx, y + my).isGround()
        ) {
            this.die();
            return false;
        }
        Creature other = world.creature(x + mx, y + my);

        if (other == null) {
            // setFootPrints(mx, my);
            return ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        } else if (this.camp() != other.camp()){
            Bullet.attackLock.lock();
            try {
                if(this.attack(other)) {
                    this.host.addScore(1);
                }
            } finally {
                Bullet.attackLock.unlock();
            }
            this.die();
            return false;
        } else {
            // System.out.println("[" + Thread.currentThread().getName() + String.format("](Bullet)moveBy(%d, %d)",mx,my));
            return moveBy(mx + ((BulletAI)this.ai).xSpeed(), my + ((BulletAI)this.ai).ySpeed());
        }
    }

}
