
package ch.uprisesoft.chelonia.ide.console;

import com.badlogic.gdx.utils.Array;


public class Log {
	private final Array<LogEntry> logEntries;

	protected Log () {
		logEntries = new Array<>();
	}

	protected void addEntry (String msg, LogLevel level) {
		logEntries.add(new LogEntry(msg, level));
	}

	protected Array<LogEntry> getLogEntries () {
		return logEntries;
	}
}
