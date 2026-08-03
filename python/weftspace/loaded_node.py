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

	# MARK: Properties
	_line: int

	@property
	def line(self) -> int:
		"""The line this node was parsed from."""
		return self._line
	
	@line.setter
	def line(self, line: int) -> None:
		"""Changes the line number associated with this node."""
		self._line = line


	_file: str

	@property
	def file(self) -> str:
		"""The filename of the file this node was parsed from."""
		return self._file
	
	@file.setter
	def file(self, file: str) -> None:
		"""Changes the file associated with this node."""
		self._file = file



	# MARK: Constructor
	def __init__(
		self,
		name: str,
		parent: DataNode | None,
		args: list[str],
		children: list[DataNode],
		line: int,
		file: str
	) -> None:
		"""
		Sole constructor.
		"""
		super().__init__(name, parent, args, children)
		self.line = line
		self.file = file
