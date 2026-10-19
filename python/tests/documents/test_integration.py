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

import filecmp
from pathlib import Path
import unittest

from weftspace import DataNode
from weftspace import ReaderError
from weftspace.documents import Document

from ..testing_utils import TestingUtils

class TestIntegration(unittest.TestCase):
	"""Basic integration tests for documents."""

	def test_human_readable_nodes(self) -> None:
		test_data: str = "../testdata/humanreadable.txt"
		expected: str = "../testdata/humanreadable-docres.txt"
		dest: str = "test-doc-hr.txt"
		root_node: DataNode = DataNode.create_root_node()

		document: Document

		try:
			document = Document(root_node, test_data)
			document.write_out(dest)
		except ReaderError:
			self.fail()

		result = TestingUtils.get_test_node()
		self.assertEqual(result, root_node.children[0])

		self.assertTrue(filecmp.cmp(expected, dest, shallow = False))

		Path.unlink(Path(dest))


	def test_space_indentation(self) -> None:
		test_data: str = "../testdata/spaceindented.txt"
		expected: str = "../testdata/spaceindented-docres.txt"
		dest: str = "test-doc-si.txt"
		root_node: DataNode = DataNode.create_root_node()

		document: Document

		try:
			document = Document(root_node, test_data)
			document.write_out(dest)
		except ReaderError:
			self.fail()

		result = TestingUtils.get_test_node()
		self.assertEqual(result, root_node.children[0])

		self.assertTrue(filecmp.cmp(expected, dest, shallow = False))

		Path.unlink(Path(dest))


	def test_terrible_indentation(self) -> None:
		test_data: str = "../testdata/terriblyindented.txt"
		expected: str = "../testdata/terriblyindented-docres.txt"
		dest: str = "test-doc-ti.txt"
		root_node: DataNode = DataNode.create_root_node()

		document: Document

		try:
			document = Document(root_node, test_data)
			document.write_out(dest)
		except ReaderError:
			self.fail()

		result = TestingUtils.get_test_node()
		self.assertEqual(result, root_node.children[0])

		self.assertTrue(filecmp.cmp(expected, dest, shallow = False))

		Path.unlink(Path(dest))
