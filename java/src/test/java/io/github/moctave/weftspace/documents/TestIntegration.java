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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import io.github.moctave.weftspace.DataNode;
import io.github.moctave.weftspace.ReaderException;

public class TestIntegration {
	/**
	 * Check to make sure the two scanners show the same text.
	 * 
	 * @param expected The expected value
	 * @param actual The actual value
	 */
	public void assertScannersEqual(Scanner expected, Scanner actual) {
		int line = 0;
		while (expected.hasNextLine() && actual.hasNextLine()) {
			line++;
			final String e = expected.nextLine();
			final String a = actual.nextLine();
			if (!e.equals(a)) {
				fail(String.format("Files diverge at line %d: \"%s\" != \"%s\"", line, e, a));
			}
		}

		if (expected.hasNextLine() || actual.hasNextLine()) {
			fail(String.format("Files diverge after line %d: lengths do not match", line));
		}

		expected.close();
		actual.close();
	}



	/**
	 * This test checks to make sure that nodes with extra empty lines in the
	 * middle of their definitions still parse properly.
	 */
	@Test
	public void testHumanReadableNodes() {
		// Open and parse the test data
		final File testData = new File("../testdata/humanreadable.txt");
		final File expected = new File("../testdata/humanreadable-docres.txt");
		final File dest = new File("test-doc-hr.txt");
		final DataNode rootNode = new DataNode();

		final Document document;
		try {
			document = new Document(rootNode, testData);
			document.writeOut(dest);
		} catch (ReaderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			fail();
		}

		final DataNode result = io.github.moctave.weftspace.TestIntegration.getTestNode();
		assertEquals(result, rootNode.getChild(0));

		try {
			assertScannersEqual(new Scanner(expected), new Scanner(dest));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		// Do clean-up
		dest.delete();
	}


	/**
	 * This test checks to make sure that space-based indentation is parsed properly.
	 */
	@Test
	public void testSpaceIndentation() {
		// Open and parse the test data
		final File testData = new File("../testdata/spaceindented.txt");
		final File expected = new File("../testdata/spaceindented-docres.txt");
		final File dest = new File("test-doc-si.txt");
		final DataNode rootNode = new DataNode();

		final Document document;
		try {
			document = new Document(rootNode, testData);
			document.writeOut(dest);
		} catch (ReaderException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			fail();
		}

		final DataNode result = io.github.moctave.weftspace.TestIntegration.getTestNode();
		assertEquals(result, rootNode.getChild(0));

		try {
			assertScannersEqual(new Scanner(expected), new Scanner(dest));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		// Do clean-up
		dest.delete();
	}


	/**
	 * This test checks to make sure that even the worst indentation can still be parsed,
	 * but that the proper warnings are thrown when you attempt to do so.
	 */
	@Test
	public void testTerribleIndentation() {
		// Open and parse the test data
		final File testData = new File("../testdata/terriblyindented.txt");
		final File expected = new File("../testdata/terriblyindented-docres.txt");
		final File dest = new File("test-doc-ti.txt");
		final DataNode rootNode = new DataNode();

		final Document document;
		try {
			document = new Document(rootNode, testData);
			document.writeOut(dest);
		} catch (ReaderException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			fail();
		}

		final DataNode result = io.github.moctave.weftspace.TestIntegration.getTestNode();
		assertEquals(result, rootNode.getChild(0));

		try {
			assertScannersEqual(new Scanner(expected), new Scanner(dest));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		// Do clean-up
		dest.delete();
	}
}
