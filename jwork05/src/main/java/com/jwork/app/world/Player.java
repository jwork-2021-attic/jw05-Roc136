package com.jwork.app.world;

import java.awt.Color;

public class Player extends Creature {

    private int xTarget = 1;
    private int yTarget = 0;

    public Player(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
        this.score = 0;
    }

    public Player(int id, World world, CreatureFactory factory, char glyph, int camp, Color color, int maxHP, int attack, int defense, int visionRadius, int actionTime) {
        super(id, world, factory, glyph, camp, color, maxHP, attack, defense, visionRadius);
        this.score = 0;
        this.actionTime = actionTime;
    }

    @Override
    public boolean moveBy(int mx, int my) {
        xTarget = mx;
        yTarget = my;
        Creature other = world.creature(x + mx, y + my);

        if (other != null) {
            this.modifyHP(-Math.max(0, other.attackValue() - this.defenseValue()));
            return ai.onEnter(x - mx, y - my, world.tile(x - mx, y - my)); // 反弹
        } else {
            return ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        }
    }

    public void pick(Creature other) {
        this.notify("You pick a %s.", other.glyph);
        this.score++;
        other.notify("The '%s' picks you.", glyph);
        other.modifyHP(0x8fffffff);
    }

    @Override
    public void shot() {
        Creature monster = this.world.getNearlyMonster(this);
        if (monster != null) {
            this.shot(monster);
        } else {
            super.shot(xTarget, yTarget);
        }
    }
    
    @Override
    public boolean win() {
        return ((PlayerAI)this.ai).win();
    }
}
