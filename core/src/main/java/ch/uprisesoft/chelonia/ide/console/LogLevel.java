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

package ch.uprisesoft.chelonia.ide.console;

import com.badlogic.gdx.graphics.Color;

public enum LogLevel {
	DEFAULT(new Color(1, 1, 1, 1), ""),
	ERROR(new Color(217f / 255f, 0, 0, 1), "Error: "),
	SUCCESS(new Color(0, 217f / 255f, 0, 1), "Success! "),
	COMMAND(new Color(1, 1, 1, 1), "> ");

	private final Color color;
	private final String identifier;

	LogLevel (Color c, String identity) {
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
