package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.kotcrab.vis.ui.VisUI;

public class Chelonia extends Game {

    private final Interpreter yali;
    private MainScreen replScreen;
//    private EditScreen editScreen;

    Skin skin;

    public Chelonia() {
        yali = new Interpreter();
//        yali.addTracer(new YaliTracer());
    }

    @Override
    public void create() {
        this.replScreen = new MainScreen(yali, this);
//        this.editScreen = new EditScreen(yali, this);
        skin = new Skin(Gdx.files.internal("holo/skin/dark-hdpi/Holo-dark-hdpi.json"));
        setScreen(replScreen);
    }

    @Override
    public void dispose() {
        skin.dispose();
    }

    protected void switchToRepl() {
        setScreen(replScreen);
    }

    protected void edit(String editorContents) {
        replScreen.edit(editorContents);
    }
}
