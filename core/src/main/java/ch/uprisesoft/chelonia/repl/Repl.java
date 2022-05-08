/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.repl;

import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.scope.VariableNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rma
 */
public class Repl extends Window {

    private ScrollPane commandScrollPane;
    private HighlightTextArea commandArea;
    private Window commandWindow;
    private final TextFieldListener tfl;

    public Repl(String title, Skin skin, TextFieldListener tfl) {
        super(title, skin);
        this.tfl = tfl;
        initRepl();
    }

    private void initRepl() {
        commandArea = new HighlightTextArea("> ");
        commandArea.setFocusTraversal(false);
        commandArea.setTextFieldListener(tfl);
        
        commandScrollPane = commandArea.createCompatibleScrollPane();

        this.add(commandScrollPane).fill().expand();
        updateCursor();
    }

    public void updateCursor() {
        commandArea.setCursorAtTextEnd();
    }
    
    public void append(String newContent){
        commandArea.appendText(newContent);
    }

}
