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

from abc import ABC

from weftspace import DataNode

class TestUtils(ABC):
	"""A class containing utility methods to support tests."""

	@classmethod
	def get_test_node(cls):
		"""A method which constructs a sample data node that can be used across multiple tests."""
		test_node: DataNode = DataNode("ship", None, ["Much Confused Wardragon"], [])

		child_node: DataNode = DataNode("mass", test_node, ["35"], [])
		test_node.children.append(child_node)

		child_node: DataNode = DataNode("drag", test_node, ["0.3"], [])
		test_node.children.append(child_node)

		child_node: DataNode = DataNode("weapon", test_node, [], [])
		grand_node: DataNode = DataNode("hit force", child_node, ["308"], [])
		child_node.children.append(grand_node)
		grand_node: DataNode = DataNode("hull damage", child_node, ["6100"], [])
		child_node.children.append(grand_node)
		grand_node: DataNode = DataNode("shield damage", child_node, ["42"], [])
		child_node.children.append(grand_node)
		test_node.children.append(child_node)

		child_node: DataNode = DataNode("description", test_node, ["This Wardragon bears no resemblance to any actual ship in the game Endless Sky. It has no material existence, despite having mass and possibly explaining the existence of the dark matter in our universe."], [])
		test_node.children.append(child_node)

		return test_node



	


