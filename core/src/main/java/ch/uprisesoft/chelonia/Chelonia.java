package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import ch.uprisesoft.yali.runtime.io.InputGenerator;
import ch.uprisesoft.yali.runtime.io.OutputObserver;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

public class Chelonia extends Game implements InputGenerator, OutputObserver{

    private Interpreter yali;
    private IdeScreen ide;

    private Skin skin;

    @Override
    public void create() {
        VisUI.load();
        yali = new Interpreter();
        
        ide = new IdeScreen(yali, this);
        yali.loadStdLib(ide, ide);
        skin = new Skin(Gdx.files.internal("commodore64ui/uiskin.json"));
        setScreen(ide);
    }

    @Override
    public void dispose() {
        skin.dispose();
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
