/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.scope.VariableNotFoundException;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogLevel;
import java.util.ResourceBundle;

/**
 *
 * @author rma
 */
public class ReplConsole extends GUIConsole {

    private Interpreter it;
    private ResourceBundle messages;

    public ReplConsole(Interpreter it, ResourceBundle messages) {
        super();
        this.it = it;
        this.messages = messages;
    }

    @Override
    public void execCommand(String command) {
        log(command, LogLevel.COMMAND);
//        log(it.run(it.read(command)).toString(), LogLevel.SUCCESS);
        run(command);
    }

    private void run(String source) {
        try {
            Node result = it.run(it.read(source));
            log("; " + result.toString(), LogLevel.SUCCESS);
        } catch (NodeTypeException nte) {
            if (nte.getExpected().contains(NodeType.PROCCALL) && nte.getReceived().equals(NodeType.SYMBOL)) {
                log(String.format(
                        "; " + messages.getString("function_not_found"),
                        nte.getNode().getToken().getLexeme(),
                        nte.getReceived()
                ),
                        LogLevel.ERROR
                );
            } else if (nte.getExpected().contains(NodeType.PROCCALL)) {
                log(String.format(
                        "; " + messages.getString("redundant_argument"),
                        nte.getNode().getToken().getLexeme(),
                        nte.getReceived()
                ),
                        LogLevel.ERROR
                );
            } else {
                log(String.format(
                        "; " + messages.getString("not_expected"),
                        nte.getNode().toString(),
                        nte.getExpected(),
                        nte.getReceived()
                ),
                        LogLevel.ERROR
                );
            }
        } catch (VariableNotFoundException vnfe) {
            log(String.format(
                    "; " + messages.getString("variable_not_found"),
                    vnfe.getName()
            ),
                    LogLevel.ERROR
            );
        }
    }
}
