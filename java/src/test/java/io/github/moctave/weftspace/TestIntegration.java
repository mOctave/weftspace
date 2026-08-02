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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The integrations tests for this library.
 */
public class TestIntegration {
	/**
	 * A method which constructs a sample data node that can be used across multiple tests.
	 * @return The node to use for testing.
	 */
	public static DataNode getTestNode() {
		DataNode testNode = new DataNode("ship", null, new ArrayList<>(), new ArrayList<>());
		testNode.addArg("Much Confused Wardragon");

		DataNode childNode = new DataNode("mass", testNode, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("35");
		testNode.addChild(childNode);

		childNode = new DataNode("drag", testNode, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("0.3");
		testNode.addChild(childNode);

		childNode = new DataNode("weapon", testNode, new ArrayList<>(), new ArrayList<>());
		testNode.addChild(childNode);

		DataNode grandNode = new DataNode("hit force", childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("308");
		childNode.addChild(grandNode);

		grandNode = new DataNode("hull damage", childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("6100");
		childNode.addChild(grandNode);

		grandNode = new DataNode("shield damage", childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("42");
		childNode.addChild(grandNode);

		childNode = new DataNode("description", testNode, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("This Wardragon bears no resemblance to any actual ship in the game Endless Sky. It has no material existence, despite having mass and possibly explaining the existence of the dark matter in our universe.");
		testNode.addChild(childNode);

		return testNode;
	}



	/**
	 * This test checks to make sure that identical data nodes
	 * are being treated as equal.
	 */
	@Test
	public void testNodeEquality() {
		assertTrue(getTestNode().equals(getTestNode()));
	}



	/**
	 * This test creates a new {@link DataNode}, writes it to a file using {@link DataWriter},
	 * reads it using {@link DataReader}, and compares it against the original node.
	 */
	@Test
	public void testIO() {
		// Write test data to a file
		File file = new File("test.txt");
		DataWriter writer = new DataWriter(file);
		try {
			writer.open();
		} catch (IOException e) {
			fail();
		}
		writer.write(getTestNode());
		writer.close();


		// Read test data from the file
		DataReader reader = new DataReader(file, new DataNode());
		try {
			reader.parse();
		} catch (ReaderException e) {
			fail();
		}
		DataNode loadedNode = reader.getRoot().getChild(0);

		// Do clean-up
		file.delete();

		// Check to make sure there were no issues
		assertTrue(getTestNode().equals(loadedNode));
	}



	/**
	 * This test takes data from the test node, builds it, and makes sure it's
	 * working properly.
	 */
	@Test
	public void testBuilder() {
		try {
			// Build the node
			DataNode node = getTestNode();
			String name = Builder.buildString(node, 0);
			int mass = 0;
			double drag = 0.;
			String description = "";
			for (DataNode child : node.getChildren()) {
				if (child.getName().equals("mass")) {
					mass = Builder.buildInt(child, 0);
				} else if (child.getName().equals("drag")) {
					drag = Builder.buildDouble(child, 0);
				}  else if (child.getName().equals("description")) {
					description += Builder.buildString(child, 0);
				}
			}

			// Check to make sure there were no issues
			assertTrue(
				name.equals("Much Confused Wardragon")
				&& mass == 35
				&& drag == 0.3
				&& description.equals("This Wardragon bears no resemblance to any actual ship in the game Endless Sky. It has no material existence, despite having mass and possibly explaining the existence of the dark matter in our universe.")
			);
		} catch (BuilderException e) {
			fail("Unexpected builder exception");
		}
	}



	/**
	 * This test checks to make sure that nodes with extra empty lines in the
	 * middle of their definitions still parse properly.
	 */
	@Test
	public void testHumanReadableNodes() {
		// Open and parse the test data
		File testData = new File("../testdata/humanreadable.txt");
		DataNode rootNode = new DataNode();
		DataReader reader = new DataReader(testData, rootNode);

		try {
			reader.parse();
		} catch (ReaderException e) {
			fail();
		}

		DataNode loadedNode = rootNode.getChild(0);

		// Check to make sure there were no issues
		assertTrue(getTestNode().equals(loadedNode));
	}


	/**
	 * This test checks to make sure that space-based indentation is parsed properly.
	 */
	@Test
	public void testSpaceIndentation() {

		// Open and parse the test data
		File testData = new File("../testdata/spaceindented.txt");
		DataNode rootNode = new DataNode();
		DataReader reader = new DataReader(testData, rootNode);

		try {
			reader.parse();
		} catch (ReaderException e) {
			fail();
		}

		DataNode loadedNode = rootNode.getChild(0);

		assertTrue(getTestNode().equals(loadedNode));
	}


	/**
	 * This test checks to make sure that even the worst indentation can still be parsed,
	 * but that the proper warnings are thrown when you attempt to do so.
	 */
	@Test
	public void testTerribleIndentation() {

		// Open and parse the test data
		File testData = new File("../testdata/terriblyindented.txt");
		DataNode rootNode = new DataNode();
		DataReader reader = new DataReader(testData, rootNode);

		try {
			reader.parse();
			fail();
		} catch (ReaderException e) {
			assertTrue(e.getMessage().startsWith("Warning - mixed whitespace"));
		}

		DataNode loadedNode = rootNode.getChild(0);

		assertTrue(getTestNode().equals(loadedNode));
	}
}
