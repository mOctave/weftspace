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

package moctave.esdf;

/** A class of utility methods designed to allow easy conversion from nodes to objects. */
public abstract class Builder {
	/**
	 * Takes an argument from a node and returns it as a string, handling any
	 * exceptions that occur.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @return The argument, as a string.
	 */
	public static String buildString(DataNode node, int arg, String context) {
		try {
			return node.getArg(arg);
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		}

		return null;
	};



	/**
	 * Takes an argument from a node and returns it as an integer, handling any
	 * exceptions that occur.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @return The argument, as a integer.
	 */
	public static int buildInt(DataNode node, int arg, String context) {
		try {
			return Integer.parseInt(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		} catch (NumberFormatException e) {
			Logger.ERROR_BUILDER_MALFORMED_INT.log(node, context);
		}

		return 0;
	};



	/**
	 * An enum storing a list of potential integer types, for use in
	 * {@link #buildInt(DataNode, int, String, IntType)}.
	 */
	public static enum IntType {
		/**
		 * A default integer, between {@code Integer.MIN_VALUE} and
		 * {@code Integer.MAX_VALUE} inclusive.
		 */
		STANDARD,
		/** A non-negative integer, between {@code 0} and {@code Integer.MAX_VALUE} inclusive. */
		NATURAL,
		/**
		 * An integer in the range that could be rolled using Endless Sky's default
		 * {@code random} function, between {@code 0} and {@code 99} inclusive.
		 */
		POSSIBLE_ROLL,
		/**
		 * An integer in the valid range for Endless Sky swizzles, between
		 * {@code 0} and {@code 28} inclusive.
		 */
		SWIZZLE,
	}


	/**
	 * Takes an argument from a node and returns it as an integer. The special type of the value
	 * may be defined; this will have no impact on the value returned by this method, but it will
	 * cause a warning to be thrown if the final value does not conform to the intended pattern.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @param type The intended type of value to be returned. If the final value does not match, a
	 * warning will be thrown.
	 * @return The argument, as a integer.
	 */
	public static int buildInt(DataNode node, int arg, String context, IntType type) {
		try {
			int value = Integer.parseInt(node.getArg(arg));
			switch (type) {
				case NATURAL:
					if (value < 0)
						Logger.WARN_BUILDER_NATURAL_OUT_OF_BOUNDS.log(node, context);
					break;
				case POSSIBLE_ROLL:
					if (value < 0 || value > 99)
						Logger.WARN_BUILDER_ROLL_OUT_OF_BOUNDS.log(node, context);
					break;
				case SWIZZLE:
					if (value < 0 || value > 28)
						Logger.WARN_BUILDER_SWIZZLE_OUT_OF_BOUNDS.log(node, context);
					break;
				default:
					break;
			}
			return value;
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		} catch (NumberFormatException e) {
			Logger.ERROR_BUILDER_MALFORMED_INT.log(node, context);
		}

		return 0;
	};



	/**
	 * Takes an argument from a node and returns it as a double, handling any
	 * exceptions that occur.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @return The argument, as a double.
	 */
	public static double buildDouble(DataNode node, int arg, String context) {
		try {
			return Double.parseDouble(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		} catch (NumberFormatException e) {
			Logger.ERROR_BUILDER_MALFORMED_REAL.log(node, context);
		}

		return 0.;
	};



	/**
	 * An enum storing a list of potential double types, for use in
	 * {@link #buildDouble(DataNode, int, String, DoubleType)}.
	 */
	public static enum DoubleType {
		/** A default double. */
		STANDARD,
		/** A non-negative double n such that {@code n ≥ 0.}. */
		POSREAL,
		/**
		 * A double in the range {@code 0.} to {@code 1.} inclusive, as used in colour definitions.
		 */
		SMALLREAL,
	}


	/**
	 * Takes an argument from a node and returns it as an double. The special type of the value
	 * may be defined; this will have no impact on the value returned by this method, but it will
	 * cause a warning to be thrown if the final value does not conform to the intended pattern.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @param type The intended type of value to be returned. If the final value does not match, a
	 * warning will be thrown.
	 * @return The argument, as a double.
	 */
	public static double buildDouble(DataNode node, int arg, String context, DoubleType type) {
		try {
			double value = Double.parseDouble(node.getArg(arg));
			switch (type) {
				case POSREAL:
					if (value < 0.)
						Logger.WARN_BUILDER_POSREAL_OUT_OF_BOUNDS.log(node, context);
					break;
				case SMALLREAL:
					if (value < 0. || value > 1.)
						Logger.WARN_BUILDER_SMALLREAL_OUT_OF_BOUNDS.log(node, context);
					break;
				default:
					break;
			}
			return value;
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		} catch (NumberFormatException e) {
			Logger.ERROR_BUILDER_MALFORMED_INT.log(node, context);
		}

		return 0.;
	};



	/**
	 * Takes an argument from a node and returns it as a long int, handling any
	 * exceptions that occur.
	 * @param node The node to access.
	 * @param arg The index of the argument to convert.
	 * @param context The name of the object being made from the node.
	 * @return The argument, as a long int.
	 */
	public static long buildlong(DataNode node, int arg, String context) {
		try {
			return Long.parseLong(node.getArg(arg));
		} catch (IndexOutOfBoundsException e) {
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context);
		} catch (NumberFormatException e) {
			Logger.ERROR_BUILDER_MALFORMED_LONG.log(node, context);
		}

		return 0l;
	};
}
