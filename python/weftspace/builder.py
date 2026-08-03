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

"""
A class of utility methods designed to allow easy conversion from nodes to objects.
"""

from abc import ABC

from .builder_error import BuilderError
from .data_node import DataNode

class Builder(ABC):
	"""A class of utility methods designed to allow easy conversion from nodes to objects."""

	@classmethod
	def build_string(cls, node: DataNode, arg: int) -> str:
		"""
		Takes an argument from a node and returns it as a string, handling any
		exceptions that occur.
		"""
		try:
			return node.args[arg]
		except IndexError as exc:
			raise BuilderError(f"No argument at position {arg}.", node) from exc



	@classmethod
	def build_int(cls, node: DataNode, arg: int) -> int:
		"""
		Takes an argument from a node and returns it as an integer, handling any
		exceptions that occur.
		"""
		try:
			return int(node.args[arg])
		except ValueError as exc:
			raise BuilderError(
				f"The string \"{node.args[arg]}\" could not be parsed to an integer.", node) from exc
		except IndexError as exc:
			raise BuilderError(f"No argument at position {arg}.", node) from exc



	@classmethod
	def build_float(cls, node: DataNode, arg: int) -> float:
		"""
		Takes an argument from a node and returns it as a float, handling any
		exceptions that occur.
		"""
		try:
			return float(node.args[arg])
		except ValueError as exc:
			raise BuilderError(
				f"The string \"{node.args[arg]}\" could not be parsed to a float.", node) from exc
		except IndexError as exc:
			raise BuilderError(f"No argument at position {arg}.", node) from exc




	@classmethod
	def search(cls, node: DataNode, scope: DataNode) -> DataNode | None:
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
