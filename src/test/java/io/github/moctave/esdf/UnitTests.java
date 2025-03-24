package io.github.moctave.esdf;

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
		DataNode testNode = new DataNode("ship", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		testNode.addArg("Much Confused Wardragon");

		DataNode childNode = new DataNode("mass", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("35");
		testNode.addChild(childNode);

		childNode = new DataNode("drag", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		childNode.addArg("0.3");
		testNode.addChild(childNode);

		childNode = new DataNode("weapon", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		testNode.addChild(childNode);

		DataNode grandNode = new DataNode("hit force", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("308");
		childNode.addChild(grandNode);

		grandNode = new DataNode("hull damage", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("6100");
		childNode.addChild(grandNode);

		grandNode = new DataNode("shield damage", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
		grandNode.addArg("42");
		childNode.addChild(grandNode);

		childNode = new DataNode("description", DataNode.Flag.NORMAL, new ArrayList<>(), new ArrayList<>());
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
}
