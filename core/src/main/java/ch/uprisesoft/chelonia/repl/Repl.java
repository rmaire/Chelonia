/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.repl;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

/**
 *
 * @author rma
 */
public class Repl extends Window {

    private ScrollPane commandScrollPane;
    private HighlightTextArea commandArea;
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
