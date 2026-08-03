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

package io.github.moctave.weftspace;

import java.util.ArrayList;
import java.util.List;

/** A general exception thrown by the builder when it encounters an issue. */
public class BuilderException extends Exception {
	// MARK: Fields
	/** The node associated with this exception. */
	private final DataNode node;



	// MARK: Constructor
	/**
	 * Constructs a new builder exception with the given message and data node.
	 * 
	 * @param message The error message for this instruction
	 * @param node The data node that caused the exception to be thrown
	 */
	public BuilderException(String message, DataNode node) {
		super(message);
		this.node = node;
	}



	// MARK: Methods
	/**
	 * Represents this exception as a long-form error message.
	 * 
	 * @return The exception message followed by a trace of the node that threw it.
	 */
	public String represent() {
		final List<String> nodeDescriptions = new ArrayList<>();
		DataNode currentNode = node;
		while (currentNode != null) {
			nodeDescriptions.add(0, DataWriter.nodeToLine(currentNode));
			currentNode = currentNode.getParent();
		}

		String representation = "Builder Exception: " + this.getMessage();
		for (int i = 0; i < nodeDescriptions.size(); i++) {
			representation += System.lineSeparator();
			for (int tab = 0; tab < i; tab++) {
				representation += "\t";
			}
			representation += nodeDescriptions.get(i);
		}

		return representation;
	}
}
