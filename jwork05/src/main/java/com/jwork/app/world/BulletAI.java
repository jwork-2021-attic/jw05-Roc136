package com.jwork.app.world;

public class BulletAI extends CreatureAI {

    private int xSpeed;
    private int ySpeed;

    /**
     * 
     * @param creature bullet
     * @param xSpeed -1,0,1
     * @param ySpeed -1,0,1
     */
    public BulletAI(Creature creature, int xSpeed, int ySpeed) {
        super(creature);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        if (xSpeed == 0 && ySpeed == 0) {
            xSpeed = 1;
        }
    }

    @Override
    public boolean onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void onUpdate() {
        creature.moveBy(xSpeed, ySpeed);
    }

    public int xSpeed() {
        return xSpeed;
    }

    public int ySpeed() {
        return ySpeed;
    }
}
