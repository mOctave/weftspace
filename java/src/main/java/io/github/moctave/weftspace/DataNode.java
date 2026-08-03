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

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.*;

/** A class representing a node in the data tree. */
public class DataNode {
	// MARK: Fields
	/** The name of this node. */
	private @NonNull String name;

	/** The parent of this node. */
	private @Nullable DataNode parent;

	/** This node's arguments. */
	private @NonNull List<String> args;

	/** This node's children. */
	private @NonNull List<DataNode> children;



	// MARK: Constructors
	/**
	 * Primary constructor. Takes all the standard arguments, except
	 * those defined by {@link LoadedNode}.
	 * 
	 * @param name The name of this node, typically the first phrase present on its line.
	 * @param parent This node's parent node (or {@code null} if it should be the root of its tree).
	 * @param args A list of arguments attached to this node.
	 * @param children A list of nodes which are children of this node.
	 */
	public DataNode(
		@NonNull String name,
		@Nullable DataNode parent,
		@NonNull List<String> args,
		@NonNull List<DataNode> children
	) {
		this.name = name;
		this.parent = parent;
		this.args = args;
		this.children = children;
	}


	/**
	 * A simplified constructor that creates an empty node intended to be used as a root
	 * node for the {@link DataReader}.
	 */
	public DataNode() {
		this.name = "--ROOT--";
		this.parent = null;
		this.args = new ArrayList<>();
		this.children = new ArrayList<>();
	}



	// MARK: Methods
	/**
	 * Generates a hash code for this node, so that all nodes which are equal
	 * have the same hash code.
	 * 
	 * Multiple distinct nodes may have the same hash code, as
	 * (1) the mechanics for name.hashCode() are not controlled,
	 * (2) parents are not taken into account in this method, and
	 * (3) nodes which have themself as a child will not have their children considered.
	 * 
	 * @return A hash code value for this node.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 1;
		hash = prime * hash + (name == null ? 0 : name.hashCode());
		hash = prime * hash + (args == null ? 0 : args.hashCode());
		hash = prime * hash + ((children == null || children.contains(this)) ? 0 : children.hashCode());
		return hash;
	}



	/**
	 * Indicates whether an object is equal to this node, comparing the names,
	 * arguments, and children of the two nodes.
	 * 
	 * NOTE: Parents are NOT considered by this method, to reduce complexity. If you
	 * need to check if two nodes have identical parents, use {@link #sameParent(DataNode)}
	 * 
	 * @param obj The object to compare this node against.
	 * @return {@code true} if this node is equal to {@code obj}, {@code false} otherwise.
	 */
	@Override
	public boolean equals(@Nullable Object obj) {
		// The object is a reference for this node!
		if (obj == this) return true;

		// The object is not a data node!
		if (!(obj instanceof DataNode)) return false;


		final DataNode node = (DataNode) obj;

		// Check if the two nodes have a different parameter
		if (!node.getName().equals(name)) return false;

		// Check if the arguments and children are equal
		if (!node.getArgs().equals(args)) return false;
		if (!node.getChildren().equals(children)) return false;

		// Everything that matters is equal, return true
		return true;
	}



	/**
	 * Compares the parent of this node against another node to see if they are equal.
	 * 
	 * @param node The node to compare this node against.
	 * @return {@code true} if this node has the same parent as the other node,
	 * {@code false} otherwise.
	 */
	public boolean sameParent(@NonNull DataNode node) {
		if (node.hasParent()) {
			if (!this.hasParent()) {
				return false;
			}

			return (node.getParent().equals(this.getParent()));
		} else {
			return !(this.hasParent());
		}
	}



	/**
	 * Returns a string representation of this node, including name, arguments,
	 * and the number of children it has.
	 * 
	 * @return A string representation of this node.
	 */
	@Override
	public @NonNull String toString() {
		return String.format(
			"Node{name: %s, args: %s, children: %d}",
			name,
			args.toString(),
			children.size()
		);
	}


	/**
	 * Mutator method to add an argument to this node's argument list.
	 * 
	 * @param arg The argument to add.
	 */
	public void addArg(@NonNull String arg) {
		args.add(arg);
	}



	/**
	 * Accessor method to get a specific argument from this node.
	 * 
	 * @param i The index of the argument to get.
	 * @return The selected argument.
	 */
	public @NonNull String getArg(int i) {
		return args.get(i);
	}



	/**
	 * Convenience method to get the size of this node's argument list.
	 * 
	 * @return The number of arguments this node has.
	 */
	public int countArgs() {
		return args.size();
	}


	/**
	 * Mutator method to add a node to the tree as a child of this node.
	 * 
	 * @param child The node to add.
	 */
	public void addChild(@NonNull DataNode child) {
		children.add(child);
	}



	/**
	 * Accessor method to get a specific child from this node.
	 * 
	 * @param i The index of the child to get.
	 * @return The selected child.
	 */
	public @NonNull DataNode getChild(int i) {
		return children.get(i);
	}



	/**
	 * Convenience method to get the size of this node's child list.
	 * 
	 * @return The number of children this node has.
	 */
	public int countChildren() {
		return children.size();
	}



	/**
	 * Convenience method to check whether a node has a defined parent.
	 * 
	 * @return {@code true} if a node has a parent, or {@code false} otherwise.
	 */
	public boolean hasParent() {
		return parent != null;
	}



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the name of this node.
	 * 
	 * @return {@link #name}
	 */
	public @NonNull String getName() {
		return name;
	}

	/**
	 * Setter: Changes the name of this node.
	 * 
	 * @param name The new value for {@link #name}.
	 */
	public void setName(@NonNull String name) {
		this.name = name;
	}


	/**
	 * Getter: Returns the parent of this node.
	 * 
	 * @return {@link #parent}
	 */
	public @Nullable DataNode getParent() {
		return parent;
	}

	/**
	 * Setter: Changes this node's parent.
	 * This method should be used very cautiously, as it can easily
	 * damage the structure of the node tree.
	 * 
	 * @param parent This node's new {@link #parent}.
	 */
	public void setParent(@Nullable DataNode parent) {
		this.parent = parent;
	}


	/**
	 * Getter: Returns the full argument list of this node.
	 * 
	 * @return {@link #args}
	 */
	public @NonNull List<String> getArgs() {
		return args;
	}

	/**
	 * Setter: Entirely overwrites the arguments of this node.
	 * 
	 * @param args The new value for {@link #args}.
	 */
	public void setArgs(@NonNull List<String> args) {
		this.args = args;
	}


	/**
	 * Getter: Returns the full list of children of this node.
	 * 
	 * @return {@link #children}
	 */
	public @NonNull List<DataNode> getChildren() {
		return children;
	}

	/**
	 * Setter: Entirely overwrites the children of this node.
	 * This method should be used very cautiously, as it can easily
	 * damage the structure of the node tree.
	 * 
	 * @param children The new value for {@link #children}.
	 */
	public void setChildren(@NonNull List<DataNode> children) {
		this.children = children;
	}

}
