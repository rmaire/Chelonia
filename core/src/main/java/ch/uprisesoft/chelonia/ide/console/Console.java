package ch.uprisesoft.chelonia.ide.console;

import ch.uprisesoft.yali.ast.node.Node;
import ch.uprisesoft.yali.ast.node.NodeType;
import ch.uprisesoft.yali.exception.NodeTypeException;
import ch.uprisesoft.yali.runtime.interpreter.UnthreadedInterpreter;
import ch.uprisesoft.yali.scope.VariableNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.Locale;

public final class Console {

    private final UnthreadedInterpreter yali;

    private final FileHandle baseFileHandle = Gdx.files.internal("i18n/Translation");
    private final Locale locale = new Locale("de", "CH");
    private final I18NBundle messages = I18NBundle.createBundle(baseFileHandle, locale);

    private final Log log;

    private final ConsoleDisplay display;
    private boolean usesMultiplexer;
    private InputProcessor appInput;
    private InputMultiplexer multiplexer;
    private final Stage stage;
    private final CommandHistory commandHistory;
    private final Window consoleWindow;
    private final Vector3 stageCoords = new Vector3();
    private ScrollPane scroll;

    private final String tableBackground = "default-rect-pad";

    public Console(Skin skin, UnthreadedInterpreter yali, Stage stage) {

        this.yali = yali;

        this.log = new Log();

        this.stage = stage;
        display = new ConsoleDisplay(skin);
        commandHistory = new CommandHistory();

        resetInputProcessing();

        display.root.pad(4);
        display.root.padTop(22);
        display.root.setFillParent(true);
        display.showSubmit(false);

        consoleWindow = new Window("Commands", skin);
        consoleWindow.setMovable(true);
        consoleWindow.setResizable(true);
        consoleWindow.setKeepWithinStage(true);
        consoleWindow.addActor(display.root);
        consoleWindow.setTouchable(Touchable.disabled);


        stage.addActor(consoleWindow);
        stage.setKeyboardFocus(display.root);
        display.showSubmit(true);
        display.setVisible();
    }

    public void clear() {
        log.getLogEntries().clear();
        display.refresh();
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

    public void setPositionPercent(float xPosPct, float yPosPct) {
        if (xPosPct > 100 || yPosPct > 100) {
            throw new IllegalArgumentException("Error: The console would be drawn outside of the screen.");
        }
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        consoleWindow.setPosition(w * xPosPct / 100.0f, h * yPosPct / 100.0f);
    }

    public void resetInputProcessing() {
        usesMultiplexer = true;
        appInput = Gdx.input.getInputProcessor();
        if (appInput != null) {
            multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(stage);
            multiplexer.addProcessor(appInput);
            Gdx.input.setInputProcessor(multiplexer);
        } else {
            Gdx.input.setInputProcessor(stage);
        }
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    public void refresh() {
        this.refresh(false);
    }

    public void refresh(boolean retain) {
//        float oldWPct = 0, oldHPct = 0, oldXPosPct = 0, oldYPosPct = 0;
//        if (retain) {
//            oldWPct = consoleWindow.getWidth() / stage.getWidth() * 100;
//            oldHPct = consoleWindow.getHeight() / stage.getHeight() * 100;
//            oldXPosPct = consoleWindow.getX() / stage.getWidth() * 100;
//            oldYPosPct = consoleWindow.getY() / stage.getHeight() * 100;
//        }
        int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
        stage.getViewport().setWorldSize(width, height);
        stage.getViewport().update(width, height, true);
    }

    public void log(String msg, LogLevel level) {
        log.addEntry(msg, level);
        display.refresh();
    }

    public boolean hitsConsole(float screenX, float screenY) {

        stage.getCamera().unproject(stageCoords.set(screenX, screenY, 0));
        return stage.hit(stageCoords.x, stageCoords.y, true) != null;
    }

    public void dispose() {
        if (usesMultiplexer && appInput != null) {
            Gdx.input.setInputProcessor(appInput);
        }
        stage.dispose();
    }

    public void setTitle(String title) {
        consoleWindow.getTitleLabel().setText(title);
    }

    public void setSubmitText(String text) {
        display.setSubmitText(text);
    }

    public Window getWindow() {
        return this.consoleWindow;
    }

    public void execCommand(String command) {
        try {
            if (command.trim().equals("")) {
//                } else if (command.toLowerCase().startsWith("to")) {
////                    ide.toggleEditor(lastCommandString + "\n" + "end");
//                } else if (command.toLowerCase().startsWith("edit")) {
//                    String[] lastCommandElements = command.split("\\s+", 2);
//                    if (yali.env().defined(lastCommandElements[1])) {
//                        ide.toggleEditor(yali.env().procedure(lastCommandElements[1]).getSource());
//                    } else {
//                        ide.toggleEditor(command.replaceFirst("edit", "to") + "\n" + "end");
//                    }
            } else {
                Node ast = yali.read(command);
                Node result = yali.run(ast);
                
                log(command, LogLevel.COMMAND);
                log(result.toString(), LogLevel.SUCCESS);
            }
        } catch (NodeTypeException nte) {
            if (nte.getExpected().contains(NodeType.PROCCALL) && nte.getReceived().equals(NodeType.SYMBOL)) {
                log(String.format(
                        messages.get("function_not_found"),
                        nte.getNode().token().get(0).getLexeme(),
                        nte.getReceived()),
                        LogLevel.ERROR);
            } else if (nte.getExpected().contains(NodeType.PROCCALL)) {
                log(String.format(
                        messages.get("redundant_argument"),
                        nte.getNode().toString(),
                        nte.getReceived()),
                        LogLevel.ERROR);
            } else {
                yali.reset();
                log(String.format(
                        messages.get("not_expected"),
                        nte.getNode().token().get(0).getLexeme(),
                        nte.getExpected()),
                        LogLevel.ERROR);
            }
        } catch (VariableNotFoundException vnfe) {
            yali.reset();
            log(String.format(
                    messages.get("variable_not_found"),
                    vnfe.getName()),
                    LogLevel.ERROR);
        }
    }

//    private void refreshWindowColor() {
//        consoleWindow.setColor(hasHover ? hoverColor : noHoverColor);
//    }

    private class ConsoleDisplay {

        private Table root, logEntries;
        private TextField input;
        private TextButton submit;
        private Skin skin;
        private Array<Label> labels;
//        private String fontName;
        private boolean selected = true;
        private ConsoleContext context;
        private Cell<TextButton> submitCell;

        ConsoleDisplay(Skin skin) {

            root = new Table(skin);
            this.skin = skin;

            labels = new Array<>();

            logEntries = new Table(skin);

            input = new TextField("", skin);

            submit = new TextButton("Los!", skin);
            submit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    submit();
                }
            });

            scroll = new ScrollPane(logEntries, skin);
            scroll.setFadeScrollBars(false);
            scroll.setScrollbarsOnTop(false);
            scroll.setOverscroll(false, false);
            scroll.addListener(new DragScrollListener(scroll) {
                @Override
                public boolean scrolled(InputEvent event, float x, float y, int amount) {
                    closeContext();
                    return super.scrolled(event, x, y, amount);
                }
            });

            root.add(scroll).colspan(2).expand().fill().pad(4).row();
            root.add(input).expandX().fillX().pad(4);
            submitCell = root.add(submit);
            root.addListener(new KeyListener(input));
            context = new ConsoleContext(skin);
        }

