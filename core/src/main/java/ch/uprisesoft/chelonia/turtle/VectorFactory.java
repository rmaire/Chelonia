/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.turtle;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rma
 */
public class VectorFactory {
    
    public static List<Vector2> fromTurtlePositionList(List<TurtlePosition> turtlePositions) {
        List<Vector2> turtleVectors = new ArrayList<>();
        
        for(TurtlePosition tp : turtlePositions) {
            turtleVectors.add(new Vector2(tp.x, tp.y));
        }
        
        return turtleVectors;
    }
    
}
