package io.github.moctave.weftspace.exceptions;

import java.util.ArrayList;
import java.util.List;

import io.github.moctave.weftspace.DataNode;
import io.github.moctave.weftspace.DataWriter;

/** A general exception thrown by the builder when it encounters an issue. */
public class BuilderException extends Exception {
	// MARK: Fields
	/** The node associated with this exception. */
	private DataNode node;

	

	// MARK: Constructor
	/**
	 * Constructs a new builder exception with the given message and data node
	 * @param message The error message for this instruction
	 * @param node The data node that caused the exception to be thrown
	 */
	public BuilderException(String message, DataNode node) {
		super(message);
		this.node = node;
	}



	// MARK: Methods
	/**
	 * Represents this exception as a long-form error message
	 * @return The exception message followed by a trace of the node that threw it.
	 */
	public String represent() {
		List<String> nodeDescriptions = new ArrayList<>();
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
