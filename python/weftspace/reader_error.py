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
An exception thrown by the data reader.
"""

from __future__ import annotations

class ReaderError(Exception):
	"""An exception thrown by the data reader."""

	# MARK: Properties
	_message: str

	@property
	def message(self):
		"""The error message for this error."""
		return self._message

	@message.setter
	def message(self, message: str):
		"""Changes the error message associated with this error."""
		self._message = message



	# MARK: Constructor
	def __init__(self, message: str):
		"""Sole constructor. Takes a message."""
		super().__init__(message)
		self._message = message
