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

from abc import ABC, abstractmethod

"""
An interface consisting of common attributes of document components.
"""

class DocumentComponent(ABC):
	"""An interface consisting of common attributes of document components."""
	@abstractmethod
	def as_line(self, indentation: int) -> str:
		"""Converts this document element into a line of text with the given indentation level."""
