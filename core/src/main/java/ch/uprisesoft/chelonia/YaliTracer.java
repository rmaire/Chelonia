/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.runtime.interpreter.Tracer;
import ch.uprisesoft.yali.scope.Environment;
import java.util.List;

/**
 *
 * @author rmaire
 */
public class YaliTracer implements Tracer {

    @Override
    public void parse(String source) {
        
    }

    @Override
    public void start(Node node) {
        
    }

    @Override
    public void callPrimitive(String name, List<Node> args, Environment env) {
        System.out.println("Calling primitive Procedure " + name + " with args " + args.toString());
    }

    @Override
    public void call(String name, List<Node> args, Environment env) {
        System.out.println("Calling Procedure " + name + " with args " + args.toString());
    }

    @Override
    public void make(String name, Node val, Environment env) {
        
    }

    @Override
    public void thing(String name, Node val, Environment env) {
        
    }

    @Override
    public void local(String name, Environment env) {
        
    }

    @Override
    public void run(Node val) {
        
    }

    @Override
    public void tick(Node val) {
        
    }

    @Override
    public void apply(Node val) {
        
    }

    @Override
    public void pause(Node val) {
        
    }

    @Override
    public void resume(Node val) {
        
    }
    
}
