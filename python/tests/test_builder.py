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

from weftspace import Builder, BuilderError, DataNode

class TestBuilder(unittest.TestCase):
	"""Tests for the various methods of Builder"""

	no_args: DataNode
	string_args: DataNode
	int_args: DataNode
	float_args: DataNode

	def setUp(self) -> None:
		print("PRINT ME")
		self.float_args = DataNode("doubargs", None, ["0.", "-0.8", "15.36", "3.14159"], [])
		self.string_args = DataNode("strargs", None, ["foo", "bar"], [])
		self.int_args = DataNode("intargs", None, ["1", "2", "3", "-1"], [self.float_args])
		self.no_args = DataNode("argless", None, [], [self.string_args, self.int_args])
		return super().setUp()

	def test_pass(self):
		return

	# MARK: build_string()
	def test_build_string_no_args(self) -> None:
		try:
			Builder.build_string(self.no_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass
	
	def test_build_string_in_range(self) -> None:
		try:
			self.assertEqual("foo", Builder.build_string(self.string_args, 0))
			self.assertEqual("3.14159", Builder.build_string(self.float_args, 3))
		except BuilderError:
			self.fail()

	def test_build_string_out_of_range(self) -> None:
		try:
			Builder.build_string(self.string_args, 2)
			self.fail()
		except BuilderError:
			# Expected Result
			pass



	# MARK: build_int()
	def test_build_int_no_args(self) -> None:
		try:
			Builder.build_int(self.no_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass
	
	def test_build_int_in_range(self) -> None:
		try:
			self.assertEqual(3, Builder.build_int(self.int_args, 2))
			self.assertEqual(-1, Builder.build_int(self.int_args, 3))
		except BuilderError:
			self.fail()

	def test_build_int_out_of_range(self) -> None:
		try:
			Builder.build_int(self.int_args, 4)
			self.fail()
		except BuilderError:
			# Expected Result
			pass

	def test_build_int_string_value(self) -> None:
		try:
			Builder.build_int(self.string_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass

	def test_build_int_float_value(self) -> None:
		try:
			Builder.build_int(self.float_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass



	# MARK: build_float()
	def test_build_float_no_args(self) -> None:
		try:
			Builder.build_float(self.no_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass
	
	def test_build_float_in_range(self) -> None:
		try:
			self.assertAlmostEqual(0., Builder.build_float(self.float_args, 0))
			self.assertAlmostEqual(3.14159, Builder.build_float(self.float_args, 3))
		except BuilderError:
			self.fail()

	def test_build_float_out_of_range(self) -> None:
		try:
			Builder.build_float(self.float_args, 4)
			self.fail()
		except BuilderError:
			# Expected Result
			pass

	def test_build_float_string_value(self) -> None:
		try:
			Builder.build_float(self.string_args, 0)
			self.fail()
		except BuilderError:
			# Expected Result
			pass

	def test_build_float_int_value(self) -> None:
		try:
			self.assertAlmostEqual(1., Builder.build_float(self.int_args, 0))
		except BuilderError:
			self.fail()
			pass



	# search()
	def test_search_no_match(self) -> None:
		self.assertEqual(None, Builder.search(DataNode("a", None, ["q", "r", "s"], []), self.no_args))


	def test_search_exact_match(self) -> None:
		self.assertEqual(self.string_args, Builder.search(self.string_args, self.no_args))

	def test_search_inexact_match(self) -> None:
		self.assertEqual(self.int_args,
			Builder.search(DataNode("intargs", None, ["1", "0", "not even an int"], []), self.no_args))


	def test_search_close_failure(self) -> None:
		self.assertEqual(None,
			Builder.search(DataNode("intargs", None, [], []), self.no_args))
		self.assertEqual(None,
			Builder.search(DataNode("intargs", None, ["."], []), self.no_args))
		self.assertEqual(None,
			Builder.search(DataNode("not int args", None, ["1"], []), self.no_args))

	def test_search_second_level_match(self) -> None:
		self.assertEqual(None, Builder.search(self.float_args, self.no_args))



if __name__ == "__main__":
	unittest.main()
