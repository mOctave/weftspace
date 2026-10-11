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

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.moctave.weftspace.DataNode;

public class TestDocumentNode {
	/** */
	@Test
	public void testConstructor() {
		final DataNode base = new DataNode();
		final DataNode dataNode = new DataNode("Name", null, List.of("1", "2", "3"), List.of(base));
		final DocumentNode node = new DocumentNode(dataNode);
		final DocumentNode bdn = new DocumentNode(base);

		assertEquals("Name", node.getName());
		assertEquals(null, node.getParent());
		assertEquals(List.of("1", "2", "3"), node.getArgs());
		assertEquals(List.of(base), node.getChildren());

		assertEquals(List.of(bdn), node.getComponents());
	}
}
