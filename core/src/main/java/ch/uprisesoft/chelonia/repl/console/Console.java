package ch.uprisesoft.chelonia.repl.console;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import ch.uprisesoft.chelonia.repl.console.annotation.HiddenCommand;

public interface Console {

	int UNLIMITED_ENTRIES = -1;

	/**
	 * @param numEntries maximum number of entries the console will hold.
	 */
	void setMaxEntries (int numEntries);

	/**
	 * Clears all log entries.
	 */
	void clear ();

	/**
	 * Set size of the console in pixels
	 *
	 * @param width  width of the console in pixels
	 * @param height height of the console in pixels
	 */
	void setSize (int width, int height);

	/**
	 * Set size of the console as a percent of screen size
	 *
	 * @param wPct width of the console as a percent of screen width
	 * @param hPct height of the console as a percent of screen height
	 */
	void setSizePercent (float wPct, float hPct);

	/**
	 * Set position of the lower left corner of the console
	 *
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	void setPosition (int x, int y);

	/**
	 * Set position of the lower left corner of the console as a percent of screen size
	 *
	 * @param xPosPct Percentage for the x position relative to the screen size.
	 * @param yPosPct Percentage for the y position relative to the screen size.
	 */
	void setPositionPercent (float xPosPct, float yPosPct);

	/**
	 * Call this method if you changed the input processor while this console was active.
	 */
	void resetInputProcessing ();

	/**
	 * @return {@link InputProcessor} for this {@link Console}
	 */
	InputProcessor getInputProcessor ();

	/**
	 * Draws the console.
	 */
	void draw ();

	/**
	 * Calls {@link Console#refresh(boolean)} with true.
	 */
	void refresh ();

	/**
	 * Refreshes the console's stage. Use if the app's window size was changed.
	 *
	 * @param retain True if you want position and size percentages to be kept.
	 */
	void refresh (boolean retain);

	/**
	 * Logs a new entry to the console.
	 *
	 * @param msg   The message to be logged.
	 * @param level The {@link LogLevel} of the log entry.
	 * @see LogLevel
	 */
	void log (String msg, LogLevel level);

	/**
	 * Logs a new entry to the console using {@link LogLevel#DEFAULT}.
	 *
	 * @param msg The message to be logged.
	 * @see LogLevel
	 * @see Console#log(String, LogLevel)
	 */
	void log (String msg);


	/**
	 * @return If the console is disabled.
	 * @see Console#setDisabled(boolean)
	 */
	boolean isDisabled ();

	/**
	 * @param disabled True if the console should be disabled (unable to be shown or used). False otherwise.
	 */
	void setDisabled (boolean disabled);

	/**
	 * Gets the console's display key. If the console is enabled, the console will be shown upon this key being pressed.<br>
	 * Default key is <b>`</b> a.k.a. '<b>backtick</b>'.
	 *
	 * @return the keyID
	 */
	int getDisplayKeyID ();

	/**
	 * @param code The new key's ID. Cannot be {@link Keys#ENTER}.
	 * @see Console#getDisplayKeyID()
	 */
	void setDisplayKeyID (int code);


	/**
	 * Executes the specified command via the set {@link CommandExecutor}.
	 *
	 * @param command The command to execute.
	 */
	void execCommand (String command);

	/**
	 * Returns if the given screen coordinates hit the console.
	 *
	 * @param screenX X coordinate on the screen.
	 * @param screenY Y coordinate on the screen.
	 * @return True, if the console was hit.
	 */
	boolean hitsConsole (float screenX, float screenY);

	/**
	 * Resets the {@link InputProcessor} to the one that was the default before this console object was created.
	 */
	void dispose ();

	/**
	 * @return If console is visible
	 */
	boolean isVisible ();

	/**
	 * Hides or shows the console. Same effect as pushing the display button, except it doesn't toggle.
	 *
	 * @param visible If the console should be visible.
	 */
	void setVisible (boolean visible);

	/**
	 * Sets the executeHiddenCommands field
	 *
	 * @param enabled - if true, commands annotated with {@link HiddenCommand} can be executed
	 */
	void setExecuteHiddenCommands (boolean enabled);

	/**
	 * Sets the executeHiddenCommands field
	 *
	 * @param enabled - if true, commands annotated with {@link HiddenCommand} show when printCommands (help) is executed
	 */
	void setDisplayHiddenCommands (boolean enabled);

	/**
	 * @return If hidden commands can be executed
	 */
	boolean isExecuteHiddenCommandsEnabled ();

	/**
	 * @return If hidden commands can be displayed
	 */
	boolean isDisplayHiddenCommandsEnabled ();

	/**
	 * @param enabled If the command execution stack traces should be printed to the console.
	 */
	void setConsoleStackTrace (boolean enabled);

	/**
	 * Works only for GUIConsole. Selects the text field.
	 */
	void select ();

	/**
	 * Works only for a GUIConsole. Deselects the text field.
	 */
	void deselect ();

	/**
	 * Works only for GUIConsole.
	 *
	 * @param title - The title for the console window.
	 */
	void setTitle (String title);

	/**
	 * Works only for GUIConsole. Sets the transparency for when the mouse is over the console window.
	 *
	 * @param alpha The transparency (scale from 0 to 1).
	 */
	void setHoverAlpha (float alpha);

	/**
	 * Works only for GUIConsole. Sets the transparency for when the mouse is not over the console window.
	 *
	 * @param alpha The transparency (scale from 0 to 1).
	 */
	void setNoHoverAlpha (float alpha);

	/**
	 * Works only for GUIConsole. Sets the color for when the mouse is over the console window. (Resets previous alpha changes).
	 *
	 * @param color The color.
	 */
	void setHoverColor (Color color);

	/**
	 * Works only for GUIConsole. Sets the color for when the mouse is not over the console window. (Resets previous alpha changes).
	 *
	 * @param color The color.
	 */
	void setNoHoverColor (Color color);

	/**
	 * Works only for GUIConsole. Adds a 'Submit' button next to the input field.
	 *
	 * @param enable Whether to show the submit button.
	 */
	void enableSubmitButton (boolean enable);

	/**
	 * Sets the text the submit button should show.
	 *
	 * @param text The text.
	 */
	void setSubmitText (String text);

	/**
	 * Returns the Console's Window object.
	 *
	 * @return The window.
	 */
	Window getWindow ();
}
