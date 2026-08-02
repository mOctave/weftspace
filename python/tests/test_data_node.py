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

from weftspace import DataNode

class TestDataNode(unittest.TestCase):
	"""General tests for data nodes"""

	def test_constructor(self) -> None:
		base: DataNode = DataNode.create_root_node()
		dn: DataNode = DataNode("Name", None, ["1", "2", "3"], [base])

		self.assertEqual("Name", dn.name)
		self.assertEqual(None, dn.parent)
		self.assertEqual(["1", "2", "3"], dn.args)
		self.assertEqual([base], dn.children)

	def test_equals_hash_code(self) -> None:
		child: DataNode = DataNode("X", None, ["A", "B", "C"], [])
		a: DataNode = DataNode("X", None, ["A", "B", "C"], [child])
		b: DataNode = DataNode("X", None, ["A", "B", "C"], [child])
		c: DataNode = DataNode("Y", None, ["A", "B", "C"], [child])
		d: DataNode = DataNode("X", None, ["A", "B"], [child])

		self.assertEqual(a, b)
		self.assertEqual(a.__hash__(), b.__hash__())
		self.assertNotEqual(a, c)
		self.assertNotEqual(a, d)
		self.assertNotEqual(a, child)


	def test_str(self) -> None:
		node: DataNode = DataNode("A", None, ["1", "2", "3"], [])

		self.assertEqual("Node{name: A, args: ['1', '2', '3'], children: 0}", node.__str__())
