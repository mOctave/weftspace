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

from .data_node import DataNode

class LoadedNode(DataNode):
	"""A subclass of a node that is attached to a specific line and file, for use in debugging."""

	# MARK: Constructor
	def __init__(
		self,
		name: str,
		flag: DataNode.Flag,
		parent: DataNode | None,
		args: list[str],
		children: list[DataNode],
		line: int,
		file: str
	):
		"""
		Sole constructor.
		"""
		super().__init__(name, flag, parent, args, children)
		self.line = line
		self.file = file



	# MARK: Properties
	_line: int

	@property
	def line(self):
		"""The line this node was parsed from."""
		return self._name
	
	@line.setter
	def line(self, line: int):
		"""Changes the line number associated with this node."""
		self._line = line


	_file: str

	@property
	def file(self):
		"""The filename of the file this node was parsed from."""
		return self._name
	
	@file.setter
	def file(self, file: str):
		"""Changes the file associated with this node."""
		self._file = file
