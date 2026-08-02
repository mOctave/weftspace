package io.github.moctave.weftspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests that the represent() method of BuilderException works properly. */
public class TestBuilderException {
	@Test
	public void testRepresent() {
		DataNode emptyChild = new DataNode("Empty Child", null, List.of(), List.of());
		DataNode badChild = new DataNode("Bad Child", null, List.of("quid", "pro", "quo"), List.of(emptyChild));
		DataNode otherChild = new DataNode("Other Child", null, List.of("quid", "pro", "quo"), List.of());
		DataNode parent = new DataNode("Parent", null, List.of("foo", "bar"), List.of(badChild, otherChild));

		emptyChild.setParent(badChild);
		badChild.setParent(parent);
		otherChild.setParent(parent);

		assertEquals(
			"Builder Exception: Message\nParent foo bar\n\t\"Bad Child\" quid pro quo",
			new BuilderException("Message", badChild).represent()
		);
	}
}
