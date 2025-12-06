// Copyright (c) 2025 by mOctave
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/** Unit tests for DataReader */
public class TestDataReader {

	/** Tests for {@link DataReader#getIndentSubstring()}. */
	@Test
	public void getIndentSubstring() {
		assertEquals("", DataReader.getIndentSubstring("", 0));
		assertEquals("\t", DataReader.getIndentSubstring("\tHello World", 0));
		assertEquals("\t\t", DataReader.getIndentSubstring("\t\tHello World", 0));
		assertEquals("\t", DataReader.getIndentSubstring("\t\tHello World", 1));
		assertEquals("  ", DataReader.getIndentSubstring("    Hello World", 2));
		assertEquals(" ", DataReader.getIndentSubstring("    Hello World", 3));
		assertEquals("\t", DataReader.getIndentSubstring(" \tHello World", 1));
	}



	/** Tests for {@link DataReader#trimComments()}. */
	@Test
	public void trimComments() {
		assertEquals("", DataReader.trimComments(""));
		assertEquals(" ", DataReader.trimComments(" "));
		assertEquals("", DataReader.trimComments("#only comment"));
		assertEquals("only text", DataReader.trimComments("only text"));
		assertEquals("text and ", DataReader.trimComments("text and #comment"));
		assertEquals("Here is a", DataReader.trimComments("Here is a# double #comment"));
	}


	/** Tests for {@link DataReader#countLeadingWhitespace(String)}. */
	@Test
	public void countLeadingWhitespace() {
		assertEquals(0, DataReader.countLeadingWhitespace(""));
		assertEquals(1, DataReader.countLeadingWhitespace(" "));
		assertEquals(2, DataReader.countLeadingWhitespace("\t\t"));
		assertEquals(0, DataReader.countLeadingWhitespace("hello world"));
		assertEquals(2, DataReader.countLeadingWhitespace("  hello world  "));
		assertEquals(3, DataReader.countLeadingWhitespace("\t \tGoodbye World!\t"));
	}


	/** Tests for {@link DataReader#containsOnlyWhitespace(String)} */
	@Test
	public void containsOnlyWhitespace() {
		assertEquals(true, DataReader.containsOnlyWhitespace(""));
		assertEquals(false, DataReader.containsOnlyWhitespace("foo"));
		assertEquals(true, DataReader.containsOnlyWhitespace("    "));
		assertEquals(true, DataReader.containsOnlyWhitespace("\t"));
		assertEquals(true, DataReader.containsOnlyWhitespace(" \t "));
		assertEquals(false, DataReader.containsOnlyWhitespace("a \t"));
		assertEquals(false, DataReader.containsOnlyWhitespace("   b"));
	}
}
