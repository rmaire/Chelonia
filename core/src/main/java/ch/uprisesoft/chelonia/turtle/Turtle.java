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

    private List<TurtlePosition> positions;
    private Boolean visible = true;

    public Turtle() {
        this(new TurtlePosition(0, 0, 0));
    }

    public Turtle(TurtlePosition init) {
        positions = new ArrayList<>();
        positions.add(init);
    }

    public List<TurtlePosition> getPositions() {
        return positions;
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
        positions.add(new TurtlePosition(0, 0, 0));
    }
}
