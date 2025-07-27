# Copyright (c) 2025 by mOctave
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

from pathlib import Path
import unittest

from weftspace import Builder, DataNode, DataReader, DataWriter, Logger

from tests.test_utils import TestUtils

class UnitTests(unittest.TestCase):
	"""The actual unit tests being run on the code."""

	def test_node_equality(self):
		"""A test to make sure identical data nodes are being treated as equal."""
		self.assertEqual(TestUtils.get_test_node(), TestUtils.get_test_node())
	


	def test_io(self):
		"""
		This test creates a new DataNode, writes it to a file using DataWriter,
		reads it using DataReader, and compares it against the original node.
		"""

		# Start the error counter
		Logger.reset_error_count()

		# Write test data to a file
		writer: DataWriter = DataWriter("test.txt")
		writer.open()
		writer.write(TestUtils.get_test_node())
		writer.close()

		# Read test data from the file
		reader: DataReader = DataReader("test.txt", DataNode.create_root_node())
		reader.parse()
		print(reader.root)
		for child in reader.root.children:
			print(child)
		loaded_node: DataNode = reader.root.children[0]

		# Do clean-up
		Path.unlink(Path("test.txt"))

		# Check to make sure there were no issues
		print("TEST RESULTS:")
		print(TestUtils.get_test_node())
		print(loaded_node)
		print("Errors:", Logger.get_error_count())
		print("---")
		self.assertTrue(
			Logger.get_error_count() == 0
			and TestUtils.get_test_node() == loaded_node
		)



	def test_builder(self):
		"""
		This test takes data from the test node, builds it, and makes sure it's
		working properly.
		"""

		# Start the error counter
		Logger.reset_error_count()

		# Build the node
		node: DataNode = TestUtils.get_test_node()
		name: str | None = Builder.build_string(node, 0, "ship")
		mass: int = 0
		drag: float = 0
		description: str | None = ""
		for child in node.children:
			match child.name:
				case "mass":
					mass = Builder.build_int(child, 0, "ship")
				case "drag":
					drag = Builder.build_float(child, 0, "ship")
				case "description":
					description = Builder.build_string(child, 0, "ship")
				case _:
					pass
		
		# Check to make sure there were no issues
		self.assertTrue(
			Logger.get_error_count() == 0
			and name == "Much Confused Wardragon"
			and mass == 35
			and drag == 0.3
			and description == "This Wardragon bears no resemblance to any actual ship in the game Endless Sky. It has no material existence, despite having mass and possibly explaining the existence of the dark matter in our universe."
		)



	def test_human_readable_nodes(self):
		"""
		This test checks to make sure that nodes with extra empty lines in the
		iddle of their definitions still parse properly.
		"""

		# Start the error counter
		Logger.reset_error_count()

		# Open and parse the test data
		root_node: DataNode = DataNode.create_root_node()
		reader: DataReader = DataReader("../testdata/humanreadable.txt", root_node)
		reader.parse()

		loaded_node: DataNode = root_node.children[0]

		# Check to make sure there were no issues
		self.assertTrue(Logger.get_error_count() == 0 and TestUtils.get_test_node() == loaded_node)

if __name__ == "__main__":
	unittest.main()