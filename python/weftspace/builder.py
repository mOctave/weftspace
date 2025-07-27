# Copyright (c) 2025 by mOctave
#
# This program is free software: you can redistribute it and/or modify it under the
# terms of the GNU Affero General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
# PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License along with
# this program. If not, see <https://www.gnu.org/licenses/>.

from abc import ABC
from enum import Enum

from .data_node import DataNode
from .logger import Logger

class Builder(ABC):
	"""A class of utility methods designed to allow easy conversion from nodes to objects."""

	@classmethod
	def build_string(cls, node: DataNode, arg: int, context: str):
		"""
		Takes an argument from a node and returns it as a string, handling any
		exceptions that occur.
		"""
		try:
			return node.args[arg]
		except IndexError:
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context)
		
		return None



	@classmethod
	def build_int(cls, node: DataNode, arg: int, context: str):
		"""
		Takes an argument from a node and returns it as an integer, handling any
		exceptions that occur.
		"""
		try:
			return int(node.args[arg])
		except ValueError:
			Logger.ERROR_BUILDER_MALFORMED_INT.log(node, context)
		except IndexError:
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context)
		
		return 0



	class IntType(Enum):
		"""An enum storing a list of potential integer types, for use in build_spec_int()"""
		
		STANDARD = 0
		"""A default unbounded integer."""

		NATURAL = 1
		"""A non-negative integer with no upper bound."""

		POSSIBLE_ROLL = 2
		"""
		An integer in the range that could be rolled using Endless Sky's default
		random function, between 0 and 99 inclusive.
		"""



	@classmethod
	def build_spec_int(cls, node: DataNode, arg: int, context: str, type: IntType):
		"""
		Takes an argument from a node and returns it as an integer. The special type of the value
		may be defined; this will have no impact on the value returned by this method, but it will
		cause a warning to be thrown if the final value does not conform to the intended pattern.
		"""
		try:
			value: int = int(node.args[arg])
			match (type):
				case Builder.IntType.NATURAL:
					if value < 0:
						Logger.WARN_BUILDER_NATURAL_OUT_OF_BOUNDS.log(node, context)
				case Builder.IntType.POSSIBLE_ROLL:
					if value < 0 or value > 99:
						Logger.WARN_BUILDER_ROLL_OUT_OF_BOUNDS.log(node, context)
				case _:
					pass
			
			return value
		except ValueError:
			Logger.ERROR_BUILDER_MALFORMED_INT.log(node, context)
		except IndexError:
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context)
		
		return 0



	@classmethod
	def build_float(cls, node: DataNode, arg: int, context: str):
		"""
		Takes an argument from a node and returns it as a float, handling any
		exceptions that occur.
		"""
		try:
			return float(node.args[arg])
		except ValueError:
			Logger.ERROR_BUILDER_MALFORMED_REAL.log(node, context)
		except IndexError:
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context)
		
		return 0.



	class FloatType(Enum):
		"""An enum storing a list of potential integer types, for use in build_spec_int()"""
		
		STANDARD = 0
		"""A default unbounded float."""

		POS_REAL = 1
		"""A non-negative float with no upper bound."""

		SMALL_REAL = 2
		"""
		A float in the range 0. to 1. inclusive, as used in colour definitions.
		"""



	@classmethod
	def build_spec_float(cls, node: DataNode, arg: int, context: str, type: FloatType):
		"""
		Takes an argument from a node and returns it as a float. The special type of the value
		may be defined; this will have no impact on the value returned by this method, but it will
		cause a warning to be thrown if the final value does not conform to the intended pattern.
		"""
		try:
			value: float = float(node.args[arg])
			match (type):
				case Builder.FloatType.POS_REAL:
					if value < 0:
						Logger.WARN_BUILDER_POSREAL_OUT_OF_BOUNDS.log(node, context)
				case Builder.FloatType.SMALL_REAL:
					if value < 0. or value > 1.:
						Logger.WARN_BUILDER_SMALLREAL_OUT_OF_BOUNDS.log(node, context)
				case _:
					pass
			
			return value
		except ValueError:
			Logger.ERROR_BUILDER_MALFORMED_REAL.log(node, context)
		except IndexError:
			Logger.ERROR_BUILDER_MISSING_ARG.log(node, context)
		
		return 0
	


	@classmethod
	def search(cls, node: DataNode, scope: DataNode, context: str):
		"""
		Takes a node and uses it as a key to search the scope for a node
		with a matching name and first argument, handling any exceptions that occur.
		"""
		try:
			for child in scope.children:
				if node.name == child.name and node.args[0] == child.args[0]:
					return child
		except IndexError:
			# This is not necessarily an error, so no error message is printed.
			pass
		
		return None
