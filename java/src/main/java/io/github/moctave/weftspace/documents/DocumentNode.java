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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.*;

import io.github.moctave.weftspace.DataNode;
import io.github.moctave.weftspace.DataWriter;

/** A type of node linked directly to a document. */
public class DocumentNode extends DataNode implements DocumentComponent {
	/** The document components contained within this node. */
	private @NonNull List<DocumentComponent> components;
	/** An optional comment string to place after the text in this node. */
	private @Nullable String comment;

	// MARK: Constructor
	/**
	 * A constructor that creates a new document node given no arguments.
	 */
	public DocumentNode() {
		components = new ArrayList<>();
	}

	/**
	 * A constructor that creates a new document node given an existing data node.
	 * This constructor may share data with the original node.
	 * 
	 * @param node The node to base this document node on.
	 */
	public DocumentNode(DataNode node) {
		this();

		setName(node.getName());
		setArgs(node.getArgs());

		for (DataNode child : node.getChildren()) {
			addChild(Objects.requireNonNull(child));
		}
	}



	// MARK: Methods
	/**
	 * Converts this node and its children into lines of text with the given
	 * indentation level.
	 * 
	 * @param indentation The indentation depth of this line.
	 * @return A string representation of this component.
	 */
	@Override
	public @NonNull String asLine(int indentation) {
		String line = "";

		for (int i = 0; i < indentation; i++) {
			line += "\t";
		}

		line += DataWriter.nodeToLine(this);

		if (comment != null) {
			line += " #" + comment;
		}

		for (DocumentComponent component : components) {
			line += System.lineSeparator() + component.asLine(indentation + 1);
		}

		return line;
	}

	/**
	 * Mutator method to add a node to the tree as a child of this node
	 * AND as a subcomponent.
	 * 
	 * @param child The node to add.
	 */
	public void addChild(@NonNull DataNode child) {
		if (child instanceof DocumentNode) {
			super.addChild(child);
			components.add((DocumentNode) child);
			child.setParent(this);
		} else {
			final DocumentNode converted = new DocumentNode(child);
			super.addChild(converted);
			components.add(converted);
			converted.setParent(this);
		}
	}

	// MARK: Getters / Setters
	/**
	 * Getter: Returns the list of components in this node.
	 * 
	 * @return {@link #components}
	 */
	public List<DocumentComponent> getComponents() {
		return components;
	}

	/**
	 * Getter: Returns the comment for this node.
	 * 
	 * @return {@link #comment}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Setter: Changes this node's comment.
	 * 
	 * @param comment The new value for {@link #comment}.
	 */
	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}
}
