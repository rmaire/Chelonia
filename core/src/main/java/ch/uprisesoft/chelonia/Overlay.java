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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Overlay extends Stage {

    private final Interpreter yali;
    private ResourceBundle messages;
    private String outputs = "";

    private final GuiToggleAdapter gta;

    private int screenWidth;
    private int screenHeight;

    private Skin mainSkin;

    private OrthographicCamera guiCamera;
    Viewport guiViewport;

    // REPL specifics
    private int replHeight;
    private ScrollPane commandScrollPane;
    private HighlightTextArea commandArea;
    private Window commandWindow;

    // Editor GUI specifics
//    private boolean editorVisible = true;
    private VisTable mainTable;
    private VisTable editorTable;
    private VisTable buttonTable;
    private VisTextButton saveButton;
    private VisTextButton cancelButton;
    private ScrollPane editScrollPane;
    private HighlightTextArea editArea;
    private Window editorWindow;
    private boolean procDefinitionMode = false;
    private StringBuilder procDefinition = new StringBuilder();
    private String newContent = "";
    private int editorHeight;
    private int editorWidth;
    private int editorOriginX;
    private int editorOriginY;

    // Tools GUI specifics
    private Window toolsWindow;
    private int toolsBarWidth;
    private int toolsBarHeight;
    private int toolsBarOriginX;
    private int toolsBarOriginY;

    public Overlay(Interpreter yali, Skin mainSkin, GuiToggleAdapter gta) {
        super();
        this.yali = yali;
        this.mainSkin = mainSkin;
        this.gta = gta;
        messages = ResourceBundle.getBundle("Translation", Locale.getDefault());
    }

    public void init(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        updateScreenSizes(width, height);

        guiCamera = new OrthographicCamera();
        guiCamera.setToOrtho(false, screenWidth, screenHeight);
        guiViewport = new ScreenViewport(guiCamera);
        super.setViewport(guiViewport);

//        initReplWindow();
//        initToolsBarWindow();
//        initEditorWindow();

//        commandWindow.setBounds(0, 0, screenWidth, replHeight);
//        toolsWindow.setBounds(toolsBarOriginX, toolsBarOriginY, toolsBarWidth, toolsBarHeight);
//        editorWindow.setBounds(editorOriginX, editorOriginY, editorWidth, editorHeight);

//        commandWindow.debug();
//        editorWindow.debug();
//        toolsWindow.debug();
    }

    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        updateScreenSizes(width, height);

        guiCamera.setToOrtho(false, screenWidth, screenHeight);
        guiViewport.update(screenWidth, screenHeight);

//        commandWindow.setBounds(0, 0, screenWidth, replHeight);
//        toolsWindow.setBounds(toolsBarOriginX, toolsBarOriginY, toolsBarWidth, toolsBarHeight);
//        editorWindow.setBounds(editorOriginX, editorOriginY, editorWidth, editorHeight);
    }

    public void update() {
        guiCamera.update();
    }

    private void initReplWindow() {

        commandArea = new HighlightTextArea("> ");

        commandArea.setFocusTraversal(false);
        commandArea.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField vtf, char c) {
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    String areaContent = vtf.getText();
                    List<String> commandLines = Arrays.asList(areaContent.split("\n"));
                    List<String> commands = commandLines.stream()
                            .filter(command -> command.startsWith("> ") || command.startsWith(": "))
                            .map(command -> command.replace("> ", ""))
                            .map(command -> command.replace(": ", ""))
                            .collect(Collectors.toList());

                    String lastCommandString = commands.get(commands.size() - 1);

                    if (!procDefinitionMode && lastCommandString.toLowerCase().startsWith("to")) {
                        procDefinitionMode = true;
                        procDefinition = new StringBuilder();
                        procDefinition.append(lastCommandString).append("\n");
                    } else if (procDefinitionMode && lastCommandString.toLowerCase().startsWith("end")) {
                        procDefinition.append(lastCommandString).append("\n");
                        Node result = Node.none();
                        try {
                            result = yali.run(yali.read(procDefinition.toString()));
                        } catch (Exception ex) {
                            // TODO
                            Logger.getLogger(Overlay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        newContent += result.toString() + "\n";
                        procDefinitionMode = false;
                    } else if (procDefinitionMode && !lastCommandString.toLowerCase().startsWith("end")) {
                        procDefinition.append(lastCommandString).append("\n");
                    } else {
                        Node result = Node.none();
                        try {
                            result = yali.run(yali.read(lastCommandString));
                        } catch (NodeTypeException nte) {
                            if (nte.getExpected().contains(NodeType.PROCCALL) && nte.getReceived().equals(NodeType.SYMBOL)) {
                                newContent += String.format(
                                        messages.getString("function_not_found"),
                                        nte.getNode().getToken().getLexeme(),
                                        nte.getReceived()
                                ) + "\n";
                            } else if (nte.getExpected().contains(NodeType.PROCCALL)) {
                                newContent += String.format(
                                        messages.getString("redundant_argument"),
                                        nte.getNode().getToken().getLexeme(),
                                        nte.getReceived()
                                ) + "\n";

                            } else {
                                newContent += String.format(
                                        messages.getString("not_expected"),
                                        nte.getNode().toString(),
                                        nte.getExpected(),
                                        nte.getReceived()
                                ) + "\n";

                            }
                        } catch (VariableNotFoundException vnfe) {
                            newContent += String.format(
                                    messages.getString("variable_not_found"),
                                    vnfe.getName()
                            ) + "\n";
                        }

//                        } catch (Exception ex) {
//                            // TODO
//                            newContent += ex.toString() + "\n";
//                            Logger.getLogger(Overlay.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        newContent += result.toString() + "\n";
                    }

                    if (!procDefinitionMode) {
                        newContent += "> ";
                    } else {
                        newContent += ": ";
                    }

                    if(!outputs.isEmpty()) {
                        newContent = outputs + newContent;
                        outputs = "";
                    }
                    
                    vtf.appendText(newContent);
                    newContent = "";
                }
            }

        });

        commandScrollPane = commandArea.createCompatibleScrollPane();

        commandWindow = new Window("Commands", mainSkin);
        commandWindow.add(commandScrollPane).fill().expand();
        commandWindow.setColor(1f, 1f, 1f, 0.5f);

        this.addActor(commandWindow);
        this.setKeyboardFocus(commandArea);
    }

    private void initEditorWindow() {
        mainTable = new VisTable();

        editArea = new HighlightTextArea("");

        editArea.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
            }
        });

        editArea.setFocusTraversal(false);

        editorTable = new VisTable();

        editScrollPane = editArea.createCompatibleScrollPane();
        editorTable.add(editScrollPane).expand().fill();
        mainTable.add(editorTable).expand().fill().padBottom(10);

        mainTable.row();

        buttonTable = new VisTable();
        saveButton = new VisTextButton("Save", new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                save(editArea.getText());
            }
        });
        cancelButton = new VisTextButton("Cancel", new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                cancel();
            }
        });
        buttonTable.add(saveButton).padRight(10);
        buttonTable.add(new VisTextButton("Cancel"));
        mainTable.add(buttonTable).bottom().right();

