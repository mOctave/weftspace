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
import java.io.PrintStream;

/** A class which handles error logging for this library. */
public abstract class Logger {
	// MARK: Constants
	// ANSI Colours
	/** Escape sequence for ANSI black text. */
	public static final String BLACK = "\u001B[30m";
	/** Escape sequence for ANSI red text. */
	public static final String RED = "\u001B[31m";
	/** Escape sequence for ANSI green text. */
	public static final String GREEN = "\u001B[32m";
	/** Escape sequence for ANSI yellow text. */
	public static final String YELLOW = "\u001B[33m";
	/** Escape sequence for ANSI blue text. */
	public static final String BLUE = "\u001B[34m";
	/** Escape sequence for ANSI magenta text. */
	public static final String MAGENTA = "\u001B[35m";
	/** Escape sequence for ANSI cyan text. */
	public static final String CYAN = "\u001B[36m";
	/** Escape sequence for ANSI white text. */
	public static final String WHITE = "\u001B[37m";
	/** Escape sequence for ANSI formatting reset. */
	public static final String RESET = "\u001B[0m";
	/** Escape sequence for ANSI bold text. */
	public static final String BOLD = "\u001B[1m";

	/** An enum storing different levels of severity that a message can have. */
	public static enum Severity {
		/** This error resulted in the program's immediate failure. */
		FATAL,
		/** This error is serious, but not immediately fatal. */
		ERROR,
		/** This error is not serious, but may cause later issues or unintended behaviour. */
		WARN,
		/** This message is not an error. */
		INFO,
		/** This message is an indicator of success. */
		SUCCESS
	}

	// MARK: Messages
	/** Fatal Error: No context */
	public static final DynamicMessage FATAL_GENERIC = new DynamicMessage(Severity.FATAL, "A fatal error was encountered. No other information is available.");

	/** Error: No context */
	public static final DynamicMessage ERROR_GENERIC = new DynamicMessage(Severity.ERROR, "An error was encountered. No other information is available.");
	/** Error: File does not exist */
	public static final DynamicMessage ERROR_FILE_DNE = new DynamicMessage(Severity.ERROR, "The file $FILEPATH does not exist or could not be found.");

	/** Error: Node failed to parse */
	public static final DynamicMessage ERROR_NODE_PARSE_GENERIC = new DynamicMessage(Severity.ERROR, "There was an error parsing the node $NODE.");

	/** Error: Node missing arg for builder */
	public static final DynamicMessage ERROR_BUILDER_MISSING_ARG = new DynamicMessage(Severity.ERROR, "Missing argument for $NODE in $CONTEXT.");
	/** Error: Node argument not a valid integer */
	public static final DynamicMessage ERROR_BUILDER_MALFORMED_INT = new DynamicMessage(Severity.ERROR, "$NODE argument in $CONTEXT is not a valid integer.");
	/** Error: Node argument not a valid long */
	public static final DynamicMessage ERROR_BUILDER_MALFORMED_LONG = new DynamicMessage(Severity.ERROR, "$NODE argument in $CONTEXT is not a valid long integer.");
	/** Error: Node argument not a valid double */
	public static final DynamicMessage ERROR_BUILDER_MALFORMED_REAL = new DynamicMessage(Severity.ERROR, "$NODE argument in $CONTEXT is not a real number.");

	/** Warning: Node is a root node and should not be written */
	public static final DynamicMessage WARN_NODE_WRITE_ROOT = new DynamicMessage(Severity.WARN, "$NODE is a root node and should not be written.");

	/** Warning: Node argument is out of the expected range for a natural number */
	public static final DynamicMessage WARN_BUILDER_NATURAL_OUT_OF_BOUNDS = new DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT should be a natural number, but is less than 0.");
	/** Warning: Node argument is out of the expected range for a random roll */
	public static final DynamicMessage WARN_BUILDER_ROLL_OUT_OF_BOUNDS = new DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT is either too large or too small to be a valid default random roll.");
	/** Warning: Node argument is out of the expected range for a swizzle number */
	public static final DynamicMessage WARN_BUILDER_SWIZZLE_OUT_OF_BOUNDS = new DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT is either too large or too small to be a valid swizzle number.");

	/** Warning: Node argument is out of the expected range for a positive real number */
	public static final DynamicMessage WARN_BUILDER_POSREAL_OUT_OF_BOUNDS = new DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT should non-negative, but is less than 0.");
	/** Warning: Node argument is out of the expected range 0...1 */
	public static final DynamicMessage WARN_BUILDER_SMALLREAL_OUT_OF_BOUNDS = new DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT is outside the expected range of 0 to 1 inclusive.");



