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

import org.jspecify.annotations.*;

/** A class of utility methods designed to allow easy conversion from nodes to objects. */
public abstract class Builder {
	/**
	 * Takes an argument from a node and returns it as a string, throwing a
	 * BuilderException if there is an error that prevents parsing.
	 * 
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @return The argument, as a string.
	 */
	public static @NonNull String buildString(@NonNull DataNode node, int arg) throws BuilderException {
		try {
			return node.getArg(arg);
		} catch (IndexOutOfBoundsException e) {
			throw new BuilderException(String.format("No argument at position %d.", arg), node);
		}
	}



	/**
	 * Takes an argument from a node and returns it as an integer, throwing a
	 * BuilderException if there is an error that prevents parsing.
	 * 
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @return The argument, as a integer.
	 */
	public static int buildInt(@NonNull DataNode node, int arg) throws BuilderException {
		try {
			return Integer.parseInt(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			throw new BuilderException(String.format("No argument at position %d.", arg), node);
		} catch (NumberFormatException e) {
			throw new BuilderException(
				String.format("The string \"%s\" could not be parsed to an integer.", node.getArg(arg)), node);
		}
	}



	/**
	 * Takes an argument from a node and returns it as a double, throwing a
	 * BuilderException if there is an error that prevents parsing.
	 * 
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @return The argument, as a double.
	 */
	public static double buildDouble(@NonNull DataNode node, int arg) throws BuilderException {
		try {
			return Double.parseDouble(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			throw new BuilderException(String.format("No argument at position %d.", arg), node);
		} catch (NumberFormatException e) {
			throw new BuilderException(
				String.format("The string \"%s\" could not be parsed to a double.", node.getArg(arg)), node);
		}
	}



	/**
	 * Takes an argument from a node and returns it as a long int, throwing a
	 * BuilderException if there is an error that prevents parsing.
	 * 
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @return The argument, as a long int.
	 */
	public static long buildLong(@NonNull DataNode node, int arg) throws BuilderException {
		try {
			return Long.parseLong(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			throw new BuilderException(String.format("No argument at position %d.", arg), node);
		} catch (NumberFormatException e) {
			throw new BuilderException(
				String.format("The string \"%s\" could not be parsed to a long int.", node.getArg(arg)), node);
		}
	}



	/**
	 * Takes a node and uses it as a key to search the scope for a node
	 * with a matching name and first argument.
	 * 
	 * @param node The node to use as a search key.
	 * @param scope The node whose children should be searched.
	 * @return The node with the same name, or {@code null} if none exist.
	 */
	public static @Nullable DataNode search(@NonNull DataNode node, @NonNull DataNode scope) {
		try {
			for (DataNode child : scope.getChildren()) {
				if (
					node.getName().equals(child.getName())
					&& node.getArg(0).equals(child.getArg(0))
				) {
					return child;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// This is not necessarily an error, so no error message is printed.
		}

		return null;
	}
}
