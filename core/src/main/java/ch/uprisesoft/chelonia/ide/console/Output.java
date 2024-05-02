
package ch.uprisesoft.chelonia.ide.console;

import com.badlogic.gdx.utils.Array;


public class Output {
	private final Array<OutputEntry> outputEntries;

	protected Output () {
		outputEntries = new Array<>();
	}

	protected void addEntry (String msg, OutputLevel level) {
		outputEntries.add(new OutputEntry(msg, level));
	}

	protected Array<OutputEntry> getLogEntries () {
		return outputEntries;
	}
}
