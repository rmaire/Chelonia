/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.repl;

import ch.uprisesoft.chelonia.Ide;
import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.scope.VariableNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 * @author rma
 */
public class ReplTextFieldListener implements TextFieldListener {
    
    FileHandle baseFileHandle = Gdx.files.internal("i18n/Translation");
    Locale locale = new Locale("de", "CH");
    private I18NBundle messages = I18NBundle.createBundle(baseFileHandle, locale);
    private final Ide ide;
    private final Interpreter yali;

    public ReplTextFieldListener(Ide ide, Interpreter yali) {
        this.ide = ide;
        this.yali = yali;
    }

    @Override
    public void keyTyped(VisTextField vtf, char c) {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            String newContent = "";
            String areaContent = vtf.getText();
            List<String> commandLines = Arrays.asList(areaContent.split("\n"));
            List<String> commands = commandLines.stream()
                    .filter(command -> command.startsWith("> ") || command.startsWith(": "))
                    .map(command -> command.replace("> ", ""))
                    .map(command -> command.replace(": ", ""))
                    .collect(Collectors.toList());

            String lastCommandString = commands.get(commands.size() - 1);

            try {
                if (lastCommandString.trim().equals("")) {
                } else if (lastCommandString.toLowerCase().startsWith("to")) {
                    ide.toggleEditor(lastCommandString + "\n" + "end");
                } else if (lastCommandString.toLowerCase().startsWith("edit")) {
                    String[] lastCommandElements = lastCommandString.split("\\s+", 2);
                    if (yali.env().defined(lastCommandElements[1])) {
                        ide.toggleEditor(yali.env().procedure(lastCommandElements[1]).getSource());
                    } else {
                        ide.toggleEditor(lastCommandString.replaceFirst("edit", "to") + "\n" + "end");
                    }
                } else {
                    Node ast = yali.read(lastCommandString);
                    Node result = yali.run(ast);
                    newContent += result.toString() + "\n";
                }
            } catch (NodeTypeException nte) {
                if (nte.getExpected().contains(NodeType.PROCCALL) && nte.getReceived().equals(NodeType.SYMBOL)) {
                    newContent += String.format(
                            messages.get("function_not_found"),
                            nte.getNode().token().get(0).getLexeme(),
                            nte.getReceived()
                    ) + "\n";
                } else if (nte.getExpected().contains(NodeType.PROCCALL)) {
                    newContent += String.format(
                            messages.get("redundant_argument"),
                            nte.getNode().toString(),
                            nte.getReceived()
                    ) + "\n";
                } else {
                    newContent += String.format(
                            messages.get("not_expected"),
                            nte.getNode().token().get(0).getLexeme(),
                            nte.getExpected(),
                            nte.getReceived()
                    ) + "\n";
                }
                yali.reset();
            } catch (VariableNotFoundException vnfe) {
                newContent += String.format(
                        messages.get("variable_not_found"),
                        vnfe.getName()
                ) + "\n";
                yali.reset();
            }

            newContent += "> ";

            vtf.appendText(newContent);
            newContent = "";
        }
    }
}
