package io.github.moctave.weftspace;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

/** A set of tests for data nodes. */
public class TestDataNode {
	@Test
	public void testConstructor() {
		DataNode base = new DataNode();
		DataNode dn = new DataNode("Name", null, List.of("1", "2", "3"), List.of(base));

		assertEquals("Name", dn.getName());
		assertEquals(null, dn.getParent());
		assertEquals(List.of("1", "2", "3"), dn.getArgs());
		assertEquals(List.of(base), dn.getChildren());
	}

	@Test
	public void testEqualsHashCode() {
		DataNode child = new DataNode("X", null, List.of("A", "B", "C"), List.of());
		DataNode a = new DataNode("X", null, List.of("A", "B", "C"), List.of(child));
		DataNode b = new DataNode("X", null, List.of("A", "B", "C"), List.of(child));
		DataNode c = new DataNode("Y", null, List.of("A", "B", "C"), List.of(child));
		DataNode d = new DataNode("X", null, List.of("A", "B"), List.of(child));

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a, c);
		assertNotEquals(a, d);
		assertNotEquals(a, child);
	}


	@Test
	public void testSameParent() {
		DataNode a = new DataNode("X", null, List.of("A", "B", "C"), List.of());
		DataNode e = new DataNode("X", null, List.of("A", "B", "C"), List.of());
		DataNode b = new DataNode("X", a, List.of("A", "B", "C"), List.of(e));
		DataNode c = new DataNode("X", a, List.of("A", "B", "C"), List.of());
		DataNode d = new DataNode("X", b, List.of("A", "B", "C"), List.of());
		DataNode f = new DataNode("X", c, List.of("A", "B", "C"), List.of());

		assertTrue(a.sameParent(e));
		assertTrue(e.sameParent(a));

		assertTrue(b.sameParent(c));
		assertTrue(c.sameParent(b));

		assertFalse(a.sameParent(b));
		assertFalse(b.sameParent(a));

		assertFalse(d.sameParent(f));
		assertFalse(f.sameParent(d));
	}



	@Test
	public void testGeneralPropertiesCaseA() {
		DataNode node = new DataNode("A", null, List.of("1", "2", "3"), List.of());

		assertEquals("Node{name: A, args: [1, 2, 3], children: 0}", node.toString());
		assertFalse(node.hasParent());
		assertEquals(3, node.countArgs());
		assertEquals(0, node.countChildren());
	}

	@Test
	public void testGeneralPropertiesCaseB() {
		DataNode node = new DataNode("B", new DataNode(), List.of(), List.of(new DataNode()));

		assertEquals("Node{name: B, args: [], children: 1}", node.toString());
		assertTrue(node.hasParent());
		assertEquals(0, node.countArgs());
		assertEquals(1, node.countChildren());
	}
}
