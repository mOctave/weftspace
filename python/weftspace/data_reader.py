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

from collections import deque
from enum import Enum
from warnings import deprecated

from .data_node import DataNode
from .loaded_node import LoadedNode
from .logger import Logger

class DataReader:
	"""A class which reads data from a file and stores it in a node tree."""

	# MARK: Constants
	class Option(Enum):
		"""Special flags that can be used when parsing."""

		IGNORE_NODE_FLAGS = 1
		"""Do not apply node flags."""



	# MARK: Constructor
	def __init__(self, file: str, root: DataNode):
		self.file = file
		self.root = root



	# MARK: Properties
	_file: str

	@property
	def file(self):
		"""The file this DataReader is parsing."""
		return self._file
	
	@file.setter
	def file(self, file: str):
		"""Changes the file being parsed."""
		self._file = file

	_root: DataNode

	@property
	def root(self):
		"""The node this DataReader is using as its root."""
		return self._root
	
	@root.setter
	def root(self, root: DataNode):
		"""Changes the root node being added to."""
		self._root = root



	# MARK: Methods
	def parse(self, *args: Option) -> None:
		"""Parses the file associated with this object, and stores all nodes in the tree."""
		ignore_node_flags: bool = DataReader.Option.IGNORE_NODE_FLAGS in args
		try:
			line_number: int = 0
			node_stack: deque[DataNode] = deque()
			current_node: DataNode | None = None

			indent: int = 0
			indent_depths: deque[int] = deque()
			expected_indent_string: str | None = None

			indent_depths.append(0)

			with open(self.file, "r") as f:
				for line in f:
					line_number += 1
					if len(line.split("#")) == 0:
						if line.isspace() or not line:
							continue
					else:
						tl: str = line.split("#")[0]
						if tl.isspace() or not tl:
							continue
				
					indent = DataReader.count_leading_whitespace(line)
					indent_substring: str = DataReader.get_indent_substring(line, indent_depths[-1])

					if (expected_indent_string == None and len(indent_substring) > 0):
						expected_indent_string = indent_substring
						if (" " in expected_indent_string and "\t" in expected_indent_string):
							Logger.WARN_MIXED_WHITESPACE.log(f)


					if indent > indent_depths[-1] and current_node != None:
						if expected_indent_string != indent_substring:
							Logger.WARN_MIXED_WHITESPACE.log(f)

						node_stack.append(current_node)
						indent_depths.append(indent)
					else:
						while indent_depths[-1] > indent:
							node_stack.pop()
							indent_depths.pop()
					
					current_node = self.make_node(line, line_number, not ignore_node_flags)

					if len(node_stack) == 0 and current_node != None:
						self.root.children.append(current_node)
						current_node.parent = self.root
					elif current_node != None:
						parent: DataNode = node_stack[-1]
						parent.children.append(current_node)
						current_node.parent = parent
				
				f.close()

		except FileNotFoundError:
			Logger.ERROR_FILE_DNE.log(self.file)



	def make_node(self, line: str, number: int, check_for_flags: bool) -> LoadedNode | None:
		"""Parses a single line and converts it to a node."""
		stripped_line: str = line.strip()
		data: list[str] = []
		split_on: str = " "
		current_item: str = ""
		is_empty: bool = True

		for char in stripped_line:
			if is_empty and not char.isspace():
				is_empty = False
				if char == "\"":
					# Item will end with a double quote, ignore this one.
					split_on = "\""
					continue
				elif char == "`":
					# Item will end with a backtick, ignore this one.
					split_on = "`"
					continue
				else:
					# Item will end when the current word ends.
					split_on = " "
			elif is_empty:
				continue

			if char == split_on:
				# Found end of item, add it to the list and start on the next one.
				data.append(current_item)
				current_item = ""
				is_empty = True
				continue
			elif char == "#" and split_on == " ":
				# Ignore everything after a comment.
				break

			# Add to the current item.
			current_item += char
		
		# Items can end at the end of the line, too.
		if current_item:
			data.append(current_item)
		
		if len(data) == 0:
			return None
		
		# Treat the first entry as the node name, and everything else as args.
		node_name: str = data.pop(0)

		flag: DataNode.Flag = DataNode.Flag.NORMAL

		# Check for flags
		if check_for_flags:
			if node_name == "add":
				flag = DataNode.Flag.ADD
				node_name = data.pop(0)
			elif node_name == "remove":
				flag = DataNode.Flag.REMOVE
				node_name = data.pop(0)
		
		return LoadedNode(node_name, flag, None, data, [], number, self.file)



	@classmethod
	def get_indent_substring(cls, line: str, depth: int) -> str:
		"""
		Gets the substring which appears to be used for indentation, starting
		with the character at a given depth.
		"""
		i: int = depth
		s: str = ""
		while (i < len(line) and (line[i] == "\t" or line[i] == " ")):
			s += line[i]
			i += 1
		
		return s



	@classmethod
	def count_leading_whitespace(cls, line: str) -> int:
		"""Counts the number of leading tab or space characters on a line."""
		i: int = 0
		while (i < len(line) and (line[i] == "\t" or line[i] == " ")):
			i += 1
		
		return i



	@classmethod
	@deprecated("Use count_leading_whitespace() instead")
	def count_leading_tabs(cls, line: str) -> int:
		"""
		Deprecated method included to support programs written expecting legacy
		whitespace handling. Calls count_leading_whitespace(), see that
		method for documentation.
		"""
		i: int = 0
		while (i < len(line) and line[i] == "\t"):
			i += 1
		
		return i
