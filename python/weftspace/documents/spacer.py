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

import os

from .document_component import DocumentComponent

"""
One or more consecutive empty lines in a document.
"""

class Spacer(DocumentComponent):
	"""One or more consecutive empty lines in a document."""

	# MARK: Properties
	_size: int

	@property
	def size(self) -> int:
		"""How many lines this spacer takes up."""
		return self._size

	@size.setter
	def size(self, size: int) -> None:
		"""Changes the size of this spacer."""
		self._size = size



	# MARK: Constructor
	def __init__(self, size: int) -> None:
		"""Creates a new spacer with the given size."""
		super().__init__()
		self._size = size



	# MARK: Methods
	def as_line(self, indentation: int) -> str:
		"""Converts this spacer into lines for output."""
		s: str = ""
		for _ in range(self._size - 1):
			s += os.linesep

		return s
