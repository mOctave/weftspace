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

public class TestSpacer {
	/** */
	@Test
	public void testSpacer() {
		final Spacer s = new Spacer(3);
		assertEquals(3, s.size());
	}



	/** */
	@Test
	public void testAsLineFlushZeroSize() {
		final Spacer s = new Spacer(0);
		assertEquals("", s.asLine(0));
	}

	/** */
	@Test
	public void testAsLineFlushPositiveSize() {
		final Spacer s = new Spacer(3);
		assertEquals(System.lineSeparator() + System.lineSeparator(), s.asLine(0));
	}

	/** */
	@Test
	public void testAsLineFlushNegativeSize() {
		final Spacer s = new Spacer(-6);
		assertEquals("", s.asLine(0));
	}


	/** */
	@Test
	public void testAsLineIndentedZeroSize() {
		final Spacer s = new Spacer(0);
		assertEquals("", s.asLine(4));
	}

	/** */
	@Test
	public void testAsLineIndentedPositiveSize() {
		final Spacer s = new Spacer(3);
		assertEquals(System.lineSeparator() + System.lineSeparator(), s.asLine(4));
	}

	/** */
	@Test
	public void testAsLineIndentedNegativeSize() {
		final Spacer s = new Spacer(-6);
		assertEquals("", s.asLine(4));
	}

	/** */
	@Test
	public void testSetSize() {
		final Spacer s = new Spacer(2);
		s.setSize(17);
		assertEquals(17, s.size());
	}
}
