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

from io import TextIOWrapper

from .data_node import DataNode
from .logger import Logger

class DataWriter:
	# MARK: Constructor
	def __init__(self, file: str):
		"""Sole constructor."""
		self.file = file

	# MARK: Properties
	_file: str

	@property
	def file(self):
		"""The file this DataWriter is writing to."""
		return self._file
	
	@file.setter
	def file(self, file: str):
		"""Changes the file being written to."""
		self._file = file
	


	_io_wrapper: TextIOWrapper

	@property
	def io_wrapper(self):
		"""The TextIOWrapper used to write to this DataWriter's file."""
		return self._io_wrapper
	
	# io_wrapper has no setter, and should be created with open()

	

	# MARK: Methods
	def open(self):
		"""Opens the TextIOWrapper for this DataWriter."""
		self._io_wrapper = open(self.file, "a")



	def close(self):
		"""Closes the TextIOWrapper for this DataWriter."""
		self.io_wrapper.close()
	


	def write(self, node: DataNode):
		"""Writes a node to a file with no indent."""
		self.write_indented(node, 0)

	


	def write_indented(self, node: DataNode, indent: int):
		"""Writes a node to the file."""
		for _ in range(indent):
			self.io_wrapper.write("\t")
			
		self.io_wrapper.write(DataWriter.node_to_line(node))
		self.io_wrapper.write("\n")
		self.io_wrapper.flush()

		for child in node.children:
			self.write_indented(child, indent + 1)
			
		if indent == 0:
			self.io_wrapper.write("\n")



	@classmethod
	def node_to_line(cls, node: DataNode):
		"""
		Represents a node as a line to be saved to a file.
		Not to be confused with DataNode.__str__
		"""
		if node.flag == DataNode.Flag.ROOT:
			Logger.WARN_NODE_WRITE_ROOT.log(node)
		
		s: str = ""

		if (node.flag == DataNode.Flag.ADD):
			s += "add "
		elif (node.flag == DataNode.Flag.REMOVE):
			s += "remove "
		
		s += DataWriter.quote_word(node.name) + " "

		for arg in node.args:
			s += DataWriter.quote_word(arg) + " "
		
		return s.strip()



	@classmethod
	def quote_word(cls, word: str):
		"""
		Puts quotes around text, adapting between no quotes, double quotes, and backticks as
		necessary. Does not conform to Endless Sky human readability conventions, but uses the
		simplest possible quotes for a given word.
		"""
		if " " in word:
			if "\"" in word:
				return "`" + word + "`"
			else:
				return "\"" + word + "\""
		else:
			return word
