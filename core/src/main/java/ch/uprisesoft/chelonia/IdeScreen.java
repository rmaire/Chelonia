package ch.uprisesoft.chelonia;

import ch.uprisesoft.chelonia.turtle.TurtleManager;
import ch.uprisesoft.chelonia.turtle.TurtlePosition;
import ch.uprisesoft.chelonia.turtle.VectorFactory;
import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import ch.uprisesoft.yali.scope.VariableNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class IdeScreen implements Screen, InputGenerator, OutputObserver {

    FileHandle baseFileHandle = Gdx.files.internal("i18n/Translation");
    Locale locale = new Locale("de", "CH");
    I18NBundle messages = I18NBundle.createBundle(baseFileHandle, locale);

//    private final Chelonia parent;
    private Stage main;

    private InputMultiplexer multiplexer;

    private boolean replCollapsed = false;
    private boolean editorCollapsed = true;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    // REPL specific members
    private static final int COMMAND_HEIGHT = 250;
    private ScrollPane commandScrollPane;
    private HighlightTextArea commandArea;
    private Window commandWindow;
    private Skin mainSkin;

    // Editor specific members
    private Window editorParentWindow;
    private VisTable editorTable;
    private VisTable buttonTable;
    private VisTextButton saveButton;
    private VisTextButton cancelButton;
    private ScrollPane editScrollPane;
    private HighlightTextArea editArea;

    // Yali specifics
    private final Interpreter yali;
    private final TurtleManager turtle;

    public IdeScreen(Interpreter interpreter, Chelonia game) {
        yali = interpreter;
        turtle = new TurtleManager();
        turtle.registerProcedures(yali);

        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        mainSkin.getFont("default-font").getData().setScale(1.33f, 1.33f);
        camera = new OrthographicCamera();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(0, 0, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sizeRepl();
        sizeEditor();

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.WHITE);

        List<TurtlePosition> positions = turtle.getTurtle().getPositionsWithHead(delta);

        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(i - 1).pendown) {
                Vector2 origin = new Vector2(positions.get(i - 1).x, positions.get(i - 1).y);
                Vector2 dest = new Vector2(positions.get(i).x, positions.get(i).y);
                shapeRenderer.setColor(positions.get(i - 1).color);
                shapeRenderer.line(origin, dest);
            }
        }

        shapeRenderer.end();

//        commandScrollPane.scrollTo(0, commandArea.getHeight() - commandArea.getCursorY(), 0, commandArea.getStyle().font.getLineHeight());
        main.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        main.draw();
    }

    @Override
    public void dispose() {
        main.dispose();
    }

    @Override
    public String request() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String requestLine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void inform(String output) {
        commandArea.appendText(output);
    }

    @Override
    public void show() {
        initRepl();
        sizeRepl();
        initEditor();
        sizeEditor();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(replAdapter);
        multiplexer.addProcessor(main);
        Gdx.input.setInputProcessor(multiplexer);
        commandArea.setCursorAtTextEnd();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(multiplexer);
        commandArea.setCursorAtTextEnd();
    }

    @Override
    public void hide() {
    }

    private void initEditor() {
        editorParentWindow = new Window("Editor", mainSkin);

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
        editorParentWindow.add(editorTable).expand().fill().padBottom(10);

        editorParentWindow.row();

        buttonTable = new VisTable();
        saveButton = new VisTextButton("Save", new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                saveEditorContent();
            }
        });
        cancelButton = new VisTextButton("Cancel", new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                toggleEditor();
            }
        });
        buttonTable.add(saveButton).padRight(10);
        buttonTable.add(cancelButton);
        editorParentWindow.add(buttonTable).bottom().right();

        editorParentWindow.setColor(1f, 1f, 1f, 0.5f);
        editArea.getStyle().font.getData().setScale(1.33f, 1.33f);
    }

    private void initRepl() {
        main = new Stage(new ScreenViewport());

        commandArea = new HighlightTextArea("> ");

        commandArea.setFocusTraversal(false);

        commandArea.setTextFieldListener(new VisTextField.TextFieldListener() {
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
                            editArea.setText(lastCommandString + "\n" + "end");
                            toggleEditor();
                        } else if (lastCommandString.toLowerCase().startsWith("edit")) {
                            String[] lastCommandElements = lastCommandString.split("\\s+", 2);
                            if (yali.env().defined(lastCommandElements[1])) {
                                editArea.setText(yali.env().procedure(lastCommandElements[1]).getSource());
                            } else {
                                editArea.setText(lastCommandString.replaceFirst("edit", "to") + "\n" + "end");
                            }
                            toggleEditor();
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
                    commandArea.setCursorAtTextEnd();
                } else if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
                    toggleRepl();
                }
            }
        });

        commandScrollPane = commandArea.createCompatibleScrollPane();

        commandWindow = new Window("Commands", mainSkin);
        commandWindow.add(commandScrollPane).fill().expand();
        commandWindow.setColor(1f, 1f, 1f, 0.5f);

        main.addActor(commandWindow);
        main.setKeyboardFocus(commandArea);
        commandArea.setCursorAtTextEnd();
        commandArea.getStyle().font.getData().setScale(1.33f, 1.33f);
    }

    private InputAdapter replAdapter = new InputAdapter() {
        boolean ctrl = false;
        boolean alt = false;
        boolean shift = false;

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT) {
                ctrl = true;
            }

            if (keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT) {
                alt = true;
            }

            if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
                shift = true;
            }

            actOnKey(keycode);
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT) {
                ctrl = false;
            }

            if (keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT) {
                alt = false;
            }

            if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
                shift = false;
            }

            return false;
        }

        private void actOnKey(int keycode) {

            if (ctrl && keycode == Keys.R) {
                toggleRepl();
            }

            if (ctrl && keycode == Keys.E) {
                toggleEditor();
            }

            if (ctrl && keycode == Keys.S && !editorCollapsed) {
                saveEditorContent();
            }
        }
    };

    private void toggleRepl() {
        if (replCollapsed) {
            main.addActor(commandWindow);
        } else {
            commandWindow.addAction(Actions.removeActor());
        }
        replCollapsed = !replCollapsed;
    }

    private void sizeRepl() {
        main.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!replCollapsed) {
            commandWindow.setBounds(0, 0, Gdx.graphics.getWidth(), COMMAND_HEIGHT);
        }
    }

    private void toggleEditor() {
        if (editorCollapsed) {
            main.addActor(editorParentWindow);
        } else {
            editorParentWindow.addAction(Actions.removeActor());
        }
        editorCollapsed = !editorCollapsed;
    }

    private void saveEditorContent() {
        System.out.println(editArea.getText());
        yali.run(yali.read(editArea.getText()));
        toggleEditor();
    }

    private void sizeEditor() {
        main.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!editorCollapsed && !replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - COMMAND_HEIGHT);
        } else if (!editorCollapsed && replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (editorCollapsed) {
            editorParentWindow.setBounds(0, 0, 0, 0);
        }

        editorParentWindow.setPosition(0, COMMAND_HEIGHT);
    }
}
