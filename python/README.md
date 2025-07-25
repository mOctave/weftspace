![Latest release](https://img.shields.io/github/v/release/mOctave/weftspace)
![CI](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/ci.yml?label=CI)
![Documentation](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/docs.yml?label=Documentation)
![Release](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/release.yml?label=Release)
![Commits since last release](https://img.shields.io/github/commits-since/mOctave/weftspace/latest)

# Weftspace
Weftspace was originally written in Java, but it is now also available in Python. The Python library is slightly smaller than the Java one, but it provides all the same functionalities.

## Installation and Usage
The Weftspace Python library can be installed using pip:

```
pip install weftspace
```

You can find the full project details on [PyPI](htpps://pypi.org/project/weftspace/).

## Parser

The most common use case of Weftspace is to read data from a file into a node tree. This is accomplished by using the `DataReader` class.

Although the DataReader class does have several potentially useful methods, in the vast majority of cases a variation on the following three lines is all you need.

```python
root_node: DataNode = DataNode.create_root_node() # Creates a generic root node that you'll access your parsed nodes from later
reader: DataReader = DataReader("path/to/file", root_node) # Constructs a DataReader
reader.parse() # Parses every line in the file, writing its contents as children of root_node, and automatically handling exceptions
```

These lines should turn your file of ES-formatted data into a node tree, ready for use!

### Options

`DataReader` currently has the following options that can be used as \*args when parsing:
- `IGNORE_NODE_FLAGS`: Treats the keywords `add` and `remove` as node names rather than flags.

## Working with the Node Tree

Now that you've parsed your data, it should end up written to a (sometimes enormous) tree, with a single root node. Keep track of that root node, because it's how you access the rest of the tree!

The tree itself is made up of a whole bunch of `DataNode`s, each with three major properties: a name, a list of arguments, and a list of child nodes. Additionally, nodes include a reference to their parent node (if they aren't the root of a tree), and a special `Flag` that usually isn't all that important. Any nodes loaded using `DataReader` will also contain information about where they were loaded from for debug purposes.

Suppose you have the following lines in a datafile:

```ruby
"some node"
	description `This node is a cool node.`
	attributes "short" "helpful"
```

When parsed, this would produce three nodes:
1. A node named `some node` with no arguments and two children (`description` and `attributes`).
2. A node named `description` with one argument (`This node is a cool node.`) and no children.
3. A node named `attributes` with two arguments (`short` and `helpful`) and no children.

Both the arguments and children of any given node are presented in a list, and the `DataNode` class contains several convenience methods to with each list.

## Building Objects from Nodes

Let's face it: you probably don't want a node tree. You want to turn the nodes into objects. And you probably don't want to handle a billion exceptions that might arise if the data doesn't conform to the expected pattern. For this reason, I put together the `Builder` class, which allows you to convert a `DataNode` argument into any of several common data types, given a node, the number of the argument to build, and a "context" string that is usually the same across all instances of a class and is best defined as a constant at the top of the class you're writing a constructor for.

Here's an example of how you could convert a node to an object:

```python
	CONTEXT: Final[str] = "node-based object"
	def __init__(self, node: DataNode):
		self.name = Builder.build_string(node, 0, CONTEXT)
		for child in node.children:
			match child.name:
				case "description": # Normal string
					self.description = Builder.build_string(child, 0, CONTEXT)
				case "mass": # Non-negative integer
					self.mass = Builder.build_spec_int(child, 0, CONTEXT, Builder.IntType.NATURAL)
				case "random number for fun": # A float
					self.thingy = Builder.build_float(child, 0, CONTEXT)
				case "position": # From "position" x y
					self.x = Builder.build_int(child, 0, CONTEXT)
					self.y = Builder.build_int(child, 1, CONTEXT)
				case _:
					pass
```

## Writing Data

Occasionally, you may find that you need to write data to a file. This can be accomplished using the `DataWriter` class. This can be done almost as simply as parsing, as follows:

```python
writer: DataWriter = new DataWriter("path/to/file") # Constructs a DataWriter for the file you want to write to.
writer.open() # Opens the DataWriter so you can append to the file
writer.write(some_node) # Writes the node to the end of the file
writer.close() # Don't forget to do this, or you may have memory leaks!
```

Currently, it is not possible to overwrite or insert data using `DataWriter`.
