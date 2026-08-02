// Copyright (c) 2026 by mOctave
//
// This program is free software: you can redistribute it and/or modify it under the
// terms of the GNU Affero General Public License as published by the Free Software
// Foundation, either version 3 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
// PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License along with
// this program. If not, see <https://www.gnu.org/licenses/>.

package io.github.moctave.weftspace;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBuilder {
	private DataNode testNoArgs;
	private DataNode testStringArgs;
	private DataNode testIntegerArgs;
	private DataNode testDoubleArgs;

	@BeforeEach
	public void init() {
		testDoubleArgs = new DataNode("doubargs", null, List.of("0.", "-0.8", "15.36", "3.14159"), List.of());
		testStringArgs = new DataNode("strargs", null, List.of("foo", "bar"), List.of());
		testIntegerArgs = new DataNode("intargs", null, List.of("1", "2", "3", "-1"), List.of(testDoubleArgs));
		testNoArgs = new DataNode("argless", null, List.of(), List.of(testStringArgs, testIntegerArgs));
	}


	// MARK: buildString()
	@Test
	public void testBuildStringNoArgs() {
		try {
			Builder.buildString(testNoArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildStringInRange() {
		try {
			assertEquals("foo", Builder.buildString(testStringArgs, 0));
			assertEquals("3.14159", Builder.buildString(testDoubleArgs, 3));
		} catch (BuilderException e) {
			fail();
		}
	}

	@Test
	public void testBuildStringOutOfRange() {
		try {
			Builder.buildString(testStringArgs, 2);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}



	// MARK: buildInt()
	@Test
	public void testBuildIntNoArgs() {
		try {
			Builder.buildInt(testNoArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildIntInRange() {
		try {
			assertEquals(3, Builder.buildInt(testIntegerArgs, 2));
			assertEquals(-1, Builder.buildInt(testIntegerArgs, 3));
		} catch (BuilderException e) {
			fail();
		}
	}

	@Test
	public void testBuildIntOutOfRange() {
		try {
			Builder.buildInt(testIntegerArgs, 4);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildIntStringValue() {
		try {
			Builder.buildInt(testStringArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildIntDoubleValue() {
		try {
			Builder.buildInt(testDoubleArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}




	// MARK: buildDouble()
	@Test
	public void testBuildDoubleNoArgs() {
		try {
			Builder.buildDouble(testNoArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildDoubleInRange() {
		try {
			assertEquals(0., Builder.buildDouble(testDoubleArgs, 0));
			assertEquals(3.14159, Builder.buildDouble(testDoubleArgs, 3), 1e-6);
		} catch (BuilderException e) {
			fail();
		}
	}

	@Test
	public void testBuildDoubleOutOfRange() {
		try {
			Builder.buildDouble(testDoubleArgs, 4);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildDoubleStringValue() {
		try {
			Builder.buildDouble(testStringArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildDoubleIntValue() {
		try {
			assertEquals(1., Builder.buildDouble(testIntegerArgs, 0));
		} catch (BuilderException e) {
			fail();
		}
	}



	// MARK: buildLong()
	@Test
	public void testBuildLongNoArgs() {
		try {
			Builder.buildLong(testNoArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildLongInRange() {
		try {
			assertEquals(3l, Builder.buildLong(testIntegerArgs, 2));
			assertEquals(-1l, Builder.buildLong(testIntegerArgs, 3));
		} catch (BuilderException e) {
			fail();
		}
	}

	@Test
	public void testBuildLongOutOfRange() {
		try {
			Builder.buildLong(testIntegerArgs, 4);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildLongStringValue() {
		try {
			Builder.buildLong(testStringArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}

	@Test
	public void testBuildLongDoubleValue() {
		try {
			Builder.buildLong(testDoubleArgs, 0);
			fail();
		} catch (BuilderException e) {
			// Expected result
		}
	}



	// MARK: search()
	@Test
	public void testSearchNoMatch() {
		assertNull(Builder.search(
			new DataNode("a", null, List.of("q", "r", "s"), List.of()),
			testNoArgs)
		);
	}

	@Test
	public void testSearchExactMatch() {
		assertEquals(testStringArgs, Builder.search(testStringArgs, testNoArgs));
	}

	@Test
	public void testSearchInexactMatch() {
		assertEquals(testIntegerArgs, Builder.search(
			new DataNode("intargs", null, List.of("1", "0", "not even an int"), List.of()),
			testNoArgs)
		);
	}


	@Test
	public void testSearchCloseFailure() {
		assertNull(Builder.search(
			new DataNode("intargs", null, List.of(), List.of()),
			testNoArgs)
		);

		assertNull(Builder.search(
			new DataNode("intargs", null, List.of("."), List.of()),
			testNoArgs)
		);

		assertNull(Builder.search(
			new DataNode("not int args", null, List.of("1"), List.of()),
			testNoArgs)
		);
	}

	@Test
	public void testSearchSecondLevelMatch() {
		assertNull(Builder.search(testDoubleArgs, testNoArgs));
	}
}
