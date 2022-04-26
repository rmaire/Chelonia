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

    // Animation speed. 0 means no animation
    private float pps = 0;
    
    // Position of animation. Points to the actual TurtlePosition in positions
    private int actualPosIndex = 1;

    private List<TurtlePosition> positions;
    private List<TurtlePosition> animatedPositions;
    private boolean visible = true;
    private boolean pendown = false;

    public Turtle() {
        this(new TurtlePosition(0, 0, 0, true), 10);
    }

    public Turtle(TurtlePosition init, int turtlespeed) {
        positions = new ArrayList<>();
        animatedPositions = new ArrayList<>();
        positions.add(init);
        animatedPositions.add(init);
        pps = turtlespeed * 20;
    }

    public TurtlePosition getPosition() {
        return positions.get(positions.size()-1);
    }

    public List<TurtlePosition> getPositions(float delta) {
        
        // Nothing more to do, just return calculated positions
        if (actualPosIndex == positions.size()) {
            return animatedPositions;
        }
        
        // If it's first element, just copy the actual in case pendown has changed
        if(actualPosIndex == 1) {
            animatedPositions.clear();
        }
        // No animation necessary
        if (pps == 0) {
//            animatedPositions.clear();
            animatedPositions.addAll(positions);
            return animatedPositions;
        }
        
        // If pen is up, line doesn't has to be animated
        if (!positions.get(actualPosIndex - 1).pendown) {
            animatedPositions.add(positions.get(actualPosIndex++));
            return animatedPositions;
        }
        
        // Get next element if counter was increased
        if (animatedPositions.size() <= actualPosIndex) {
            animatedPositions.add(positions.get(actualPosIndex - 1));
        }

        float dt = delta * pps;
        
        TurtlePosition actualPos = animatedPositions.get(animatedPositions.size()-1);
        TurtlePosition targetPos = positions.get(actualPosIndex);
        float angle = actualPos.angle;
        float newx;
        float newy;
        float newangle = targetPos.angle;
        boolean finishedX = false;
        boolean finishedY = false;

        // check if x goes in positive direction
        boolean xGoesPositive = (targetPos.x - actualPos.x) > 0;
        
        // If x goes up, the comparison has to be smaller than...
        if (xGoesPositive) {
            if (actualPos.x < targetPos.x && (actualPos.x + dt * run(angle)) < targetPos.x) {
                newx = actualPos.x + dt * run(angle);
                newangle = actualPos.angle;
            } else {
                newx = targetPos.x;
                finishedX = true;
            }
            // If x goes down, the comparison has to be larger than...
        } else {
            if (actualPos.x > targetPos.x && (actualPos.x + dt * run(angle)) > targetPos.x) {
                newx = actualPos.x + dt * run(angle);
                newangle = actualPos.angle;
            } else {
                newx = targetPos.x;
                finishedX = true;
            }
        }

        // check if y goes in positive direction
        boolean yGoesPositive = (targetPos.y - actualPos.y) > 0;
        if (yGoesPositive) {
            
            // If y goes right, the comparison has to be smaller than...
            if (actualPos.y < targetPos.y && (actualPos.y + dt * rise(angle)) < targetPos.y) {
                newy = actualPos.y + dt * rise(angle);
                newangle = actualPos.angle;
            } else {
                newy = targetPos.y;
                finishedY = true;
            }
            // If y goes left, the comparison has to be bigger than...
        } else {
            if (actualPos.y > targetPos.y && (actualPos.y + dt * rise(angle)) > targetPos.y) {
                newy = actualPos.y + dt * rise(angle);
                newangle = actualPos.angle;
            } else {
                newy = targetPos.y;
                finishedY = true;
            }
        }

//        TurtlePosition newPos = new TurtlePosition(newx, newy, newangle, positions.get(actualPosIndex).pendown);
        
        if (finishedX && finishedY) {
            actualPosIndex++;
        }
        
        TurtlePosition newPos = new TurtlePosition(newx, newy, newangle, positions.get(actualPosIndex-1).pendown);
        animatedPositions.set(animatedPositions.size() - 1, newPos);
        
        return animatedPositions;
    }
    
    private float run(float angle) {
        double run = Math.sin(Math.toRadians(angle));
        return (float) run;
    }

    private float rise(float angle) {
        double rise = Math.cos(Math.toRadians(angle));
        return (float) rise;
    }


    public List<TurtlePosition> getPositionsWithHead(float delta) {
        getPositions(delta);
        List<TurtlePosition> newPositions = new ArrayList<>();
        List<TurtlePosition> oldPositions = new ArrayList<>();
        oldPositions.addAll(positions);
        positions.clear();
        positions.addAll(animatedPositions);

        if (visible) {

            boolean oldpen = pendown;
            this.pd();

            this.lt(90);
            this.fd(5);
            this.rt(180 - 71.565f);
            this.fd(16);
            this.rt(180 - 36.87f);
            this.fd(16);
            this.rt(180 - 71.565f);
            this.fd(5);

            pendown = oldpen;
        }

        newPositions.clear();
        newPositions.addAll(positions);
        positions.clear();
        positions.addAll(oldPositions);
        return newPositions;
    }

    public void fd(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy + (float) (steps * rise(angle));
        float newx = oldx + (float) (steps * run(angle));
        positions.add(new TurtlePosition(newx, newy, angle, pendown));
    }

    public void bk(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy - (float) (steps * rise(angle));
        float newx = oldx - (float) (steps * run(angle));
        positions.add(new TurtlePosition(newx, newy, angle, pendown));
    }

    public void lt(float degrees) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle - degrees, pendown));
    }

    public void rt(float degrees) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle + degrees, pendown));
    }

    public void st() {
        this.visible = true;
    }

    public void ht() {
        this.visible = false;
    }

    public void pd() {
        pendown = true;
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown));
    }

    public void pu() {
        pendown = false;
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown));
    }

    public void ts(float speed) {
        this.pps = speed * 20;
    }

    public void cs() {
        positions.clear();
        animatedPositions.clear();
        actualPosIndex = 1;
        positions.add(new TurtlePosition(0, 0, 0, pendown));
        animatedPositions.add(new TurtlePosition(0, 0, 0, pendown));
    }
}
