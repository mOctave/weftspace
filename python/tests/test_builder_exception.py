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

from weftspace import BuilderError, DataNode

class TestBuilderError(unittest.TestCase):
	"""Tests that BuilderError.represent() works properly"""

	def test_represent(self) -> None:
		empty_child: DataNode = DataNode("Empty Child", None, [], [])
		bad_child: DataNode = DataNode("Bad Child", None, ["quid", "pro", "quo"], [empty_child])
		other_child: DataNode = DataNode("Other Child", None, ["quid", "pro", "quo"], [])
		parent: DataNode = DataNode("Parent", None, ["foo", "bar"], [bad_child, other_child])

		empty_child.parent = bad_child
		bad_child.parent = parent
		other_child.parent = parent

		self.assertEqual(
			"Builder Exception: Message\nParent foo bar\n\t\"Bad Child\" quid pro quo",
			BuilderError("Message", bad_child).represent()
		)
