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

from __future__ import annotations

class DataNode:
	"""A class representing a node on the data tree."""

	# MARK: Properties
	_name: str

	@property
	def name(self):
		"""The name of this node."""
		return self._name
	
	@name.setter
	def name(self, name: str):
		"""Changes the name of this node."""
		self._name = name



	_parent: DataNode | None

	@property
	def parent(self):
		"""The parent of this node."""
		return self._parent
	
	@parent.setter
	def parent(self, parent: DataNode | None):
		"""
		Changes the parent of this node. This should be used very cautiously, as it can
		easily damage the structure of the node tree.
		"""
		self._parent = parent

	_args: list[str]

	@property
	def args(self):
		"""This node's arguments."""
		return self._args
	
	@args.setter
	def args(self, args: list[str]):
		"""
		Overwrites this node's argument list.
		"""
		self._args = args

	_children: list[DataNode]

	@property
	def children(self):
		"""This node's children."""
		return self._children
	
	@children.setter
	def children(self, children: list[DataNode]):
		"""
		Entirely overwrites the list of this node's children.
		This should be used very cautiously, as it can
		easily damage the structure of the node tree.
		"""
		self._children = children



	# MARK: Constructors
	def __init__(self, name: str, parent: DataNode | None, args: list[str], children: list[DataNode]):
		"""
		Primary constructor. Takes all the standard arguments, except those defined by
		LoadedNode.
		"""
		self.name = name
		self.parent = parent
		self.args = args
		self.children = children


	@staticmethod
	def create_root_node():
		"""
		Builds and returns an empty node intended to be used as a root node for the DataReader.
		"""
		return DataNode("--ROOT--", None, [], [])




	# MARK: Methods
	def __hash__(self):
		"""
		Generates a hash code for this node, so that all nodes which are equal
		have the same hash code.

		Multiple distinct nodes may have the same hash code, as
		(1) the mechanics for hash() are not controlled,
		(2) parents are not taken into account in this method, and
		(3) nodes which have themself as a child will not have their children considered.
		"""
		prime: int = 31
		hash_code: int = 1
		hash_code = prime * hash_code + hash(self.name)
		# Length is used for hashing here because lists are mutable and so can't be hashed.
		hash_code = prime * hash_code + hash(len(self.args))
		hash_code = prime * hash_code + 0 if self in self.children else hash(len(self.children))

		return hash_code
	


	def __eq__(self, other: object) -> bool:
		"""
		Indicates whether an object is equal to this node, comparing the names, flags,
		arguments, and children of the two nodes.

		NOTE: Parents are NOT considered by this method, to reduce complexity. If you
		need to check if two nodes have identical parents, compare the parents of the
		two nodes using the equals operator.
		"""
		# The other object is a reference for this node
		if other is self:
			return True

		# The other object is not a data node
		if not isinstance(other, DataNode):
			return False
		
		# Check if the two nodes have a different property
		if other.name != self.name:
			return False
		if other.args != self.args:
			return False
		if other.children != self.children:
			return False

		# Everything that matters is equal, return True
		return True



	def __str__(self) -> str:
		"""
		Returns a string representation of this node, including name, arguments,
		and the number of children it has.
		"""
		return "Node{{name: {0}, args: {1}, children: {2}}}".format(self.name, str(self.args), len(self.children))
