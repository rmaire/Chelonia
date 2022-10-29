/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.turtle;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rma
 */
public class Turtle2 {

    // Animation speed. 0 means no animation
    private float pps = 0;
    private float actualDelta = 0;

    // Position of animation. Points to the actual TurtlePosition in positions
    private int actualPosIndex = 1;

    private List<TurtlePosition> positions;
    private List<TurtlePosition> animatedPositions;
    private boolean visible = true;
    private boolean pendown = true;
    private boolean angleChanged = false;
    private Color actualColor = new Color(Color.WHITE);

    public Turtle2() {
        this(new TurtlePosition(0, 0, 0, true, new Color(Color.WHITE)), 10);
    }

    public Turtle2(TurtlePosition init, int turtlespeed) {
        positions = new ArrayList<>();
        animatedPositions = new ArrayList<>();
        positions.add(init);
//        animatedPositions.add(init);
        pps = turtlespeed * 20;
    }

    public TurtlePosition getPosition() {
        return positions.get(positions.size() - 1);
    }

    // pu fd 100 pd repeat 4 [fd 100 rt 90]
    public List<TurtlePosition> getPositions(float delta) {

        if (angleChanged && !animatedPositions.isEmpty()) {
            TurtlePosition lastPos = animatedPositions.get(animatedPositions.size() - 1);
            float newAngle = positions.get(positions.size() - 1).angle;
            TurtlePosition posWithNewAngle = new TurtlePosition(lastPos.x, lastPos.y, newAngle, lastPos.pendown, lastPos.color);
            animatedPositions.set(animatedPositions.size() - 1, posWithNewAngle);
            angleChanged = false;
        }

        // Nothing more to do, just return calculated positions
        if (actualPosIndex == positions.size()) {
            return animatedPositions;
        }

        // No animation necessary
        if (pps == 0) {
            animatedPositions.clear();
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

        TurtlePosition actualPos = animatedPositions.get(animatedPositions.size() - 1);
        TurtlePosition targetPos = positions.get(actualPosIndex);
        float angle = actualPos.angle;
        float newx;
        float newy;
        float newangle = targetPos.angle;
        Color newColor = actualPos.color;
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

        if (finishedX && finishedY) {
            newColor = targetPos.color;
            actualPosIndex++;
        }

        TurtlePosition newPos = new TurtlePosition(newx, newy, newangle, positions.get(actualPosIndex - 1).pendown, newColor);
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
        List<TurtlePosition> ret = getPositions(actualDelta);
        actualDelta = delta;
        return ret;

//        if (visible) {
//            List<TurtlePosition> newPositions = new ArrayList<>();
//            List<TurtlePosition> oldPositions = new ArrayList<>();
//            oldPositions.addAll(positions);
//            positions.clear();
//            positions.addAll(animatedPositions);
//            
//            boolean oldpen = pendown;
//
//            this.pd();
//
//            this.lt(90);
//            this.fd(5);
//            this.rt(180 - 71.565f);
//            this.fd(16);
//            this.rt(180 - 36.87f);
//            this.fd(16);
//            this.rt(180 - 71.565f);
//            this.fd(5);
//
//            pendown = oldpen;
//
//            newPositions.clear();
//            newPositions.addAll(positions);
//            positions.clear();
//            positions.addAll(oldPositions);
//            return newPositions;
//        } else {
//            return animatedPositions;
//        }
    }

    public void fd(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy + (float) (steps * rise(angle));
        float newx = oldx + (float) (steps * run(angle));
        positions.add(new TurtlePosition(newx, newy, angle, pendown, actualColor));
    }

    public void bk(int steps) {
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        float angle = oldpos.angle;
        float oldx = oldpos.x;
        float oldy = oldpos.y;
        float newy = oldy - (float) (steps * rise(angle));
        float newx = oldx - (float) (steps * run(angle));
        positions.add(new TurtlePosition(newx, newy, angle, pendown, actualColor));
    }

    public void lt(float degrees) {
        angleChanged = true;

        TurtlePosition oldpos = positions.get(positions.size() - 1);

        float newAngle = oldpos.angle - degrees;

        if (newAngle <= 0) {
            newAngle += 360;
        }

        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, newAngle, oldpos.pendown, actualColor));
    }

    public void rt(float degrees) {
        angleChanged = true;

        TurtlePosition oldpos = positions.get(positions.size() - 1);

        float newAngle = oldpos.angle + degrees;

        if (newAngle >= 360) {
            newAngle -= 360;
        }

        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, newAngle, oldpos.pendown, actualColor));
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
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown, actualColor));

        if (!animatedPositions.isEmpty()) {
            TurtlePosition oldapos = animatedPositions.get(animatedPositions.size() - 1);
            animatedPositions.set(animatedPositions.size() - 1, new TurtlePosition(oldapos.x, oldapos.y, oldapos.angle, pendown, actualColor));
        }

        System.out.println("positions: " + positions.size() + ", animatedPositions: " + animatedPositions.size());
    }

    public void pu() {
        pendown = false;

        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown, actualColor));

        if (!animatedPositions.isEmpty()) {
            TurtlePosition oldapos = animatedPositions.get(animatedPositions.size() - 1);
            animatedPositions.set(animatedPositions.size() - 1, new TurtlePosition(oldapos.x, oldapos.y, oldapos.angle, pendown, actualColor));
        }

        System.out.println("positions: " + positions.size() + ", animatedPositions: " + animatedPositions.size());
    }

    public void setxy(double x, double y) {
        float oldpps = pps;
        pps = 0;
        positions.add(new TurtlePosition((float) x, (float) y, positions.get(positions.size() - 1).angle, pendown, actualColor));
        pps = oldpps;
    }

    private float getAngle(float x, float y) {
        return (float) Math.toDegrees(Math.atan(y / x));
    }

    public void setpc(float r, float g, float b) {
        actualColor = new Color(r, g, b, 1);
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown, actualColor));
    }

    public void setpc(Color color) {
        actualColor = color;
        TurtlePosition oldpos = positions.get(positions.size() - 1);
        positions.set(positions.size() - 1, new TurtlePosition(oldpos.x, oldpos.y, oldpos.angle, pendown, actualColor));

    }

    public void ts(float speed) {
        this.pps = speed * 20;
    }

    public void cs() {
        positions.clear();
        animatedPositions.clear();
        actualPosIndex = 1;
        actualColor = new Color(Color.WHITE);
        positions.add(new TurtlePosition(0, 0, 0, pendown, actualColor));
//        animatedPositions.add(positions.get(positions.size() - 1));
    }
}
