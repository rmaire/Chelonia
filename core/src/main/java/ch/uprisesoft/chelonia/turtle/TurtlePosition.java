/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.turtle;

/**
 *
 * @author rma
 */
public class TurtlePosition {

    public final float x;
    public final float y;
    public final float angle;
    public final boolean pendown;

    public TurtlePosition(float x, float y, float angle, boolean pendown) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.pendown = pendown;
    }
}