	// MARK: Error Count
	private static int errorCount = 0;
	
	/**
	 * Mutator method to reset the error count to 0.
	 */
	public static void resetErrorCount() {
		errorCount = 0;
	}
	
	/**
	 * Mutator method to increment the error count by 1.
	 */
	public static void countError() {
		errorCount++;
	}


	/**
	 * Getter: Returns the number of errors currently tracked.
	 * @return {@link #errorCount}
	 */
	public static int getErrorCount() {
		return errorCount;
	}



	// MARK: Message
	/**
	 * A class serving as a template for messages that can be logged.
	 * Each different type of message should be a class extending this one,
	 * and each should implement a {@code log()} method, although arguments may
	 * differ between different classes.
	 */
	public static abstract class Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public Message(
			Logger.Severity severity,
			String content
		) {
			setSeverity(severity);
			setContent(content);
		}

		/** The severity of this message. */
		private Logger.Severity severity;

		/** The content of this message. */
		private String content;



		/**
		 * Prints the appropriate ANSI escape code for the severity of this message.
		 */
		public void formatOn(PrintStream stream) {
			switch(severity) {
				case FATAL:
					stream.print(Logger.BOLD);
				case ERROR:
					stream.print(Logger.RED);
					countError();
					break;
				case WARN:
					stream.print(Logger.YELLOW);
					break;
				case SUCCESS:
					stream.print(Logger.GREEN);
					break;
				default:
					stream.print(Logger.BLUE);
			}
		}



		/** Prints the prefix for this error message. */
		public void printPrefix(PrintStream stream) {
			switch(severity) {
				case FATAL:
					stream.print("Fatal Error: ");
					break;
				case ERROR:
					stream.print("Error: ");
					break;
				case WARN:
					stream.print("Warning: ");
					break;
				default:
					stream.print("");
			}
		}



		/**
		 * Ends the message, reseting formating. If this message is a fatal error, calling
		 * this method will also exit the program with an error code of 1.
		 */
		public void endMessage() {
			System.err.print(RESET);
			System.err.println();
			if (severity.equals(Severity.FATAL)) {
				System.exit(errorCount);
			}
		}



		/**
		 * Getter: Returns the severity of this message.
		 * @return {@link #severity}
		 */
		public Logger.Severity getSeverity() {
			return severity;
		}

		/**
		 * Setter: Changes the severity of this message.
		 * @param severity The new value for {@link #severity}.
		 */
		public void setSeverity(Logger.Severity severity) {
			this.severity = severity;
		}


		/**
		 * Getter: Returns the content of this message.
		 * @return {@link #content}
		 */
		public String getContent() {
			return content;
		}

