package io.github.moctave.weftspace.exceptions;

/** An exception thrown when parsing a file into nodes. */
public class ReaderException extends Exception {
	/**
	 * Constructs a new file exception with the given message
	 * @param message The error message for this instruction
	 */
	public ReaderException(String message) {
		super(message);
	}
}