        void refresh() {
            Array<LogEntry> entries = log.getLogEntries();
            logEntries.clear();

            // expand first so labels start at the bottom
            logEntries.add().expand().fill().row();
            int size = entries.size;
            for (int i = 0; i < size; i++) {
                LogEntry le = entries.get(i);
                Label l;
                // recycle the labels so we don't create new ones every refresh
                if (labels.size > i) {
                    l = labels.get(i);
                } else {
                    l = new Label("", skin, "default-font", LogLevel.DEFAULT.getColor());
                    l.setColor(LogLevel.DEFAULT.getColor());
                    l.setWrap(true);
                    labels.add(l);
                    l.addListener(new LogListener(l, skin.getDrawable(tableBackground)));
                }
                l.setText(" " + le.toConsoleString());
                l.setColor(le.getColor());
                logEntries.add(l).expandX().fillX().top().left().row();
            }
            scroll.validate();
            scroll.setScrollPercentY(1);
        }

        private void setVisible() {
            input.setText("");
            consoleWindow.setTouchable(Touchable.enabled);
            if (selected) {
                select();
            }
        }

        void select() {
            selected = true;
            stage.setKeyboardFocus(input);
            stage.setScrollFocus(scroll);
        }

        void deselect() {
            selected = false;
            stage.setKeyboardFocus(null);
            stage.setScrollFocus(null);
        }

        void openContext(Label label, float x, float y) {
            context.setLabel(label);
            context.setPosition(x, y);
            context.setStage(stage);
        }

        void closeContext() {
            context.remove();
        }

        boolean submit() {
            String s = input.getText();
            if (s.length() == 0 || s.split(" ").length == 0) {
                return false;
            }

            commandHistory.store(s);
            execCommand(s);
            input.setText("");
            return true;
        }

        void showSubmit(boolean show) {
            submit.setVisible(show);
            submitCell.size(show ? submit.getPrefWidth() : 0, show ? submit.getPrefHeight() : 0);
        }

        void setSubmitText(String text) {
            submit.setText(text);
            showSubmit(submit.isVisible());
        }
    }

    private class KeyListener extends InputListener {

        private TextField input;

        protected KeyListener(TextField tf) {
            input = tf;
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Keys.ENTER) {
                commandHistory.getNextCommand(); // Makes up arrow key repeat the same command after pressing enter
                return display.submit();
            } else if (keycode == Keys.UP) {
                input.setText(commandHistory.getPreviousCommand());
                input.setCursorPosition(input.getText().length());
                return true;
            } else if (keycode == Keys.DOWN) {
                input.setText(commandHistory.getNextCommand());
                input.setCursorPosition(input.getText().length());
                return true;
            }
            return false;
        }
    }

    private class LogListener extends ClickListener {

        private final Label label;
        private final Drawable highlighted;

        LogListener(Label label, Drawable highlighted) {
            this.label = label;
            this.highlighted = highlighted;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            Vector2 pos = label.localToStageCoordinates(new Vector2(x, y));
            display.openContext(label, pos.x, pos.y);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (pointer != -1) {
                return;
            }
//            System.out.println("BOOM for " + label.toString());
            label.getStyle().background = highlighted;
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
}