		/**
		 * Setter: Changes the content of this message.
		 * @param content The new value for {@link #content}.
		 */
		public void setContent(String content) {
			this.content = content;
		}
	}



	// MARK: Generic Message
	/**
	 * A class representing a standalone message.
	 * 
	 * This class is deprecated and may be removed in a future update. Use {@link DynamicMessage} instead.
	 */
	@Deprecated
	public static class GenericMessage extends Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public GenericMessage(
			Logger.Severity severity,
			String content
		) {
			super(severity, content);
		}

		/** Prints this message to {@link System#err}. */
		public void log() {
			formatOn(System.err);
			printPrefix(System.err);
			System.err.print(getContent());
			endMessage();
		}
	}



	// MARK: File Message
	/**
	 * A class representing a message associated with a file.
	 * 
	 * This class is deprecated and may be removed in a future update. Use {@link DynamicMessage} instead.
	 */
	@Deprecated
	public static class FileMessage extends Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public FileMessage(
			Logger.Severity severity,
			String content
		) {
			super(severity, content);
		}


		/**
		 * Prints this message to {@link System#err}.
		 * @param file The file with an error.
		 */
		public void log(File file) {
			formatOn(System.err);
			printPrefix(System.err);
			String content = getContent();
			content = content.replace("$FILENAME", file.getName());
			content = content.replace("$FILEPATH", file.getAbsolutePath());
			System.err.print(content);
			endMessage();
		}
	}



	// MARK: Node Parse Message
	/**
	 * A class representing a message associated with a node being parsed or written.
	 * 
	 * This class is deprecated and may be removed in a future update. Use {@link DynamicMessage} instead.
	 */
	@Deprecated
	public static class NodeIOMessage extends Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public NodeIOMessage(
			Logger.Severity severity,
			String content
		) {
			super(severity, content);
		}


		/**
		 * Prints this message to {@link System#err}. If the attached node is a {@link LoadedNode},
		 * it includes the line and file it was loaded from.
		 * @param node The node with an error.
		 */
		public void log(DataNode node) {
			formatOn(System.err);
			printPrefix(System.err);
			String content = getContent();
			content = content.replace("$NODE", node.getName());
			System.err.print(content);
			if (node instanceof LoadedNode) {
				LoadedNode loaded = (LoadedNode) node;
				System.err.printf(" (line %d of %s)", loaded.getLine(), loaded.getFile());
			}
			endMessage();
		}
	}



	// MARK: Node Instantiation Message
	/**
	 * A class representing a message associated with a file.
	 * 
	 * This class is deprecated and may be removed in a future update. Use {@link DynamicMessage} instead.
	 */
	@Deprecated
	public static class NodeInstantiationMessage extends Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public NodeInstantiationMessage(
			Logger.Severity severity,
			String content
		) {
			super(severity, content);
		}


		/**
		 * Prints this message to {@link System#err}. If the attached node is a {@link LoadedNode},
		 * it includes the line and file it was loaded from.
		 * @param node The node with an error.
		 * @param context The object that was being instantiated when an error was encountered.
		 */
		public void log(DataNode node, String context) {
			formatOn(System.err);
			printPrefix(System.err);
			String content = getContent();
			content = content.replace("$NODE", node.getName());
			content = content.replace("$CONTEXT", context);
			System.err.print(content);
			if (node instanceof LoadedNode) {
				LoadedNode loaded = (LoadedNode) node;
				System.err.printf(" (line %d of %s)", loaded.getLine(), loaded.getFile());
			}
			endMessage();
		}
	}



	// MARK: Dynamic Message
	/**
	 * A class that automatically fills in data based on the classes of its arguments.
	 * 
	 * This class fulfills the behaviour of all other extant Message subclasses, and should be
	 * used instead of creating new subclasses if possible.
	 */
	public static class DynamicMessage extends Message {
		/**
		 * Sole constructor.
		 * @param severity The severity of this message.
		 * @param content The content of this message.
		 */
		public DynamicMessage(
			Logger.Severity severity,
			String content
		) {
			super(severity, content);
		}


		/**
		 * Prints this message (usually to {@link System#err}) after filling in all appropriate text
		 * replacements using the objects provided.
		 * @param data Data used to fill out the template. Can include strings, {@link DataNode}
		 * or {@link File} objects, or an alternate {@link PrintStream} to print to. If no PrintStream
		 * is provided, {@link System#err} is used. If a {@link LoadedNode} is provided, the first such
		 * node will be used to generate a line and file reference that is appended to the message.
		 */
		public void log(Object... data) {
			formatOn(System.err);
			printPrefix(System.err);			
			String content = getContent();

			int stringCount = 0;
			int nodeCount = 0;
			int fileCount = 0;
			LoadedNode loadedNode = null;
			PrintStream printStream = System.err;

			for (Object arg : data) {
				if (arg instanceof String) {
					if (stringCount == 0) content = content.replace("$CONTEXT", (String) arg);
					content = content.replace(String.format("$CONTEXT[%d]", stringCount), (String) arg);

					stringCount++;
				} else if (arg instanceof DataNode) {
					DataNode node = (DataNode) arg;

					if (nodeCount == 0) content = content.replace("$NODE", node.getName());
					content = content.replace(String.format("$NODE[%d]", stringCount), node.getName());

					if (nodeCount == 0) content = content.replace("$PARENT", node.getParent().getName());
					content = content.replace(String.format("$PARENT[%d]", stringCount), node.getParent().getName());

					nodeCount++;
				} else if (arg instanceof File) {
					File file = (File) arg;

					if (fileCount == 0) content = content.replace("$FILENAME", file.getName());
					content = content.replace(String.format("$FILENAME[%d]", stringCount), file.getName());

					if (fileCount == 0) content = content.replace("$FILEPATH", file.getAbsolutePath());
					content = content.replace(String.format("$FILEPATH[%d]", stringCount), file.getAbsolutePath());

					nodeCount++;
				} else if (arg instanceof PrintStream) {
					printStream = (PrintStream) arg;
				}

				if (arg instanceof LoadedNode && loadedNode == null) {
					loadedNode = (LoadedNode) arg;
				}
			}

			printStream.print(content);
			if (loadedNode != null) {
				printStream.printf(" (line %d of %s)", loadedNode.getLine(), loadedNode.getFile());
			}
			endMessage();
		}
	}
}
