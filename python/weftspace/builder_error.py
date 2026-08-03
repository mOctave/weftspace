# Copyright (c) 2026 by mOctave
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
An exception thrown by the builder.
"""

from __future__ import annotations

import os

from .data_node import DataNode
from .data_writer import DataWriter

class BuilderError(Exception):
	"""An exception thrown by the builder."""

	# MARK: Properties
	_message: str

	@property
	def message(self) -> str:
		"""The error message for this error."""
		return self._message

	@message.setter
	def message(self, message: str) -> None:
		"""Changes the error message associated with this error."""
		self._message = message



	_node: DataNode

	@property
	def node(self) -> DataNode:
		"""The node that caused this error."""
		return self._node

	@node.setter
	def node(self, node: DataNode) -> None:
		"""Changes the node associated with this error."""
		self._node = node


	# MARK: Constructor
	def __init__(self, message: str, node: DataNode) -> None:
		"""Sole constructor. Takes a message and the data node that caused the error."""
		super().__init__(message)
		self._message = message
		self._node = node


	# MARK: Methods
	def represent(self) -> str:
		"""
		Represents this exception as a long-form error message with a trace of
		the node that caused it.
		"""
		node_descriptions: list[str] = []
		current_node: DataNode | None = self.node
		while current_node is not None:
			node_descriptions.insert(0, DataWriter.node_to_line(current_node))
			current_node = current_node.parent

		representation: str = "Builder Exception: " + self.message
		i: int = 0
		for desc in node_descriptions:
			representation += os.linesep
			for _ in range(i):
				representation += "\t"
			representation += desc
			i += 1

		return representation
