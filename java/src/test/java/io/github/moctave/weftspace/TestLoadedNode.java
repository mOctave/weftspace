package io.github.moctave.weftspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

/** A basic test for the LoadedNode constructor. */
public class TestLoadedNode {
	@Test
	public void testConstructor() {
		DataNode base = new DataNode();
		LoadedNode ln = new LoadedNode("Name", null, List.of("1", "2", "3"), List.of(base), 17, new File("fake/uri"));

		assertEquals("Name", ln.getName());
		assertEquals(null, ln.getParent());
		assertEquals(List.of("1", "2", "3"), ln.getArgs());
		assertEquals(List.of(base), ln.getChildren());
		assertEquals(17, ln.getLine());
		assertEquals(new File("fake/uri"), ln.getFile());
	}
}
