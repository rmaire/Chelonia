package ch.uprisesoft.chelonia;

import ch.uprisesoft.chelonia.turtle.TurtleManager;
import ch.uprisesoft.chelonia.turtle.VectorFactory;
import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;
import java.util.List;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainScreen implements Screen, InputGenerator, OutputObserver {

    // Overlay specifics
//    private Overlay gui;

//    // World GUI specifics
    private OrthographicCamera worldCamera;
    private Viewport worldViewport;

    // Yali specifics
    private final Interpreter yali;
    private final TurtleManager turtle;

    // Other stuff
    private final InputMultiplexer input;
    private final GuiToggleAdapter gta;
    private ShapeRenderer shapeRenderer;
    private Skin mainSkin;
    private int screenWidth;
    private int screenHeight;
    private final Chelonia parent;
    private Console console;

    public MainScreen(Interpreter interpreter, Chelonia game) {

        parent = game;

        yali = interpreter;
        
        yali.loadStdLib(this, this);
        
        turtle = new TurtleManager();
        turtle.registerProcedures(yali);
        
        Workspace workspace = new Workspace(yali, parent);
        workspace.registerProcedures(yali);

        input = new InputMultiplexer();
        gta = new GuiToggleAdapter();
        console = new ReplConsole(yali);
        
//        console.setDisabled(false);
        console.setVisible(true);
        console.setCommandExecutor(new CommandExecutor());
        console.setDisplayKeyID(Input.Keys.F5);
        console.setPosition(0, 0);
        console.setSizePercent(100, 30);
        console.enableSubmitButton(true);
        console.setHoverColor(new Color(1f, 1f, 1f, 0.5f));
        console.setNoHoverColor(new Color(1f, 1f, 1f, 0.5f));
//        console.getWindow().setColor(1f, 1f, 1f, 0.5f);
//        console.setHoverColor(Color.CLEAR);
    }

    @Override
    public void show() {
        updateScreenSizes();

        worldCamera = new OrthographicCamera();
        worldCamera.setToOrtho(false, screenWidth, screenHeight);
        worldCamera.position.set(0, 0, 0);
        worldViewport = new ExtendViewport(screenWidth, screenHeight, worldCamera);

        shapeRenderer = new ShapeRenderer();

        mainSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));

//        gui = new Overlay(yali, mainSkin, gta);
//        gui.init(screenWidth, screenHeight);

//        input.addProcessor(gui.getAdapter());
//        input.addProcessor(gui);
//        Gdx.input.setInputProcessor(input);
        Gdx.input.setInputProcessor(console.getWindow().getStage());
        
        console.draw();

    }

    @Override
    public void resize(int width, int height) {
        updateScreenSizes();
        worldViewport.update(screenWidth, screenHeight);
        worldCamera.setToOrtho(false, screenWidth, screenHeight);
        worldCamera.position.set(0, 0, 0);

//        gui.resize(width, height);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        shapeRenderer.setProjectionMatrix(worldCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        List<Vector2> positions = VectorFactory.fromTurtlePositionList(turtle.getTurtle().getHead());

        for (int i = 1; i < positions.size(); i++) {
            shapeRenderer.line(positions.get(i - 1), positions.get(i));
        }

        shapeRenderer.end();

//        if (gta.hasChanged()) {
//            gui.resize(screenWidth, screenHeight);
//        }
//        gui.update();
//        gui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        gui.draw();
        
        console.draw();
    }

    private void updateScreenSizes() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void dispose() {
//        gui.dispose();

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
//        gui.inform(output);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
//        Gdx.input.setInputProcessor(input);
//        gui.setKeyboardFocus(commandArea);
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }
    
    public void edit(String editorContent) {
//        gui.edit(editorContent);
        
    }
}
