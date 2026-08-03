// Copyright (c) 2025 by mOctave
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

package io.github.moctave.weftspace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jspecify.annotations.*;

/** A class which writes data from a node into a file. */
public class DataWriter {
	// MARK: Fields
	/** The file to write to. */
	private final @NonNull File file;

	/** A PrintWriter used to handle the writing. */
	private @Nullable PrintWriter writer;



	// MARK: Constructor
	/**
	 * Sole constructor.
	 * 
	 * @param file The file to write to.
	 */
	public DataWriter(@NonNull File file) {
		this.file = file;
	}



	// MARK: Methods
	/**
	 * Creates and opens a PrintWriter for the given file, throwing an exception if it occurs.
	 */
	public void open() throws IOException {
		writer = new PrintWriter(new FileWriter(file, true));
	}



	/**
	 * Helper function to close the PrintWriter.
	 */
	public void close() {
		if (writer != null) writer.close();
	}



	/**
	 * Writes a node to a file with no indent.
	 * 
	 * @param node The node to write.
	 */
	public void write(@NonNull DataNode node) {
		write(node, 0);
	}



	/**
	 * Writes a node to the file.
	 * 
	 * @param node The node to write.
	 * @param indentLevel How many tabs should be inserted before the node.
	 */
	public void write(@NonNull DataNode node, int indentLevel) {
		if (writer == null) return;

		for (int i = 0; i < indentLevel; i++) {
			writer.append("\t");
		}
		writer.append(nodeToLine(node));
		writer.append("\n");
		for (DataNode child : node.getChildren()) {
			write(child, indentLevel + 1);
		}
		if (indentLevel == 0) {
			writer.append("\n");
		}
	}



	/**
	 * Represents a node as a line to be saved to a file.
	 * Not to be confused with {@link DataNode#toString()}.
	 * 
	 * @param node The node to convert.
	 * @return A single-line representation of the node, excluding its children.
	 */
	public static @NonNull String nodeToLine(@NonNull DataNode node) {
		String s = quoteWord(node.getName()) + " ";

		for (String arg : node.getArgs()) {
			s += quoteWord(arg) + " ";
		}

		return s.trim();
	}



	/**
	 * Puts quotes around text, adapting between no quotes, double quotes, and backticks as necessary.
	 * Does not conform to Endless Sky human readability conventions, but uses the simplest
	 * possible quotes for a given word.
	 * 
	 * @param word The text to quote.
	 * @return The text in a format that will be interpreted as a single token by a parser.
	 */
	public static @NonNull String quoteWord(@NonNull String word) {
		if (word.contains(" ")) {
			if (word.contains("\"")) {
				return "`" + word + "`";
			} else {
				return "\"" + word + "\"";
			}
		} else {
			return word;
		}
	}



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the file being written to.
	 * 
	 * @return {@link #file}
	 */
	public @NonNull File getFile() {
		return file;
	}

	// There is no setter for the file. The file is final.

	/**
	 * Getter: Returns the PrintWriter for this writer.
	 * 
	 * @return {@link #writer}
	 */
	public @NonNull PrintWriter getWriter() {
		return writer;
	}

	// There is no setter for the writer. Use DataWriter.open() instead.
}
