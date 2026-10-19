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

from collections import deque
import os

from .comment import Comment
from .document_component import DocumentComponent
from .document_node import DocumentNode
from .spacer import Spacer

from ..data_node import DataNode
from ..data_reader import DataReader
from ..reader_error import ReaderError

"""
A file consisting of nodes, blank lines, and comments.
"""

class Document:
	"""A file consisting of nodes, blank lines, and comments."""

	# Properties
	_root: DataNode

	@property
	def root(self) -> DataNode:
		"""The root node of this document."""
		return self._root

	_components: list[DocumentComponent]

	@property
	def components(self) -> list[DocumentComponent]:
		"""The components of this document."""
		return self._components



	# MARK: Constructor
	def __init__(self, root: DataNode, source: str):
		self._components = []
		self._root = root

		try:
			node_stack: deque[DocumentNode] = deque()
			current_node: DocumentNode | None = None

			indent: int = 0
			indent_depths: deque[int] = deque()

			consecutive_blanks: int = 0

			indent_depths.append(0)

			with open(source, "r", encoding="UTF-8") as f:
				for line in f:
					if (line.isspace()):
						consecutive_blanks += 1
						continue

					if (consecutive_blanks > 0):
						if len(node_stack) == 0:
							self._components.append(Spacer(consecutive_blanks))

						else:
							node_stack[-1].components.append(Spacer(consecutive_blanks))

					if self.__check_and_add_comment(line, node_stack):
						continue

					indent = DataReader.count_leading_whitespace(line)

					if indent > indent_depths[-1] and current_node != None:
						node_stack.append(current_node)
						indent_depths.append(indent)
					else:
						while indent_depths[-1] > indent:
							node_stack.pop()
							indent_depths.pop()

					current_node = self.__make_node(line)

					if len(node_stack) == 0 and current_node != None:
						self.components.append(current_node)
						root.children.append(current_node)
						current_node.parent = root
					elif current_node != None:
						parent = node_stack[-1]
						parent.add_child(current_node)

		except FileNotFoundError as exc:
			raise ReaderError(f"No such file as {source}") from exc



	# MARK: Methods
	def __check_and_add_comment(self, line: str, node_stack: deque[DocumentNode]) -> bool:
		"""Checks if the given line is a comment, and handles it if it is."""
		if line.startswith("#"):
			c: Comment = Comment(line[1:], True)
			if len(node_stack) == 0:
				self.components.append(c)
			else:
				node_stack[-1].components.append(c)

			return True

		if line.strip().startswith("#"):
			c: Comment = Comment(line[1:], False)
			if len(node_stack) == 0:
				self.components.append(c)
			else:
				node_stack[-1].components.append(c)

			return True

		return False



	def __make_node(self, line: str) -> DocumentNode | None:
		"""Parses a single line and converts it to a node."""
		trimmed_line: str = line.strip()
		data: list[str] = []
		split_on: str = " "
		current_item: str = ""
		is_empty: bool = True
		comment: str | None = None

		for c in trimmed_line:
			if comment != None:
				comment += c
				continue

			if is_empty and not c.isspace():
				is_empty = False
				if c == "\"":
					# Item will end with a double quote, ignore this one.
					split_on = "\""
					continue
				if c == "`":
					# Item will end with a backtick, ignore this one.
					split_on = "`"
					continue
				else:
					# Item will end when the word it's in does.
					split_on = " "

			elif is_empty:
				continue

			if c == split_on:
				# Found end of item, add it to the list and start on the next one
				data.append(current_item)
				current_item = ""
				is_empty = True
				continue

			if c == "#" and split_on == " ":
				if comment == None:
					comment = ""
				continue

			# Add to the current item
			current_item += c

		# Items can end at the end of the line, too
		if len(current_item) == 0:
			data.append(current_item)

		if len(data) == 0:
			return None

		# Treat the first entry as the node name, and everything else as args.
		node_name: str = data.pop(0)

		node = DocumentNode.create(DataNode(node_name, None, data, []))
		node.comment = comment
		return node



	def write_out(self, dest: str) -> None:
		"""Writes the contents of this document out to the given file."""
		with open(dest, "a", encoding="UTF-8") as writer:
			for component in self.components:
				writer.write(component.as_line(0))
				writer.write(os.linesep)
