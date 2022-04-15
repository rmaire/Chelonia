package ch.uprisesoft.chelonia;

import ch.uprisesoft.yali.runtime.interpreter.Interpreter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class EditScreen implements Screen {

    private final Chelonia parent;
    private Stage editor;

    // Yali specifics
    private final Interpreter yali;

    private VisTable editorParentTable;
    private VisTable editorTable;
    private VisTable buttonTable;

    private VisTextButton saveButton;
    private VisTextButton cancelButton;

    private ScrollPane editScrollPane;
    private HighlightTextArea editArea;
    private Skin mainSkin;

    public EditScreen(Interpreter interpreter, Chelonia game) {

        this.parent = game;

        this.yali = interpreter;
//        this.editor = new Stage(new ScreenViewport());
        initEditorStage();
    }

    @Override
    public void show() {
//        VisUI.load();
//        editor = new Editor(yali, this);
        Gdx.input.setInputProcessor(editor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        editor.draw();
    }

    @Override
    public void resize(int width, int height) {
        editScrollPane.setSize(width, height);
        editor.getViewport().update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(editor);
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        editor.dispose();
//        VisUI.dispose();
    }

    public void setText(String editorContents) {
        editArea.setText(editorContents);
    }

    protected void save(String editorContents) {
//        yali.parse(editorContents);
//        yali.interpret();
        cancel();
    }

    protected void cancel() {
        parent.switchToRepl();
    }

    private void initEditorStage() {
        editor = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(editor);

        editorParentTable = new VisTable();

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
        editorParentTable.add(editorTable).expand().fill().padBottom(10);

        editorParentTable.row();

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
        editorParentTable.add(buttonTable).bottom().right();

        editorParentTable.setFillParent(true);
        editorParentTable.pad(5);
        editor.addActor(editorParentTable);

        editor.setKeyboardFocus(editArea);

    }
}
