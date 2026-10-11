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

/** An interface consisting of common attributes of document components. */
public interface DocumentComponent {
	/**
	 * Converts this document element into a line of text with the given indentation level.
	 * 
	 * @param indentation The indentation depth of this line.
	 * @return A string representation of this component.
	 */
	public abstract @NonNull String asLine(int indentation);
}
