![Latest release](https://img.shields.io/github/v/release/mOctave/weftspace)
![CI](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/ci?label=CI)
![Documentation](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/docs.yml?label=Documentation)
![Release](https://img.shields.io/github/actions/workflow/status/mOctave/weftspace/release.yml?label=Release)
![Commits since last release](https://img.shields.io/github/commits-since/mOctave/weftspace/latest)

# Weftspace
Weftspace was originally written in Java, and the Java library continues to provide the maximum possible level of support for Endless Sky datafile syntax. As well as providing read/write functionality through the DataReader and DataWriter classes, it provides support for handling the data nodes themselves.

## Installation and Usage
The Weftspace Java library is best used with Maven. To use it, add both a repository and a dependency to your `pom.xml` file. Something along the following lines should work:

```xml
  <repositories>
	[...]
    <repository>
      <id>github</id>
      <name>Weftspace on GitHub</name>
      <url>https://maven.pkg.github.com/mOctave/weftspace</url>
      <releases><enabled>true</enabled></releases>
      <snapshots><enabled>true</enabled></snapshots>
    </repository>
	[...]
  </repositories>
```

As a dependency, you'll need to add:
```xml
  <dependencies>
	[...]
    <dependency>
     <groupId>io.github.moctave.weftspace</groupId>
      <artifactId>weftspace</artifactId>
      <version>0.1.12</version>
    </dependency>
	[...]
  </dependencies>
```

In order to make Maven play nicely with GitHub, you may also need to add the following to your global `settings.xml` file (usually located somewhere like `~/.m2/settings.xml`):

```xml
  <servers>
	[...]
    <server>
      <id>github</id>
      <username>[your username here]</username>
      <password>[personal access token with read perms]</password>
    </server>
	[...]
  </servers>
```

Now, assuming everything went right, you should be able to install with `mvn install` and use Weftspace for your project!

## Parser

The most common use case of Weftspace is to read data from a file into a node tree. This is accomplished by using the `DataReader` class.

Although the DataReader class does have several potentially useful methods, in the vast majority of cases a variation on the following four lines is all you need.

```java
File file = new File("path/to/file"); // From java.io.File
DataNode rootNode = new DataNode(); // Creates a generic root node that you'll access your parsed nodes from later
DataReader reader = new DataReader(file, rootNode); // Constructs a DataReader
reader.parse(); // Parses every line in the file, writing its contents as children of rootNode, and automatically handling exceptions
```

These lines should turn your file of ES-formatted data into a node tree, ready for use!

### Options

`DataReader` currently has the following options that can be used as varargs when parsing:
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

Both the arguments and children of any given node are presented in a list, and the `DataNode` class contains several convenience methods to with each list. I highly recommend reading the Javadocs for the library (located at https://moctave.github.io/weftspace/) for a complete listing of all the classes and methods available.

## Building Objects from Nodes

Let's face it: you probably don't want a node tree. You want to turn the nodes into objects. And you probably don't want to handle a billion exceptions that might arise if the data doesn't conform to the expected pattern. For this reason, I put together the `Builder` class, which allows you to convert a `DataNode` argument into any of several common data types, given a node, the number of the argument to build, and a "context" string that is usually the same across all instances of a class and is best defined as a constant at the top of the class you're writing a constructor for.

Here's an example of how you could convert a node to an object:

```java
	public static final String CONTEXT = "node-based object";
	public YourObject(DataNode node) {
		setName(Builder.buildString(node, 0, CONTEXT));
		for (DataNode child : node.getChildren()) {
			switch (child.getName()) {
				case "description": // Normal string
					setDescription(Builder.buildString(child, 0, CONTEXT));
					break;
				case "mass": // Non-negative integer
					setMass(Builder.buildInt(child, 0, CONTEXT, Builder.IntType.NATURAL));
					break;
				case "random number for fun": // Any double-precision float
					setThingy(Builder.buildDouble(child, 0, CONTEXT));
					break;
				case "position": // From "position" x y
					setX(Builder.buildInt(child, 0, CONTEXT));
					setY(Builder.buildInt(child, 1, CONTEXT));
					break;
			}
		}
	}
```

## Writing Data

Occasionally, you may find that you need to write data to a file. This can be accomplished using the `DataWriter` class. This can be done almost as simply as parsing, as follows:

```java
File file = new File("path/to/file"); // The file you want to write to
DataWriter writer = new DataReader(file); // Constructs a DataWriter for the file
writer.open(); // Opens the DataWriter so you can add to the file
writer.write(someNode); // Writes the node to the end of the file
writer.close(); // Don't forget to do this, or else you may have memory leaks!
```

Currently, it is not possible to overwrite or insert data using `DataWriter`.

## Javadoc

Read the Javadoc! It is located at https://moctave.github.io/weftspace/ and updated automatically for every release.

## Contributing and Contact Information

All forms of contribution are welcome! If you've found a bug, want a feature, or would like to contribute your own code to Weftspace, feel free to open an issue or PR. You can reach me anytime on Discord or by email (moctave31415@gmail.com).
