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

import unittest

from weftspace.documents import Comment

class TestComment(unittest.TestCase):
	"""General tests for document comments"""

	def test_constructor(self) -> None:
		c: Comment = Comment("foo", False)
		self.assertEqual("foo", c.text)
		self.assertFalse(c.ignores_indentation)


	def test_as_line_flush_observing(self) -> None:
		c: Comment = Comment("bar", False)
		self.assertEqual("#bar", c.as_line(0))


	def test_as_line_flush_ignoring(self) -> None:
		c: Comment = Comment("bar", True)
		self.assertEqual("#bar", c.as_line(0))


	def test_as_line_indented_observing(self) -> None:
		c: Comment = Comment("bar", False)
		self.assertEqual("\t\t#bar", c.as_line(2))


	def test_as_line_indented_ignoring(self) -> None:
		c: Comment = Comment("bar", True)
		self.assertEqual("#bar", c.as_line(2))


	def test_change_text(self) -> None:
		c: Comment = Comment("foo", True)
		c.text = "bar"
		self.assertEqual("bar", c.text)


	def test_change_ignore_indentation(self) -> None:
		c: Comment = Comment("foo", False)
		c.ignores_indentation = True
		self.assertTrue(c.ignores_indentation)
