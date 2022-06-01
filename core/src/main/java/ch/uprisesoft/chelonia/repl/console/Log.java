
package ch.uprisesoft.chelonia.repl.console;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.IOException;
import java.io.Writer;

public class Log {
	private Array<LogEntry> logEntries;

	protected Log () {
		logEntries = new Array<LogEntry>();
	}

	protected void addEntry (String msg, LogLevel level) {
		logEntries.add(new LogEntry(msg, level));
	}

	protected Array<LogEntry> getLogEntries () {
		return logEntries;
	}
}
