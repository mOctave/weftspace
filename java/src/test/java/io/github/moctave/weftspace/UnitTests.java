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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * A class containing all the unit tests to be run on this library.
 */
public class UnitTests {
	/**
	 * A method which constructs a sample data node that can be used across multiple tests.
	 * @return The node to use for testing.
	 */
	public DataNode getTestNode() {
		DataNode testNode = new DataNode("ship", DataNode.Flag.NORMAL, null, new ArrayList<>(), new ArrayList<>());
		testNode.addArg("Much Confused Wardragon");

		DataNode childNode = new DataNode("mass", DataNode.Flag.NORMAL, testNode, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("35");
		testNode.addChild(childNode);

		childNode = new DataNode("drag", DataNode.Flag.NORMAL, testNode, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("0.3");
		testNode.addChild(childNode);

		childNode = new DataNode("weapon", DataNode.Flag.NORMAL, testNode, new ArrayList<>(), new ArrayList<>());
		testNode.addChild(childNode);

		DataNode grandNode = new DataNode("hit force", DataNode.Flag.NORMAL, childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("308");
		childNode.addChild(grandNode);

		grandNode = new DataNode("hull damage", DataNode.Flag.NORMAL, childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("6100");
		childNode.addChild(grandNode);

		grandNode = new DataNode("shield damage", DataNode.Flag.NORMAL, childNode, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("42");
		childNode.addChild(grandNode);

		childNode = new DataNode("description", DataNode.Flag.NORMAL, testNode, new ArrayList<>(), new ArrayList<>());
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
		// Start the error counter
		Logger.resetErrorCount();


		// Write test data to a file
		File file = new File("test.txt");
		DataWriter writer = new DataWriter(file);
		writer.open();
		writer.write(getTestNode(), 0);
		writer.close();


		// Read test data from the file
		DataReader reader = new DataReader(file, new DataNode());
		reader.parse();
		DataNode loadedNode = reader.getRoot().getChildren().get(0);

		// Do clean-up
		file.delete();

		// Check to make sure there were no issues
		assertTrue(Logger.getErrorCount() == 0 && getTestNode().equals(loadedNode));
	}

	/**
	 * This test takes data from the test node, builds it, and makes sure it's
	 * working properly.
	 */
	@Test
	public void testBuilder() {
		// Start the error counter
		Logger.resetErrorCount();

		// Build the node
		DataNode node = getTestNode();
		String name = Builder.buildString(node, 0, "ship");
		int mass = 0;
		double drag = 0.;
		String description = "";
		for (DataNode child : node.getChildren()) {
			if (child.getName().equals("mass")) {
				mass = Builder.buildInt(child, 0, "ship");
			} else if (child.getName().equals("drag")) {
				drag = Builder.buildDouble(child, 0, "ship");
			}  else if (child.getName().equals("description")) {
				description += Builder.buildString(child, 0, "ship");
			}
		}

		// Check to make sure there were no issues
		assertTrue(
			Logger.getErrorCount() == 0
			&& name.equals("Much Confused Wardragon")
			&& mass == 35
			&& drag == 0.3
			&& description.equals("This Wardragon bears no resemblance to any actual ship in the game Endless Sky. It has no material existence, despite having mass and possibly explaining the existence of the dark matter in our universe.")
		);
	}



	/**
	 * This test checks to make sure that nodes with extra empty lines in the
	 * middle of their definitions still working properly.
	 */
	@Test
	public void testHumanReadableNodes() {
		// Start the error counter
		Logger.resetErrorCount();

		// Open and parse the test data
		File testData = new File("../testdata/humanreadable.txt");
		DataNode rootNode = new DataNode();
		DataReader reader = new DataReader(testData, rootNode);
		reader.parse();

		DataNode loadedNode = rootNode.getChildren().get(0);

		// Check to make sure there were no issues
		assertTrue(Logger.getErrorCount() == 0 && getTestNode().equals(loadedNode));
	}
}
