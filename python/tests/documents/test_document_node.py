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
from weftspace.documents import DocumentNode

class TestDocumentNode(unittest.TestCase):
	"""A test for the document node constructor."""

	def test_constructor(self) -> None:
		base: DataNode = DataNode.create_root_node()
		data_node: DataNode = DataNode("Name", None, ["1", "2", "3"], [base])
		node: DocumentNode = DocumentNode.create(data_node)
		bdn: DocumentNode = DocumentNode.create(base)

		self.assertEqual("Name", node.name)
		self.assertEqual(None, node.parent)
		self.assertEqual(["1", "2", "3"], node.args)
		self.assertEqual([base], node.children)

		self.assertEqual([bdn], node.components)
