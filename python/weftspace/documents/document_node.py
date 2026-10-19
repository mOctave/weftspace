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
A type of node linked directly to a document.
"""

import os

from .document_component import DocumentComponent

from ..data_node import DataNode
from ..data_writer import DataWriter

class DocumentNode(DataNode, DocumentComponent):
	"""A type of node linked directly to a document."""

	# MARK: Properties
	_components: list[DocumentComponent]

	@property
	def components(self) -> list[DocumentComponent]:
		"""The document components contained within this node."""
		return self._components

	_comment: str | None

	@property
	def comment(self) -> str | None:
		"""An optional comment string to place after the text in this node."""
		return self._comment

	@comment.setter
	def comment(self, comment: str | None) -> None:
		"""Changes this node's trailing comment."""
		self._comment = comment



	# MARK: Constructors
	def __init__(self, name: str, parent: DataNode | None,
		args: list[str], children: list[DataNode]) -> None:
		"""A constructor that creates a new document node given the standard arguments."""
		super().__init__(name, parent, args, children)
		self._components = []


	@staticmethod
	def create(node: DataNode) -> "DocumentNode":
		"""
		A constructor that creates and returns a new document node given an existing data node.
		This constructor may share data with the original node.
		"""
		this: DocumentNode = DocumentNode(node.name, None, node.args, [])

		this.name = node.name
		this.args = node.args
		for child in node.children:
			this.add_child(child)

		return this



	# MARK: Methods
	def as_line(self, indentation: int) -> str:
		"""
		Converts this node and its children into lines of text with the given
		indentation level.
		"""

		line: str = ""

		for _ in range(indentation):
			line += "\t"

		line += DataWriter.node_to_line(self)

		if self._comment is not None:
			line += " #" + self._comment

		for component in self._components:
			line += os.linesep + component.as_line(indentation + 1)

		return line


	def add_child(self, child: DataNode) -> None:
		"""
		Mutator method to add a node to the tree as a child of this node
		AND as a subcomponent.
		"""
		if isinstance(child, DocumentNode):
			self.children.append(child)
			self.components.append(child)
			child.parent = self
		else:
			converted: DocumentNode = DocumentNode.create(child)
			self.children.append(converted)
			self.components.append(converted)
			converted.parent = self
