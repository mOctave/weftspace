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
A full-line comment in a document.
"""

from .document_component import DocumentComponent

class Comment(DocumentComponent):
	"""A full-line comment in a document."""

	# MARK: Properties
	_text: str

	@property
	def text(self) -> str:
		"""The text of this comment."""
		return self._text

	@text.setter
	def text(self, text: str) -> None:
		"""Changes the text associated with this comment."""
		self._text = text


	_ignores_indentation: bool

	@property
	def ignores_indentation(self) -> bool:
		"""Whether this comment ignores indentation when represented as part of a node tree."""
		return self._ignores_indentation

	@ignores_indentation.setter
	def ignores_indentation(self, ignores_indentation: bool) -> None:
		"""Changes whether this comment ignores indentation."""
		self._ignores_indentation = ignores_indentation



	# MARK: Constructor
	def __init__(self, text: str, ignores_indentation: bool) -> None:
		"""Sole constructor. Creates a new comment with the given text and indentation policy."""
		super().__init__()
		self._text = text
		self._ignores_indentation = ignores_indentation



	# MARK: Methods
	def as_line(self, indentation: int) -> str:
		"""Converts this node and its children into lines of text with the given indentation level."""
		if self._ignores_indentation:
			return "#" + self._text

		s: str = ""
		for _ in range(indentation):
			s += "\t"
		return s + "#" + self._text