//        mainTable.setFillParent(true);
        mainTable.pad(5);

        editorWindow = new Window("Editor", mainSkin);
        editorWindow.add(mainTable).fill().expand();
        editorWindow.setColor(1f, 1f, 1f, 0.5f);

        this.addActor(editorWindow);

//        editor.setKeyboardFocus(editArea);
//        editorWindow.debug();
//        editorTable.debug();
//        mainTable.debug();
//        buttonTable.debug();
//        editScrollPane.debug();
    }

    private void initToolsBarWindow() {

        toolsWindow = new Window("Tools", mainSkin);
        toolsWindow.setColor(1f, 1f, 1f, 0.5f);

        this.addActor(toolsWindow);
    }

    private void updateScreenSizes(int width, int height) {
        screenWidth = width;
        screenHeight = height;

//        replHeight = gta.getReplHeight();

//        if (gta.isEditorVisible()) {
//            editorWidth = screenWidth - gta.getToolsBarWidth();
//            editorHeight = screenHeight - gta.getReplHeight();
//            editorOriginX = 0;
//            editorOriginY = screenHeight - gta.getReplHeight();
//        } else {
//            editorWidth = 0;
//            editorHeight = 0;
//            editorOriginX = 0;
//            editorOriginY = 0;
//        }

//        toolsBarWidth = gta.getToolsBarWidth();
//        toolsBarHeight = screenHeight - gta.getReplHeight();
//        toolsBarOriginX = screenWidth - gta.getToolsBarWidth();
//        toolsBarOriginY = gta.getReplHeight();

    }

    public InputAdapter getAdapter() {
        return gta;
    }

    public void setText(String editorContents) {
        editArea.setText(editorContents);
    }

    public void edit(String editorContents) {
        setText(editorContents);
//        editorVisible = true;
        gta.setEditorVisible(true);
        resize(screenWidth, screenHeight);
    }

    protected void save(String editorContents) {
        try {
            yali.run(yali.read(editorContents));
        } catch (Exception ex) {
            // TODO
            Logger.getLogger(Overlay.class.getName()).log(Level.SEVERE, null, ex);
        }
        cancel();
    }
    
    public void inform(String output) {
        outputs += output + "\n"; 
    }

    protected void cancel() {
//        parent.switchToRepl();
    }

}
