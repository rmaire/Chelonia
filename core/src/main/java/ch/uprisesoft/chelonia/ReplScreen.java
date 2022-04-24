package ch.uprisesoft.chelonia;

import ch.uprisesoft.chelonia.turtle.TurtleManager;
import ch.uprisesoft.chelonia.turtle.VectorFactory;
import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class ReplScreen implements Screen, InputGenerator, OutputObserver {

    private final Chelonia parent;
    private Stage repl;

    private InputMultiplexer multiplexer;

    private boolean replCollapsed = false;
    private boolean editorCollapsed = true;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private boolean procDefinitionMode = false;
    private StringBuilder procDefinition = new StringBuilder();
    private String newContent = "";

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

    public ReplScreen(Interpreter interpreter, Chelonia game) {
        System.out.println("New ReplScreen");

        parent = game;

        yali = interpreter;
        yali.loadStdLib(this, this);
        turtle = new TurtleManager();
        turtle.registerProcedures(yali);

        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        camera = new OrthographicCamera();

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(0, 0, 0);
    }

    private void sizeRepl() {
        repl.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!replCollapsed) {
            commandWindow.setBounds(0, 0, Gdx.graphics.getWidth(), COMMAND_HEIGHT);
        }
    }

    private void sizeEditor() {
        repl.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!editorCollapsed && !replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - COMMAND_HEIGHT);
        } else if (!editorCollapsed && replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (editorCollapsed) {
            editorParentWindow.setBounds(0, 0, 0, 0);
        }

        editorParentWindow.setPosition(0, COMMAND_HEIGHT);
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
        shapeRenderer.setColor(Color.WHITE);

//        List<Vector2> positions = VectorFactory.fromTurtlePositionList(turtle.getTurtle().getHead());
        List<Vector2> positions = VectorFactory.fromTurtlePositionList(turtle.getTurtle().getPositions(delta));

        for (int i = 1; i < positions.size(); i++) {
            shapeRenderer.line(positions.get(i - 1), positions.get(i));
        }

        shapeRenderer.end();

//        commandScrollPane.scrollTo(0, commandArea.getHeight() - commandArea.getCursorY(), 0, commandArea.getStyle().font.getLineHeight());
        repl.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        repl.draw();
    }

    @Override
    public void dispose() {
        System.out.println("Dispose called");
        repl.dispose();

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
        inform(output);
    }

    @Override
    public void show() {
        System.out.println("Show called");

        initRepl();
        sizeRepl();
        initEditor();
        sizeEditor();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(replAdapter);
        multiplexer.addProcessor(repl);
        Gdx.input.setInputProcessor(multiplexer);
        commandArea.setCursorAtTextEnd();
    }

    @Override
    public void pause() {
        System.out.println("pause called");
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        System.out.println("Resume called");
        Gdx.input.setInputProcessor(multiplexer);
        commandArea.setCursorAtTextEnd();
    }

    @Override
    public void hide() {
        System.out.println("Hide called");
        // This method is called when another screen replaces this one.
    }

    private void initEditor() {
        editorParentWindow = new Window("Editor", mainSkin);

        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
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
//                save(editArea.getText());
            }
        });
        cancelButton = new VisTextButton("Cancel", new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
//                cancel();
            }
        });
        buttonTable.add(saveButton).padRight(10);
        buttonTable.add(cancelButton);
        editorParentWindow.add(buttonTable).bottom().right();

//        editorParentTable.setFillParent(true);
//        editorParentTable.pad(5);
        editorParentWindow.setColor(1f, 1f, 1f, 0.5f);
//        repl.addActor(editorParentWindow);
    }

    private void initRepl() {
        repl = new Stage(new ScreenViewport());

//        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
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

                    if (lastCommandString.trim().equals("")) {
                    } else if (!procDefinitionMode && lastCommandString.toLowerCase().startsWith("to")) {
                        procDefinitionMode = true;
                        procDefinition.append(lastCommandString).append("\n");
                    } else if (procDefinitionMode && lastCommandString.toLowerCase().startsWith("end")) {
                        procDefinition.append(lastCommandString).append("\n");
                        Node ast = yali.read(procDefinition.toString());
                        Node result = yali.run(ast);
                        newContent += result.toString() + "\n";
                        procDefinitionMode = false;
                    } else if (procDefinitionMode && !lastCommandString.toLowerCase().startsWith("end")) {
                        procDefinition.append(lastCommandString).append("\n");
                    } else {
                        Node ast = yali.read(lastCommandString);
                        Node result = yali.run(ast);
                        newContent += result.toString() + "\n";
                    }

                    if (!procDefinitionMode) {
                        newContent += "> ";
                    } else {
                        newContent += ": ";
                    }

                    vtf.appendText(newContent);
                    newContent = "";
                    commandArea.setCursorAtTextEnd();
                } else if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
                    System.out.println("YEEEES");
                    replCollapsed = !replCollapsed;
//                    sizeRepl();
//                    sizeEditor();
                }
            }
        });

        commandScrollPane = commandArea.createCompatibleScrollPane();

        commandWindow = new Window("Commands", mainSkin);
        commandWindow.add(commandScrollPane).fill().expand();
        commandWindow.setColor(1f, 1f, 1f, 0.5f);

        repl.addActor(commandWindow);
        repl.setKeyboardFocus(commandArea);
        commandArea.setCursorAtTextEnd();
    }

    private InputAdapter replAdapter = new InputAdapter() {
        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            actOnKey(keycode);
            return false;
        }

        private void actOnKey(int keycode) {
            if (keycode == Keys.F1) {
                if (replCollapsed) {
                    repl.addActor(commandWindow);
                } else {
                    commandWindow.addAction(Actions.removeActor());
                }
                replCollapsed = !replCollapsed;
            }

            if (keycode == Keys.F2) {

                if (editorCollapsed) {
                    repl.addActor(editorParentWindow);
                } else {
                    editorParentWindow.addAction(Actions.removeActor());
                }
                editorCollapsed = !editorCollapsed;

                System.out.println(editorCollapsed);
            }
        }
    };
}
