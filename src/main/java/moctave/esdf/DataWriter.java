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

package moctave.esdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DataWriter {
	// MARK: Constructor
	/**
	 * Sole constructor.
	 * @param file The file to write to.
	 */
	public DataWriter(File file) {
		this.file = file;
	}



	// MARK: Fields
	/** The file to write to. */
	private File file;

	/** A PrintWriter used to handle the writing. */
	private PrintWriter writer;



	// MARK: Methods
	/**
	 * Creates and opens a PrintWriter for the given file, handling exceptions.
	 */
	public void open() {
		try {
			writer = new PrintWriter(new FileWriter(file, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Helper function to close the PrintWriter.
	 */
	public void close() {
		writer.close();
	}



	/**
	 * Writes a node to the file.
	 * @param node The node to write.
	 * @param indentLevel How many tabs should be inserted before the node.
	 */
	public void write(DataNode node, int indentLevel) {
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
	 * @param node The node to convert.
	 * @return A single-line representation of the node, excluding its children.
	 */
	public static String nodeToLine(DataNode node) {
		String s = "";
		if (node.getFlag() == DataNode.Flag.ADD)
			s += "add ";
		else if (node.getFlag() == DataNode.Flag.REMOVE)
			s += "remove ";

		s += quoteWord(node.getName()) + " ";

		for (String arg : node.getArgs()) {
			s += quoteWord(arg) + " ";
		}

		return s.trim();
	}



	/**
	 * Puts quotes around text, adapting between no quotes, double quotes, and backticks as necessary.
	 * Does not confom to Endless Sky human readability conventions, but uses the simplest
	 * possible quotes for a given word.
	 * @param word The text to quote.
	 * @return The text in a format that will be interpreted as a single token by a parser.
	 */
	public static String quoteWord(String word) {
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
	 * @return {@link #file}
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Setter: Changes the file being written to.
	 * @param file The new value for {@link #file}.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Getter: Returns the PrintWriter for this writer.
	 * @return {@link #writer}
	 */
	public PrintWriter getWriter() {
		return writer;
	}

	// There is no setter for the writer. Use DataWriter.open() instead.
}
