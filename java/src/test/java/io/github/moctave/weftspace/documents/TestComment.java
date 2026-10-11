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

package io.github.moctave.weftspace.documents;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestComment {
	/** */
	@Test
	public void testConstructor() {
		final Comment c = new Comment("foo", false);
		assertEquals("foo", c.getText());
		assertEquals(false, c.ignoresIndentation());
	}



	/** */
	@Test
	public void testAsLineFlushObserving() {
		final Comment c = new Comment("bar", false);
		assertEquals("#bar", c.asLine(0));
	}

	/** */
	@Test
	public void testAsLineFlushIgnoring() {
		final Comment c = new Comment("bar", true);
		assertEquals("#bar", c.asLine(0));
	}

	/** */
	@Test
	public void testAsLineIndentedObserving() {
		final Comment c = new Comment("bar", false);
		assertEquals("\t\t#bar", c.asLine(2));
	}

	/** */
	@Test
	public void testAsLineIndentedIgnoring() {
		final Comment c = new Comment("bar", true);
		assertEquals("#bar", c.asLine(2));
	}


	/** */
	@Test
	public void testSetText() {
		final Comment c = new Comment("foo", true);
		c.setText("bar");
		assertEquals("bar", c.getText());
	}

	/** */
	@Test
	public void testSetIgnoreIndentation() {
		final Comment c = new Comment("foo", false);
		c.setIgnoreIndentation(true);
		assertEquals(true, c.ignoresIndentation());
	}
}
