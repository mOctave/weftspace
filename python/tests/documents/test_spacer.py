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
import unittest

from weftspace.documents import Spacer

class TestSpacer(unittest.TestCase):
	"""Tests for the spacer document component"""

	def test_spacer(self) -> None:
		s: Spacer = Spacer(3)
		self.assertEqual(3, s.size)


	def test_as_line_flush_zero_size(self) -> None:
		s: Spacer = Spacer(0)
		self.assertEqual("", s.as_line(0))


	def test_as_line_flush_positive_size(self) -> None:
		s: Spacer = Spacer(3)
		self.assertEqual(os.linesep + os.linesep, s.as_line(0))


	def test_as_line_flush_negative_size(self) -> None:
		s: Spacer = Spacer(-6)
		self.assertEqual("", s.as_line(0))


	def test_as_line_indented_zero_size(self) -> None:
		s: Spacer = Spacer(0)
		self.assertEqual("", s.as_line(4))


	def test_as_line_indented_positive_size(self) -> None:
		s: Spacer = Spacer(3)
		self.assertEqual(os.linesep + os.linesep, s.as_line(4))


	def test_as_line_indented_negative_size(self) -> None:
		s: Spacer = Spacer(-6)
		self.assertEqual("", s.as_line(4))

	def test_change_size(self) -> None:
		s: Spacer = Spacer(2)
		s.size = 17
		self.assertEqual(17, s.size)
