/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.uprisesoft.chelonia.ide.console2;

import ch.uprisesoft.chelonia.ide.console.LogLevel;
import ch.uprisesoft.yali.runtime.interpreter.UnthreadedInterpreter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author roman
 */
public class Console2 {

    private final Stage stage;
    private final Window consoleWindow;
    private final Vector3 stageCoords = new Vector3();
    private ScrollPane scroll;

    // Display stuff
    private Table root, logEntries;
    private TextField input;

    public Console2(Skin skin, UnthreadedInterpreter yali, Stage stage) {
        this.stage = stage;

        consoleWindow = new Window("Commands", skin);
        consoleWindow.setMovable(true);
        consoleWindow.setResizable(true);
        consoleWindow.setKeepWithinStage(true);
//        consoleWindow.addActor(display.root);
        consoleWindow.setTouchable(Touchable.disabled);

        stage.addActor(consoleWindow);

        root = new Table(skin);
        logEntries = new Table(skin);
        input = new TextField("", skin);
        input.setCursorPosition(2);
        input.setFillParent(true);
        input.getStyle().background = null;

        Label promptLabel = new Label("> ", skin);

        HorizontalGroup prompt = new HorizontalGroup();
        prompt.addActor(promptLabel);
        prompt.addActor(input);

        scroll = new ScrollPane(logEntries, skin);
        scroll.setFadeScrollBars(false);
        scroll.setScrollbarsOnTop(false);
        scroll.setOverscroll(false, false);
        scroll.addListener(new DragScrollListener(scroll) {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
//                    closeContext();
                return super.scrolled(event, x, y, amount);
            }
        });
        root.add(scroll).colspan(2).expand().fill().pad(4);
        logEntries.add(prompt).expandX().fillX().top().left().row();

        Label l = new Label("", skin, "default-font", new Color(Color.CYAN));
        l.setWrap(true);
        l.setText("Test");
        l.addListener(new ReplLogListener(l, new Color(0.5f, 0.5f, 0.5f, 0.5f)));
        logEntries.add(l).expandX().fillX().top().left().row();

        for (int i = 0; i < 10; i++) {
            Label x = new Label("", skin, "default-font", new Color(Color.CYAN));
            x.setWrap(true);
            x.setText("Test");
            x.addListener(new ReplLogListener(x, Color.GREEN));
            logEntries.add(x).expandX().fillX().top().left().row();
        }

//        Label fill = new Label("", skin, "default-font", new Color(Color.CYAN));
//        fill.setWrap(true);
//        logEntries.add(fill).expand().fill();
        root.pad(4);
        root.padTop(22);
        root.setFillParent(true);

        consoleWindow.addActor(root);

        scroll.validate();
        scroll.setScrollPercentY(1);

        consoleWindow.setTouchable(Touchable.enabled);

        stage.setKeyboardFocus(input);

        root.debugAll();

//        stage.setKeyboardFocus(display.root);
    }

    public void setSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Pixel size must be greater than 0.");
        }
        consoleWindow.setSize(width, height);
    }

    public void setPosition(int x, int y) {
        consoleWindow.setPosition(x, y);
    }

    public void setTitle(String title) {
        consoleWindow.getTitleLabel().setText(title);
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    public void refresh() {
        this.refresh(false);
    }

    public void refresh(boolean retain) {
        int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
        stage.getViewport().setWorldSize(width, height);
        stage.getViewport().update(width, height, true);
    }
}

class ReplLogListener extends ClickListener {

    private final Label label;
    private final Color color;
//    private final Drawable highlighted;

    ReplLogListener(Label label, Color color) {
        this.label = label;
        this.color = color;
    }

//        @Override
//        public void clicked(InputEvent event, float x, float y) {
//            Vector2 pos = label.localToStageCoordinates(new Vector2(x, y));
//            display.openContext(label, pos.x, pos.y);
//        }
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer != -1) {
            return;
        }

        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        label.getStyle().background = textureRegionDrawableBg;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer != -1) {
            return;
        }
//            System.out.println("POOF for " + label.toString());
        label.getStyle().background = null;
    }
}
