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

# This package contains tests for the Python Weftspace library.
# All test modules should be imported here, as this is the file that is run as the
# test suite by GitHub Actions.

# type: ignore
from tests.test_utils import TestUtils
from tests.unit_tests import UnitTests

import sys
import os

sys.path.append(os.path.dirname(os.path.realpath(__file__)) + "/../src")
