package com.jwork.app.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BulletManager extends Thread {

    private List<Creature> bulletList = new ArrayList<>();
    private Creature host;
    private int actionTime;
    private Lock lock = new ReentrantLock();
    
    public BulletManager(Creature host, int actionTime) {
        this.host = host;
        this.actionTime = actionTime;
    }

    public void addBullet(Creature bullet) {
        lock.lock();
        try {
            bulletList.add(bullet);
        } finally {
            lock.unlock();
        }
    }

    private void update() {
        lock.lock();
        try {
            Iterator<Creature> it = bulletList.iterator();
            while(it.hasNext()) {
                Creature bullet = it.next();
                if (bullet.alive()) {
                    ((Bullet)bullet).update();
                } else {
                    it.remove();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void clear() {
        lock.lock();
        try {
            Iterator<Creature> it = bulletList.iterator();
            while(it.hasNext()) {
                Creature bullet = it.next();
                if (bullet.alive()) {
                    bullet.die();
                    it.remove();
                } else {
                    it.remove();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        try {
            while(this.host.alive()) {
                update();
                while(System.currentTimeMillis() - time < actionTime) {
                    Thread.yield();
                }
                time = System.currentTimeMillis();
            }
        } finally {
            clear();
        }
    }
}
