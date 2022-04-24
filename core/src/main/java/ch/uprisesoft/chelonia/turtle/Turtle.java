/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.turtle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rma
 */
public class Turtle {

    private float pps = 200;
    private int actualPosIndex = 1;

    private List<TurtlePosition> positions;
    private List<TurtlePosition> animatedPositions;
    private Boolean visible = true;

    public Turtle() {
        this(new TurtlePosition(0, 0, 0));
    }

    public Turtle(TurtlePosition init) {
        positions = new ArrayList<>();
        animatedPositions = new ArrayList<>();
        positions.add(init);
        animatedPositions.add(init);
    }

    public List<TurtlePosition> getPositions() {
        return positions;
    }

    private float run(float angle) {
        double run = Math.sin(Math.toRadians(angle));
        return (float) run;
    }

    private float rise(float angle) {
        double rise = Math.cos(Math.toRadians(angle));
        return (float) rise;
    }

    public List<TurtlePosition> getPositions(float delta) {

        if (pps == 0) {
            return positions;
        }

        if (positions.size() < 2) {
            return positions;
        }

        if (actualPosIndex == positions.size()) {
            return animatedPositions;
        }

        if (animatedPositions.size() <= actualPosIndex) {
            animatedPositions.add(positions.get(actualPosIndex - 1));
        }

        float dt = delta * pps;
        float angle = positions.get(actualPosIndex - 1).angle;
        TurtlePosition actualPos = animatedPositions.get(actualPosIndex);
        TurtlePosition targetPos = positions.get(actualPosIndex);
        float x;
        float y;
        boolean finishedX = false;
        boolean finishedY = false;

        // check if x goes in positive direction
        boolean xGoesPositive = (targetPos.x - actualPos.x) > 0;
        if (xGoesPositive) {
            if (actualPos.x < targetPos.x && (actualPos.x + dt) < targetPos.x) {
                x = actualPos.x + dt * run(angle);
            } else {
                x = targetPos.x;
                finishedX = true;
            }
        } else {
            if (actualPos.x > targetPos.x && (actualPos.x - dt) > targetPos.x) {
                x = actualPos.x + dt * run(angle);
            } else {
                x = targetPos.x;
                finishedX = true;
            }
        }

        // check if y goes in positive direction
        boolean yGoesPositive = (targetPos.y - actualPos.y) > 0;
        if (yGoesPositive) {
            if (actualPos.y < targetPos.y && (actualPos.y + dt) < targetPos.y) {
                y = actualPos.y + dt * rise(angle);
            } else {
                y = targetPos.y;
                finishedY = true;
            }
        } else {
            if (actualPos.y > targetPos.y && (actualPos.y - dt) > targetPos.y) {
                y = actualPos.y + dt * rise(angle);
            } else {
                y = targetPos.y;
                finishedY = true;
            }
        }

        if (finishedX && finishedY) {
            actualPosIndex++;
        }

        TurtlePosition newPos = new TurtlePosition(x, y, targetPos.angle);
        animatedPositions.remove(animatedPositions.size() - 1);
        animatedPositions.add(newPos);

        return animatedPositions;
    }

    public List<TurtlePosition> getHead() {
        List<TurtlePosition> newPositions = new ArrayList<>();
        newPositions.addAll(positions);
        if (visible) {
            List<TurtlePosition> oldPositions = new ArrayList<>();
            oldPositions.addAll(positions);

            this.lt(90);
            this.fd(5);
            this.rt(180 - 71.565f);
            this.fd(16);
            this.rt(180 - 36.87f);
            this.fd(16);
            this.rt(180 - 71.565f);
            this.fd(5);
            newPositions.clear();
            newPositions.addAll(positions);
            positions.clear();
            positions.addAll(oldPositions);
        }
        return newPositions;
    }

    public void fd(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy + (float) (steps * Math.cos(Math.toRadians(angle)));
        float newx = oldx + (float) (steps * Math.sin(Math.toRadians(angle)));
        positions.add(new TurtlePosition(newx, newy, angle));
    }

    public void bk(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy - (float) (steps * Math.cos(Math.toRadians(angle)));
        float newx = oldx - (float) (steps * Math.sin(Math.toRadians(angle)));
        positions.add(new TurtlePosition(newx, newy, angle));
    }

    public void lt(float degrees) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle - degrees));
    }

    public void rt(float degrees) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle + degrees));
    }

    public void st() {
        this.visible = true;
    }

    public void ht() {
        this.visible = false;
    }

    public void cs() {
        positions.clear();
        animatedPositions.clear();
        actualPosIndex = 1;
        positions.add(new TurtlePosition(0, 0, 0));
        animatedPositions.add(new TurtlePosition(0, 0, 0));
    }
}
