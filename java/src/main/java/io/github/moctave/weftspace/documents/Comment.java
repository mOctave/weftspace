// Copyright (c) 2026 by mOctave
//
// This program is free software: you can redistribute it and/or modify it under the
// terms of the GNU Affero General Public License as published by the Free Software
// Foundation, either version 3 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
// PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License along with
// this program. If not, see <https://www.gnu.org/licenses/>.

package io.github.moctave.weftspace.documents;

import org.jspecify.annotations.*;

/** A full-line comment in a document. */
public class Comment implements DocumentComponent {
	/** The text of this comment. */
	private @NonNull String text;
	/** Whether or not this comment ignores indentation when represented as part of a node tree. */
	private boolean ignoreIndentation;

	// MARK: Constructor
	/**
	 * Creates a new comment with the given text and indentation policy.
	 * 
	 * @param text The text of the comment.
	 * @param ignoreIndentation Whether indentation is ignored when representing this comment.
	 */
	public Comment(@NonNull String text, boolean ignoreIndentation) {
		this.text = text;
		this.ignoreIndentation = ignoreIndentation;
	}

	// MARK: Methods
	/**
	 * Converts this node and its children into lines of text with the given indentation level.
	 * 
	 * @param indentation The indentation depth of this line.
	 * @return A string representation of this component.
	 */
	@Override
	public @NonNull String asLine(int indentation) {
		if (ignoreIndentation) {
			return "#" + text;
		} else {
			String s = "";
			for (int i = 0; i < indentation; i++) {
				s += "\t";
			}
			return s + "#" + text;
		}
	}




	// MARK: Getters / Setters
	/**
	 * Getter: Returns the text of this comment.
	 * 
	 * @return {@link #text}
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter: Changes the text of this comment.
	 * 
	 * @param text The new value for {@link #text}.
	 */
	public void setText(@NonNull String text) {
		this.text = text;
	}

	/**
	 * Getter: Returns true if this comment ignores indentation, false otherwise.
	 * 
	 * @return {@link #ignoreIndentation}
	 */
	public boolean ignoresIndentation() {
		return ignoreIndentation;
	}

	/**
	 * Setter: Changes whether this comment ignores indentation.
	 * 
	 * @param ignoreIndentation The new value for {@link #ignoreIndentation}.
	 */
	public void setIgnoreIndentation(boolean ignoreIndentation) {
		this.ignoreIndentation = ignoreIndentation;
	}
}
