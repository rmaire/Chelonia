package ch.uprisesoft.chelonia.ide.console;

import com.badlogic.gdx.graphics.Color;

public enum OutputLevel {
	DEFAULT(new Color(1, 1, 1, 1), ""),
	ERROR(new Color(217f / 255f, 0, 0, 1), "Error: "),
	SUCCESS(new Color(0, 217f / 255f, 0, 1), "Success! "),
	COMMAND(new Color(1, 1, 1, 1), "> ");

	private final Color color;
	private final String identifier;

	OutputLevel (Color c, String identity) {
		this.color = c;
		identifier = identity;
	}

	Color getColor () {
		return color;
	}

	String getIdentifier () {
		return identifier;
	}
}
