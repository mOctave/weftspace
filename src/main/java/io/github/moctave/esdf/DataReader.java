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

package io.github.moctave.esdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Deque;

/** A class which reads data from a file and stores it in a node tree. */
public class DataReader {
	// MARK: Constructor
	/**
	 * Sole constructor.
	 * @param file The file to be parsed.
	 * @param root The root node of the tree that nodes are stored in.
	 */
	public DataReader(File file, DataNode root) {
		this.file = file;
		this.root = root;
	}



	// MARK: Fields
	// The file to be parsed.
	private File file;

	/** The root node of the tree that nodes are stored in. */
	private DataNode root;



	// MARK: Methods
	/**
	 * Parses the file associated with this object, and stores all nodes in the tree.
	 */
	public void parse() {
		try {
			Scanner s = new Scanner(file);
			int lineNumber = 0;

			Deque<DataNode> nodeStack = new ArrayDeque<>();

			int tabs = 0;
			int lastTabs = 0;
			DataNode currentNode = null;

			while (s.hasNextLine()) {
				lineNumber ++;
				String line = s.nextLine();
				if (line.split("#")[0].isBlank())
					continue;

				tabs = countLeadingTabs(line);
				if (tabs > lastTabs && currentNode != null) {
					nodeStack.push(currentNode);
				} else {
					while (nodeStack.size() > tabs) {
						nodeStack.pop();
					}
				}
				currentNode = makeNode(line, lineNumber);

				if (nodeStack.isEmpty() && currentNode != null) {
					root.addChild(currentNode);
				} else if (currentNode != null) {
					nodeStack.peek().addChild(currentNode);
				}

				lastTabs = tabs;
			}

			s.close();
		} catch (FileNotFoundException e) {
			Logger.ERROR_FILE_DNE.log(file);
		}
	}



	/**
	 * Parses a single line and converts it to a node.
	 * @param line The line to parse.
	 * @param number The number of the line being parsed, for debugging purposes.
	 * @return The node created from the line.
	 */
	public DataNode makeNode(String line, int number) {
		String trimmedLine = line.trim();
		List<String> data = new ArrayList<>();
		char splitOn = ' ';
		String currentItem = "";
		boolean isEmpty = true;
		for (char c : trimmedLine.toCharArray()) {
			if (isEmpty && c != ' ') {
				isEmpty = false;
				if (c == '"') {
					// Item will end with a double quote, ignore this one.
					splitOn = '"';
					continue;
				} else if (c == '`') {
					// Item will end with a backtick, ignore this one.
					splitOn = '`';
					continue;
				} else {
					// Item will end when the word it's in does.
					splitOn = ' ';
				}
			} else if (isEmpty) {
				continue;
			}

			if (c == splitOn) {
				// Found end of item, add it to the list and start on the next one
				data.add(currentItem);
				currentItem = "";
				isEmpty = true;
				continue;
			} else if (c == '#' && splitOn == ' ') {
				// Ignore everything after a comment
				break;
			}

			// Add to the current item
			currentItem += c;
		}

		// Items can end at the end of the line, too
		if (!currentItem.isEmpty()) {
			data.add(currentItem);
		}

		if (data.size() == 0)
			return null;

		// The first entry is the node name, everything else is args.
		String nodeName = data.get(0);
		data.remove(0);

		DataNode.Flag flag = DataNode.Flag.NORMAL;

		// Check for flags
		if (nodeName.equals("add")) {
			flag = DataNode.Flag.ADD;
			nodeName = data.get(0);
			data.remove(0);
		} else if (nodeName.equals("remove")) {
			flag = DataNode.Flag.REMOVE;
			nodeName = data.get(0);
			data.remove(0);
		}


		return new LoadedNode(nodeName, flag, data, new ArrayList<>(), number, file);
	}



	/**
	 * Trims comments from a line of text, removing everything after the first {@code #} character.
	 * @param line The line to trim.
	 * @return The uncommented line.
	 */
	public static String trimComments(String line) {
		return line.split("#")[0];
	}



	/**
	 * Counts the number of leading tabs on a line.
	 * @param line The line to count.
	 * @return The number of tabs that come before the first non-tab character on the line.
	 */
	public static int countLeadingTabs(String line) {
		int i = 0;
		while (i < line.length() && line.charAt(i) == '\t') {
			i++;
		}
		return i;
	}



	/**
	 * Checks if a line contains only whitespace, as defined by {@link Character#isWhitespace(char)}.
	 * @param line The line to check.
	 * @return {@code true} if the line is empty or contains only whitespace, or {@code false} if it contains other characters as well.
	 */
	public static boolean containsOnlyWhitespace(String line) {
		// If the line is empty, for our purposes it's all whitespace.
		if (line == null || line.isEmpty())
			return true;

		// Check if the line contains anything that isn't whitespace.
		for (int i = 0; i < line.length(); i++) {
			if (!Character.isWhitespace(line.charAt(i)))
				return false;
		}

		return true;
	}



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the file this reader is parsing.
	 * @return {@link #file}
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Setter: Changes the file this reader is parsing.
	 * @param file The new value for {@link #file}.
	 */
	public void setFile(File file) {
		this.file = file;
	}


	/**
	 * Getter: Returns the root node of the tree this reader is writing to.
	 * @return {@link #root}
	 */
	public DataNode getRoot() {
		return root;
	}

	/**
	 * Setter: Changes the root node this reader adds children to.
	 * @param root The new value for {@link #root}.
	 */
	public void setRoot(DataNode root) {
		this.root = root;
	}
}
