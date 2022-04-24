package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

public class Chelonia extends Game implements InputGenerator, OutputObserver{

    private final Interpreter yali;
    private ReplScreen replScreen;

    Skin skin;

    public Chelonia() {
        System.out.println("NEW CHELONIA!");;

        yali = new Interpreter();

        yali.loadStdLib(this, this);

//        this.replScreen = new ReplScreen(yali, this);
//        this.editScreen = new EditScreen(yali, this);
    }

    @Override
    public void create() {
        VisUI.load();
        this.replScreen = new ReplScreen(yali, this);
        
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        setScreen(replScreen);
    }

    @Override
    public void dispose() {
        skin.dispose();
//        setScreen(basicEditor);
        VisUI.dispose();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
