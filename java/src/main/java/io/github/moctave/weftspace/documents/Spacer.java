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

import org.jspecify.annotations.*;

/** One or more consecutive empty lines in a document. */
public class Spacer implements DocumentComponent {
	/** How many lines this spacer takes up. */
	private int size;

	// MARK: Constructor
	/**
	 * Creates a new spacer with the given size.
	 * 
	 * @param size The number of lines this spacer takes up.
	 */
	public Spacer(int size) {
		this.size = size;
	}

	// MARK: Methods
	/**
	 * Converts this spacer into lines for output.
	 * 
	 * @param indentation The indentation depth of this line.
	 * @return A string representation of this component.
	 */
	@Override
	public @NonNull String asLine(int indentation) {
		String s = "";

		for (int i = 0; i < size - 1; i++) {
			s += System.lineSeparator();
		}

		return s;
	}



	// MARK: Getters / Setters
	/**
	 * Getter: Returns the size of this spacer.
	 * 
	 * @return {@link #size}
	 */
	public int size() {
		return size;
	}

	/**
	 * Setter: Changes the size of this spacer.
	 * 
	 * @param size The new value for {@link #size}.
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
