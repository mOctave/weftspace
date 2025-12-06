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
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Deque;

/** A class which reads data from a file and stores it in a node tree. */
public class DataReader {
	// MARK: Constants
	/** Special flags that can be used when parsing */
	public enum Option {
		/** Do not apply node flags. */
		IGNORE_NODE_FLAGS
	}


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
	 * @param options Any {@link Option}s you wish to apply to the parse.
	 */
	public void parse(Option... options) {
		List<Option> optionList = Arrays.asList(options);
		boolean ignoreNodeFlags = optionList.contains(Option.IGNORE_NODE_FLAGS);
		try {
			Scanner s = new Scanner(file);
			int lineNumber = 0;

			Deque<DataNode> nodeStack = new ArrayDeque<>();
			DataNode currentNode = null;

			int indent = 0;
			Deque<Integer> indentDepths = new ArrayDeque<>();
			String expectedIndentString = null;

			indentDepths.push(0);

			while (s.hasNextLine()) {
				lineNumber ++;
				String line = s.nextLine();

				if (line.split("#").length == 0) {
					if (line.isBlank())
						continue;
				} else {
					if (line.split("#")[0].isBlank())
						continue;
				}

				indent = countLeadingWhitespace(line);
				String indentSubstring = getIndentSubstring(line, indentDepths.peek());
				if (expectedIndentString == null && indentSubstring.length() > 0) {
					expectedIndentString = indentSubstring;
					if (
						expectedIndentString.contains(" ")
						&& expectedIndentString.contains("\t")
					) {
						Logger.WARN_MIXED_WHITESPACE.log(file);
					}
				}

				if (indent > indentDepths.peek() && currentNode != null) {
					if (!expectedIndentString.equals(indentSubstring)) {
						Logger.WARN_MIXED_WHITESPACE.log(file);
					}
					nodeStack.push(currentNode);
					indentDepths.push(indent);
				} else {
					while (indentDepths.peek() > indent) {
						nodeStack.pop();
						indentDepths.pop();
					}
				}
				currentNode = makeNode(line, lineNumber, !ignoreNodeFlags);

				if (nodeStack.isEmpty() && currentNode != null) {
					root.addChild(currentNode);
					currentNode.setParent(root);
				} else if (currentNode != null) {
					DataNode parent = nodeStack.peek();
					parent.addChild(currentNode);
					currentNode.setParent(parent);
				}
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
	 * @param checkForFlags Whether or not node flags should be considered and applied.
	 * @return The node created from the line.
	 */
	public DataNode makeNode(String line, int number, boolean checkForFlags) {
		String trimmedLine = line.trim();
		List<String> data = new ArrayList<>();
		char splitOn = ' ';
		String currentItem = "";
		boolean isEmpty = true;
		for (char c : trimmedLine.toCharArray()) {
			if (isEmpty && !Character.isWhitespace(c)) {
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
		if (checkForFlags) {
			if (nodeName.equals("add")) {
				flag = DataNode.Flag.ADD;
				nodeName = data.get(0);
				data.remove(0);
			} else if (nodeName.equals("remove")) {
				flag = DataNode.Flag.REMOVE;
				nodeName = data.get(0);
				data.remove(0);
			}
		}


		return new LoadedNode(nodeName, flag, null, data, new ArrayList<>(), number, file);
	}



	/**
	 * Gets the substring which appears to be being used for indentation.
	 * @param line The line to find the indent of.
	 * @param depth The base depth from which the line is being indented.
	 * @return The substring used for indentation.
	 */
	public static String getIndentSubstring(String line, int depth) {
		int i = depth;
		String s = "";
		while (i < line.length() && (line.charAt(i) == '\t' || line.charAt(i) == ' ')) {
			s += line.charAt(i);
			i++;
		}
		return s;
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
	 * Counts the number of leading tab or space characters on a line.
	 * @param line The line to count.
	 * @return The number of tabs or spaces that come before the first
	 * non-whitespace character on the line.
	 */
	public static int countLeadingWhitespace(String line) {
		int i = 0;
		while (i < line.length() && (line.charAt(i) == '\t' || line.charAt(i) == ' ')) {
			i++;
		}
		return i;
	}



	/**
	 * Deprecated method included to support programs written expecting legacy
	 * whitespace handling. Calls {@link #countLeadingWhitespace(String)}, see that
	 * method for documentation.
	 */
	@Deprecated
	public static int countLeadingTabs(String line) {
		return countLeadingWhitespace(line);
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
