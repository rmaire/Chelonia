/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogLevel;

/**
 *
 * @author rma
 */
public class ReplConsole extends GUIConsole{
    
    private Interpreter it;

    public ReplConsole(Interpreter it) {
        super();
        this.it = it;
    }
    
    
    
    @Override public void execCommand (String command) {
        log(command, LogLevel.COMMAND);
        log(it.run(it.read(command)).toString(), LogLevel.SUCCESS);
//        System.out.println(it.run(it.read(command)));
    }
}
