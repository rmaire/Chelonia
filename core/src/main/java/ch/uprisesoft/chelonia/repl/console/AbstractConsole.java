/**
 * Copyright 2018 StrongJoshua (strongjoshua@hotmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package ch.uprisesoft.chelonia.repl.console;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Eric
 */
public abstract class AbstractConsole implements Console, Disposable {
	protected final Log log;

	protected boolean disabled;

	protected boolean executeHiddenCommands = true;
	protected boolean displayHiddenCommands = false;
	protected boolean consoleTrace = false;

	public AbstractConsole () {
		log = new Log();
	}

	@Override public void log (String msg, LogLevel level) {
		log.addEntry(msg, level);
	}

	@Override public void log (String msg) {
		this.log(msg, LogLevel.DEFAULT);
	}

	@Override public boolean isDisabled () {
		return disabled;
	}

	@Override public void setDisabled (boolean disabled) {
		this.disabled = disabled;
	}


	@Override public void setExecuteHiddenCommands (boolean enabled) {
		executeHiddenCommands = enabled;
	}

	@Override public boolean isExecuteHiddenCommandsEnabled () {
		return executeHiddenCommands;
	}

	@Override public void setDisplayHiddenCommands (boolean enabled) {
		displayHiddenCommands = enabled;
	}

	@Override public boolean isDisplayHiddenCommandsEnabled () {
		return displayHiddenCommands;
	}

	@Override public void setConsoleStackTrace (boolean enabled) {
		this.consoleTrace = enabled;
	}

	@Override public void setMaxEntries (int numEntries) {
	}

	@Override public void clear () {
	}

	@Override public void setSize (int width, int height) {
	}

	@Override public void setSizePercent (float wPct, float hPct) {
	}

	@Override public void setPosition (int x, int y) {
	}

	@Override public void setPositionPercent (float xPosPct, float yPosPct) {
	}

	@Override public void resetInputProcessing () {
	}

	@Override public InputProcessor getInputProcessor () {
		return null;
	}

	@Override public void draw () {
	}

	@Override public void refresh () {
	}

	@Override public void refresh (boolean retain) {
	}

	@Override public int getDisplayKeyID () {
		return 0;
	}

	@Override public void setDisplayKeyID (int code) {
	}

	@Override public boolean hitsConsole (float screenX, float screenY) {
		return false;
	}

	@Override public void dispose () {
	}

	@Override public boolean isVisible () {
		return false;
	}

	@Override public void setVisible (boolean visible) {
	}

	@Override public void select () {
	}

	@Override public void deselect () {
	}

	@Override public void setTitle (String title) {
	}

	@Override public void setHoverAlpha (float alpha) {
	}

	@Override public void setNoHoverAlpha (float alpha) {
	}

	@Override public void setHoverColor (Color color) {
	}

	@Override public void setNoHoverColor (Color color) {
	}

	@Override public void enableSubmitButton (boolean enable) {
	}

	@Override public void setSubmitText (String text) {
	}

	@Override public Window getWindow () {
		return null;
	}
}
