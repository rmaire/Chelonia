/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.Procedure;
import ch.uprisesoft.yali.ast.node.word.QuotedWord;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.procedures.ProcedureProvider;
import ch.uprisesoft.yali.scope.Scope;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rma
 */
public class Workspace implements ProcedureProvider {
    
    private final Chelonia parent;
    
    private Interpreter interpreter;

    private static Object lock = new Object();

    public Workspace(Interpreter interpreter, Chelonia parent) {
        this.interpreter = interpreter;
        this.parent = parent;
    }

    private Node edit(Scope scope, java.util.List<Node> args) {
        QuotedWord name = args.get(0).toQuotedWord();

        if (!interpreter.env().defined(name.getQuote())) {
            return Node.none();
        }

        final Procedure functionToEdit = (Procedure) interpreter.env().procedure(name.getQuote());
        // TODO in yali
        parent.edit(functionToEdit.getSource());

//        parent.setScreen(new EditScreen(continuation.getInterpreter(), parent, functionToEdit.getSource()));
//        parent.switchToEditor(functionToEdit.getSource());

//        YaliEditor ye = new YaliEditor(functionToEdit.getSource(), interpreter);

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                ye.setVisible(true);
//            }
//        });

        return Node.nil();
    }

    @Override
    public Interpreter registerProcedures(Interpreter it) {
        it.env().define(new Procedure("edit", (scope, val) -> this.edit(scope, val), (scope, val) -> Node.none(), "name"));
        
        return it;
    }
}