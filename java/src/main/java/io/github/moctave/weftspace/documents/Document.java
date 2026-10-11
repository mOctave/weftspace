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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.jspecify.annotations.*;

import io.github.moctave.weftspace.DataNode;
import io.github.moctave.weftspace.DataReader;
import io.github.moctave.weftspace.ReaderException;

/** A file consisting of nodes, blank lines, and comments. */
public class Document {
	/** The root node of this document. */
	private @NonNull DataNode root;

	/** The components of this document. */
	private @NonNull List<DocumentComponent> components;



	// MARK: Constructor
	/**
	 * Constructs a new document reading from the given file.
	 * 
	 * @param root The data node all nodes in this document are descended from.
	 * @param source The file to read data from.
	 */
	public Document(@NonNull DataNode root, @NonNull File source) throws ReaderException {
		this.components = new ArrayList<>();
		this.root = root;

		try {
			final Scanner s = new Scanner(source);

			final Deque<DocumentNode> nodeStack = new ArrayDeque<>();
			DocumentNode currentNode = null;

			int indent = 0;
			final Deque<Integer> indentDepths = new ArrayDeque<>();

			indentDepths.push(0);

			while (s.hasNextLine()) {
				String line = s.nextLine();

				int blankLines = 0;
				while (line.trim().length() == 0) {
					blankLines++;
					line = s.nextLine();
				}
				if (blankLines > 0) {
					if (nodeStack.isEmpty()) {
						components.add(new Spacer(blankLines));
					} else {
						nodeStack.peek().getComponents().add(new Spacer(blankLines));
					}
				}

				if (line.startsWith("#")) {
					final Comment c = new Comment(Objects.requireNonNull(line.substring(1)), true);
					if (nodeStack.isEmpty()) {
						components.add(c);
					} else {
						nodeStack.peek().getComponents().add(c);
					}
					continue;
				} else if (line.trim().startsWith("#")) {
					final Comment c = new Comment(Objects.requireNonNull(line.trim().substring(1)), false);
					if (nodeStack.isEmpty()) {
						components.add(c);
					} else {
						nodeStack.peek().getComponents().add(c);
					}
					continue;
				}

				indent = DataReader.countLeadingWhitespace(line);

				if (indent > indentDepths.peek() && currentNode != null) {
					nodeStack.push(currentNode);
					indentDepths.push(indent);
				} else {
					while (indentDepths.peek() > indent) {
						nodeStack.pop();
						indentDepths.pop();
					}
				}
				currentNode = makeNode(line);

				if (nodeStack.isEmpty() && currentNode != null) {
					components.add(currentNode);
					currentNode.setParent(currentNode);
					root.addChild(currentNode);
					currentNode.setParent(root);
				} else if (currentNode != null) {
					final DataNode parent = nodeStack.peek();
					parent.addChild(currentNode);
					currentNode.setParent(parent);
				}
			}

			s.close();

		} catch (FileNotFoundException e) {
			throw new ReaderException(String.format("No such file as %s", source.getPath()));
		}
	}



	// MARK: Methods
	/**
	 * Parses a single line and converts it to a node.
	 * 
	 * @param line The line to parse.
	 * @return The node created from the line.
	 */
	private @Nullable DocumentNode makeNode(@NonNull String line) {
		final String trimmedLine = line.trim();
		final List<String> data = new ArrayList<>();
		char splitOn = ' ';
		String currentItem = "";
		boolean isEmpty = true;
		String comment = null;

		for (char c : trimmedLine.toCharArray()) {
			if (comment != null) {
				comment += c;
				continue;
			}

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
				if (comment == null) {
					comment = "";
				}
				continue;
			}

			// Add to the current item
			currentItem += c;
		}

		// Items can end at the end of the line, too
		if (!currentItem.isEmpty()) {
			data.add(currentItem);
		}

		if (data.size() == 0) return null;

		// The first entry is the node name, everything else is args.
		final String nodeName = Objects.requireNonNull(data.get(0));
		data.remove(0);

		final DocumentNode node = new DocumentNode(new DataNode(nodeName, null, data, new ArrayList<>()));
		node.setComment(comment);

		return node;
	}



	/**
	 * Writes the contents of this document out to the given file.
	 * 
	 * @param dest The file to write data to.
	 */
	public void writeOut(@NonNull File dest) throws IOException {
		final PrintWriter writer = new PrintWriter(new FileWriter(dest, true));

		for (DocumentComponent component : components) {
			writer.append(component.asLine(0) + System.lineSeparator());
		}

		writer.close();
	}
}
