package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.UnthreadedInterpreter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

public class Chelonia extends Game {

    private UnthreadedInterpreter yali;
    private IdeScreen ide;

    private Skin skin;

    @Override
    public void create() {
        VisUI.load();
        yali = new UnthreadedInterpreter();
//        skin = new Skin(Gdx.files.internal("holo/skin/light-hdpi/Holo-light-hdpi.json"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        skin.getFont("default-font").getData().setScale(1.5f, 1.5f);
        ide = new IdeScreen(yali, this, skin);
        yali.loadStdLib(ide, ide);
        
        setScreen(ide);
    }

    @Override
    public void dispose() {
        skin.dispose();
        VisUI.dispose();
    }
}
