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
import java.util.List;

/** A subclass of a node that is attached to a specific line and file, for use in debugging. */
public class LoadedNode extends DataNode {
	// MARK: Constructor
	/**
	 * Sole constructor.
	 * @param name The name of this node, typically the first phrase present on its line.
	 * @param flag A flag indicating how this node should be treated during instantiation.
	 * @param parent This node's parent node (or {@code null} if it should be the root of its tree).
	 * @param args A list of arguments attached to this node.
	 * @param children A list of nodes which are children of this node.
	 * @param line The line this node was loaded from.
	 * @param file The file this node was loaded from.
	 */
	public LoadedNode(
		String name,
		Flag flag,
		DataNode parent,
		List<String> args,
		List<DataNode> children,
		int line,
		File file
	) {
		super(name, flag, parent, args, children);

		setLine(line);
		setFile(file);
	}



	// MARK: Fields
	/** The line this node was parsed from. */
	private int line;

	/** The file this node was parsed from. */
	private File file;



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the line number this node was parsed from.
	 * @return {@link #line}
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Setter: Changes the line number associated with this node.
	 * @param line The new value for {@link #line}.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * Getter: Returns the file this node was parsed from.
	 * @return {@link #file}
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Setter: Changes the file associated with this node.
	 * @param file The new value for {@link #file}.
	 */
	public void setFile(File file) {
		this.file = file;
	}
}
