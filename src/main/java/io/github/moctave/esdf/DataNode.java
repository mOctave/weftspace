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

import java.util.ArrayList;
import java.util.List;

/** A class representing a node on the data tree. */
public class DataNode {
	// MARK: Constants
	/**
	 * The flag attached to this node, influencing how it should be treated during instantiation.
	 */
	public static enum Flag {
		/** This node should be treated normally, with no special consideration. */
		NORMAL,
		/** This node should always result in the addition of an object, even if it would usually overwrite one instead. */
		ADD,
		/** This node should result in the removal of its associated object. */
		REMOVE,
		/** This node is a root node, and should not be parsed or written. */
		ROOT
	}



	// MARK: Constructors
	/**
	 * Primary constructor. Takes all the standard arguments, except
	 * those defined by {@link LoadedNode}.
	 * @param name The name of this node, typically the first phrase present on its line.
	 * @param flag A flag indicating how this node should be treated during instantiation.
	 * @param args A list of arguments attached to this node.
	 * @param children A list of nodes which are children of this node.
	 */
	public DataNode(
		String name,
		Flag flag,
		List<String> args,
		List<DataNode> children
	) {
		setName(name);
		this.flag = flag;
		this.args = args;
		this.children = children;
	}


	/**
	 * A simplified constructor that creates an empty node intended to be used as a root
	 * node for the {@link DataReader}.
	 */
	public DataNode() {
		setName("--ROOT--");
		this.flag = Flag.ROOT;
		this.args = new ArrayList<>();
		this.children = new ArrayList<>();
	}



	// MARK: Fields
	/** The name of this node. */
	private String name;

	/** The flag attached to this node. */
	private Flag flag;

	/** This node's arguments. */
	private List<String> args;

	/** This node's children. */
	private List<DataNode> children;



	// MARK: Methods
	/**
	 * Generates a hash code for this node, so that all nodes which are equal
	 * have the same hash code.
	 * @return A hash code value for this node.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 1;
		hash = prime * hash + (name == null ? 0 : name.hashCode());
		hash = prime * hash + (flag == null ? 0 : flag.hashCode());
		hash = prime * hash + (args == null ? 0 : args.hashCode());
		hash = prime * hash + (children == null ? 0 : children.hashCode());
		return hash;
	}



	/**
	 * Indicates whether an object is equal to this node, comparing the names, flags,
	 * arguments, and children of the two nodes.
	 */
	@Override
	public boolean equals(Object obj) {
		// The object is a reference for this node!
		if (obj == this) return true;

		// The object is not a data node!
		if (!(obj instanceof DataNode)) return false;


		DataNode node = (DataNode) obj;
		
		// Check if the two nodes have a different parameter
		if (!node.getName().equals(name)) return false;
		if (!node.getFlag().equals(flag)) return false;

		// Check if the arguments and children are equal
		if (!node.getArgs().equals(args)) return false;
		if (!node.getChildren().equals(children)) return false;

		// Everything that matters is equal, return true
		return true;
	}



	/** 
	 * Returns a string representation of this node, including name, arguments,
	 * and the number of children it has.
	 * @return A string representation of this node.
	 */
	@Override
	public String toString() {
		return String.format(
			"Node{name: %s, args: %s, children: %d}",
			name,
			args.toString(),
			children.size()
		);
	}


	/**
	 * Mutator method to add a node to the tree as a child of this node.
	 * @param child The node to add.
	 */
	public void addChild(DataNode child) {
		children.add(child);
	}


	/**
	 * Mutator method to add an argument to this node's argument list.
	 * @param arg The argument to add.
	 */
	public void addArg(String arg) {
		args.add(arg);
	}



	/**
	 * Accessor method to get a specific argument from this node.
	 * @param i The index of the argument to get.
	 * @return The selected argument.
	 */
	public String getArg(int i) {
		return args.get(i);
	}



	/**
	 * Convenience method to get the size of this node's argument list.
	 * @return The number of arguments this node has.
	 */
	public int countArgs() {
		return args.size();
	}



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the name of this node.
	 * @return {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter: Changes the name of this node.
	 * @param name The new value for {@link #name}.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Getter: Returns the flag attached to this node.
	 * @return {@link #flag}
	 */
	public Flag getFlag() {
		return flag;
	}

	/**
	 * Setter: Changes the flag attached to this node.
	 * @param flag The new value for {@link #flag}.
	 */
	public void setFlag(Flag flag) {
		this.flag = flag;
	}


	/**
	 * Getter: Returns the full argument list of this node.
	 * @return {@link #args}
	 */
	public List<String> getArgs() {
		return args;
	}

	/**
	 * Setter: Entirely overwrites the arguments of this node.
	 * @param args The new value for {@link #args}.
	 */
	public void setArgs(List<String> args) {
		this.args = args;
	}


	/**
	 * Getter: Returns the full list of children of this node.
	 * @return {@link #children}
	 */
	public List<DataNode> getChildren() {
		return children;
	}

	/**
	 * Setter: Entirely overwrites the children of this node.
	 * @param children The new value for {@link #children}.
	 */
	public void setChildren(List<DataNode> children) {
		this.children = children;
	}

}
