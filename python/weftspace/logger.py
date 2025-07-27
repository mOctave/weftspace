# Copyright (c) 2025 by mOctave
#
# This program is free software: you can redistribute it and/or modify it under the
# terms of the GNU Affero General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
# PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License along with
# this program. If not, see <https://www.gnu.org/licenses/>.

from __future__ import annotations
from abc import ABC
from enum import Enum
from os import path
import sys
from typing import Any, Final, TextIO

from .data_node import DataNode
from .loaded_node import LoadedNode

class Logger(ABC):
	"""A class which handles error logging for this library."""

	# MARK: Constants

	# ANSI Colours
	BLACK: Final[str] = "\u001B[30m"
	"""Escape sequence for ANSI black text."""

	RED: Final[str] = "\u001B[31m"
	"""Escape sequence for ANSI red text."""

	GREEN: Final[str] = "\u001B[32m"
	"""Escape sequence for ANSI green text."""

	YELLOW: Final[str] = "\u001B[33m"
	"""Escape sequence for ANSI yellow text."""

	BLUE: Final[str] = "\u001B[34m"
	"""Escape sequence for ANSI blue text."""

	MAGENTA: Final[str] = "\u001B[35m"
	"""Escape sequence for ANSI magenta text."""

	CYAN: Final[str] = "\u001B[36m"
	"""Escape sequence for ANSI cyan text."""

	WHITE: Final[str] = "\u001B[37m"
	"""Escape sequence for ANSI white text."""

	RESET: Final[str] = "\u001B[0m"
	"""Escape sequence for ANSI formatting reset."""

	BOLD: Final[str] = "\u001B[1m"
	"""Escape sequence for ANSI bold text."""


	class Severity(Enum):
		"""An enum storing different levels of severity that a message can have."""

		FATAL = 0
		"""This error resulted in the program's immediate failure."""

		ERROR = 1
		"""This error is serious, but not immediately fatal."""

		WARN = 2
		"""This error is not serious, but may cause later issues or unintended behaviour."""
		
		INFO = 3
		"""This message is not an error."""

		SUCCESS = 4
		"""This message is an indicator of success."""



	# MARK: Error Count
	_error_count: int = 0

	@classmethod
	def get_error_count(cls):
		"""Returns the number of errors thrown by this program."""
		return cls._error_count
	
	@classmethod
	def reset_error_count(cls):
		"""Resets the error count to 0."""
		cls._error_count = 0
	
	@classmethod
	def count_error(cls):
		"""Increments the error count by 1."""
		cls._error_count += 1



	# MARK: Message
	class Message(ABC):
		def __init__(self, severity: Logger.Severity, content: str):
			self.severity = severity
			self.content = content

		_severity: Logger.Severity

		@property
		def severity(self):
			"""The severity of this message."""
			return self._severity
	
		@severity.setter
		def severity(self, severity: Logger.Severity):
			"""Changes the severity of this message."""
			self._severity = severity

		
		_content: str

		@property
		def content(self):
			"""The content of this message."""
			return self._content
	
		@content.setter
		def content(self, content: str):
			"""Changes the content of this message."""
			self._content = content



		def format_on(self, stream: TextIO):
			"""Prints the appropriate ANSI escape code for the severity of this message."""
			match self.severity:
				case Logger.Severity.FATAL:
					stream.write(Logger.BOLD)
					stream.write(Logger.RED)
					Logger.count_error()
				case Logger.Severity.ERROR:
					stream.write(Logger.RED)
					Logger.count_error()
				case Logger.Severity.WARN:
					stream.write(Logger.YELLOW)
				case Logger.Severity.SUCCESS:
					stream.write(Logger.GREEN)
				case _:
					stream.write(Logger.BLUE)


		def print_prefix(self, stream: TextIO):
			"""Prints the prefix for this error message."""
			match self.severity:
				case Logger.Severity.FATAL:
					stream.write("Fatal Error: ")
				case Logger.Severity.ERROR:
					stream.write("Error: ")
				case Logger.Severity.WARN:
					stream.write("Warning: ")
				case _:
					pass

		def end_message(self, stream: TextIO):
			"""
			Ends the message, reseting formating. If this message is a fatal error, calling
			this method will also exit the program with an error code of 1.
			"""
			stream.write(Logger.RESET)
			stream.write("\n")
			if (self.severity == Logger.Severity.FATAL):
				sys.exit(1)
	


	# MARK: Dynamic Message
	class DynamicMessage(Message):
		"""
		A class that automatically fills in data based on the classes of its arguments.
		This class should be used instead of creating new subclasses of Message if possible.
		"""

		def __init__(self, severity: Logger.Severity, content: str):
			"""Sole constructor."""
			self.severity = severity
			self.content = content
		


		def log(self, *args: Any):
			content: str = self.content

			str_count: int = 0
			node_count: int = 0

			loaded_node: LoadedNode | None = None
			print_stream: TextIO = sys.stderr

			for arg in args:
				if isinstance(arg, str):
					if str_count == 0:
						content = content.replace("$CONTEXT", arg)

					content = content.replace("$CONTEXT[{}]".format(str_count), arg)
					content = content.replace("$FILENAME[{}]".format(str_count), path.basename(arg))

					str_count += 1
				
				elif isinstance(arg, DataNode):
					if node_count == 0:
						content = content.replace("$NODE", arg.name)

					content = content.replace("$NODE[{}]".format(node_count), arg.name)

					if arg.parent != None:
						if node_count == 0:
							content = content.replace("$PARENT", arg.parent.name)

						content = content.replace("$PARENT[{}]".format(node_count), arg.parent.name)

					node_count += 1
				
				elif isinstance(arg, TextIO):
					print_stream = arg

				if isinstance(arg, LoadedNode) and loaded_node == None:
					loaded_node = arg


			self.format_on(print_stream)
			self.print_prefix(print_stream)
			print_stream.write(content)

			if loaded_node != None:
				print_stream.write(" (line {0} of {1})".format(loaded_node.line, loaded_node.file))

			self.end_message(print_stream)



	# MARK: Preset Messages
	FATAL_GENERIC: Final[Logger.DynamicMessage] = DynamicMessage(Severity.FATAL, "A fatal error was encountered. No other information is available.")
	"""Fatal Error: No context"""


	ERROR_GENERIC: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "An error was encountered. No other information is available.")
	"""Error: No context"""
	ERROR_FILE_DNE: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "The file $CONTEXT does not exist or could not be found.")
	"""Error: File does not exist"""

	ERROR_NODE_PARSE_GENERIC: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "There was an error parsing the node $NODE.")
	"""Error: Node failed to parse"""

	ERROR_BUILDER_MISSING_ARG: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "Missing argument for $NODE in $CONTEXT.")
	"""Error: Node missing arg for builder"""
	ERROR_BUILDER_MALFORMED_INT: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "$NODE argument in $CONTEXT is not a valid integer.")
	"""Error: Node argument not a valid integer"""
	ERROR_BUILDER_MALFORMED_REAL: Final[Logger.DynamicMessage] = DynamicMessage(Severity.ERROR, "$NODE argument in $CONTEXT is not a real number.")
	"""Error: Node argument not a valid float"""

	WARN_NODE_WRITE_ROOT: Final[Logger.DynamicMessage] = DynamicMessage(Severity.WARN, "$NODE is a root node and should not be written.")
	"""Warning: Node is a root node and should not be written"""

	WARN_BUILDER_NATURAL_OUT_OF_BOUNDS: Final[Logger.DynamicMessage] = DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT should be a natural number, but is less than 0.")
	"""Warning: Node argument is out of the expected range for a natural number"""
	WARN_BUILDER_ROLL_OUT_OF_BOUNDS: Final[Logger.DynamicMessage] = DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT is either too large or too small to be a valid default random roll.")
	"""Warning: Node argument is out of the expected range for a default random roll"""

	WARN_BUILDER_POSREAL_OUT_OF_BOUNDS: Final[Logger.DynamicMessage] = DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT should non-negative, but is less than 0.")
	"""Warning: Node argument is out of the expected range for a positive real number"""
	WARN_BUILDER_SMALLREAL_OUT_OF_BOUNDS: Final[Logger.DynamicMessage] = DynamicMessage(Severity.WARN, "$NODE argument in $CONTEXT is outside the expected range of 0 to 1 inclusive.")
	"""Warning: Node argument is out of the expected range 0...1"""
