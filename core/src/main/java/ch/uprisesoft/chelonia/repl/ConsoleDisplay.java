/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uprisesoft.chelonia.repl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.kotcrab.vis.ui.widget.VisTable;
import com.strongjoshua.console.ConsoleContext;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogEntry;
import com.strongjoshua.console.LogLevel;

/**
 *
 * @author rma
 */
public class ConsoleDisplay {

    private Table root, logEntries;
    private TextField input;
    private TextButton submit;
    private Skin skin;
    private Array<Label> labels;
    private String fontName;
    private boolean selected = true;
    private ConsoleContext context;
    private Cell<TextButton> submitCell;
    private ScrollPane scroll;

    public ConsoleDisplay(Skin skin, Stage stage) {
        root = new VisTable();
        this.skin = skin;

        labels = new Array<Label>();

        logEntries = new VisTable();
        input = new TextField("", skin);

//			input.setTextFieldListener(new GUIConsole.FieldListener());
        submit = new TextButton("", skin);
        submit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submit();
            }
        });

        scroll = new ScrollPane(logEntries, skin);
        scroll.setFadeScrollBars(false);
        scroll.setScrollbarsOnTop(false);
        scroll.setOverscroll(false, false);
        scroll.addListener(new DragScrollListener(scroll) {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                closeContext();
                return super.scrolled(event, x, y, amount);
            }
        });

        root.add(scroll).colspan(2).expand().fill().pad(4).row();
        root.add(input).expandX().fillX().pad(4);
        submitCell = root.add(submit);
        
        stage.addActor(root);
        root.setFillParent(true);
//        root.addListener(new GUIConsole.KeyListener(input));
    }

    public void refresh() {
////        Array<LogEntry> entries = log.getLogEntries();
//        logEntries.clear();
//
//        // expand first so labels start at the bottom
//        logEntries.add().expand().fill().row();
//        int size = entries.size;
//        for (int i = 0; i < size; i++) {
//            LogEntry le = entries.get(i);
//            Label l;
//            // recycle the labels so we don't create new ones every refresh
//            if (labels.size > i) {
//                l = labels.get(i);
//            } else {
//                try {
//                    l = (Label) ClassReflection.getConstructor(labelClass, CharSequence.class, Skin.class, String.class, Color.class)
//                            .newInstance("", skin, fontName, LogLevel.DEFAULT.getColor());
//                } catch (Exception e) {
//                    try {
//                        l = (Label) ClassReflection.getConstructor(labelClass, CharSequence.class, String.class, Color.class)
//                                .newInstance("", fontName, LogLevel.DEFAULT.getColor());
//                    } catch (Exception e2) {
//                        throw new RuntimeException(
//                                "Label class does not support either (<String>, <Skin>, <String>, <Color>) or (<String>, <String>, <Color>) constructors.");
//                    }
//                }
//                l.setWrap(true);
//                labels.add(l);
//                l.addListener(new GUIConsole.LogListener(l, skin.getDrawable(tableBackground)));
//            }
//            // I'm not sure about the extra space, but it makes the label highlighting look much better with VisUI
//            l.setText(" " + le.toConsoleString());
//            l.setColor(le.getColor());
//            logEntries.add(l).expandX().fillX().top().left().row();
//        }
//        scroll.validate();
//        scroll.setScrollPercentY(1);
    }

    private void setHidden(boolean h) {
//        hidden = h;
//        if (hidden) {
//            consoleWindow.setTouchable(Touchable.disabled);
//            stage.setKeyboardFocus(null);
//            stage.setScrollFocus(null);
//        } else {
//            input.setText("");
//            consoleWindow.setTouchable(Touchable.enabled);
//            if (selected) {
//                select();
//            }
//        }
    }

    void select() {
//        selected = true;
//        if (!hidden) {
//            stage.setKeyboardFocus(input);
//            stage.setScrollFocus(scroll);
//        }
    }

    void deselect() {
//        selected = false;
//        stage.setKeyboardFocus(null);
//        stage.setScrollFocus(null);
    }

    void openContext(Label label, float x, float y) {
//        context.setLabel(label);
//        context.setPosition(x, y);
//        context.setStage(stage);
    }

    void closeContext() {
//        context.remove();
    }

    boolean submit() {
//        String s = input.getText();
//        if (s.length() == 0 || s.split(" ").length == 0) {
//            return false;
//        }
//        if (exec != null) {
//            commandHistory.store(s);
//            execCommand(s);
//        } else {
//            log("No command executor has been set. "
//                    + "Please call setCommandExecutor for this console in your code and restart.", LogLevel.ERROR);
//        }
//        input.setText("");
        return true;
    }

    void showSubmit(boolean show) {
        submit.setVisible(show);
        submitCell.size(show ? submit.getPrefWidth() : 0, show ? submit.getPrefHeight() : 0);
    }

    void setSubmitText(String text) {
        submit.setText(text);
        showSubmit(submit.isVisible());
    }
}
