package ch.uprisesoft.chelonia;

import ch.uprisesoft.chelonia.repl.Repl;
import ch.uprisesoft.chelonia.repl.console.GUIConsole;
import ch.uprisesoft.chelonia.turtle.TurtleManager;
import ch.uprisesoft.chelonia.turtle.TurtlePosition;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import java.util.List;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class IdeScreen implements Screen, InputGenerator, OutputObserver, Ide {

//    FileHandle baseFileHandle = Gdx.files.internal("i18n/Translation");
//    Locale locale = new Locale("de", "CH");
//    I18NBundle messages = I18NBundle.createBundle(baseFileHandle, locale);

    private GUIConsole guic;

    private Stage main;

    private InputMultiplexer multiplexer;

    private boolean replCollapsed = false;
    private boolean editorCollapsed = true;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Skin mainSkin;

    // REPL specific members
    private static final int REPL_HEIGHT = 250;
//    private ScrollPane commandScrollPane;
//    private HighlightTextArea commandArea;
//    private Window commandWindow;
    private Repl repl;

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
    private int ticks = 0;

    public IdeScreen(Interpreter interpreter, Chelonia game) {
        yali = interpreter;
        turtle = new TurtleManager();
        turtle.registerProcedures(yali);

        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        mainSkin.getFont("default-font").getData().setScale(1.5f, 1.5f);
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
        repl.append(output);
    }

    @Override
    public void show() {
        main = new Stage(new ScreenViewport());
        
        guic = new GUIConsole(mainSkin, yali, main);
        guic.setPosition(0, 0);
        guic.setSize(Gdx.graphics.getWidth(), REPL_HEIGHT);
        guic.setTitle("Commands");
        guic.enableSubmitButton(true);
        guic.getWindow().setColor(1f, 1f, 1f, 0.5f);
        initEditor();
        sizeEditor();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(replAdapter);
        multiplexer.addProcessor(main);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(multiplexer);
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

    @Override
    public void toggleRepl() {
        if (replCollapsed) {
            main.addActor(guic.getWindow());
        } else {
            guic.getWindow().addAction(Actions.removeActor());
        }
        replCollapsed = !replCollapsed;
    }

    private void sizeRepl() {
        main.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!replCollapsed) {
            guic.setSize(Gdx.graphics.getWidth(), REPL_HEIGHT);
        }
    }

    @Override
    public void toggleEditor() {
        if (editorCollapsed) {
            main.addActor(editorParentWindow);
        } else {
            editorParentWindow.addAction(Actions.removeActor());
        }
        editorCollapsed = !editorCollapsed;
    }

    @Override
    public void toggleEditor(String content) {
        editArea.setText(content);
        toggleEditor();
    }

    private void saveEditorContent() {
        yali.run(yali.read(editArea.getText()));
        toggleEditor();
    }

    private void sizeEditor() {
        main.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (!editorCollapsed && !replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - REPL_HEIGHT);
        } else if (!editorCollapsed && replCollapsed) {
            editorParentWindow.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (editorCollapsed) {
            editorParentWindow.setBounds(0, 0, 0, 0);
        }
        editorParentWindow.setPosition(0, REPL_HEIGHT);
    }
}
