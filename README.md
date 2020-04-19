# README

## Which partner’s Stars and tIMDb were used
Stars from Calder and tIMDb from Seiji. We pretty much left Stars as it was, but we changed tIMDb a bit to make the 
graphs more generalizable.

## Partner division of labor
Because we were working at a distance, we did more things separately than we would have in person. However, before 
doing anything, we always discussed the design together, to make sure we agreed on how to implement things and both 
understood how they would work at a high level. After either of us made a part, the other would read over the code to 
understand it.

Seiji:
- New graph design
- A* search, haversine calculation, and integrating those with Dijkstra and `Locatable`
- Most of the testing

Calder:
- New REPL design
- KDTree integration
- New `DatabaseProxy`s

Both:
- Writing the commands (which integrated both the graphs and the REPL)
- Connecting the GUIs

We decided that Seiji would work on the graphs because the new graph design is based on his older design from tIMDb, 
which he understood better than Calder did. This naturally led to his working on the graph search algorithm and 
integrating it with `Locatable`. We decided that Calder would work on the REPL because the new design 
draws a bit more from his old REPL than from Seiji’s, and because Calder had already come up with the idea of storing 
arguments in a tree. Because of other assignments, Calder started working a bit later than Seiji did, so he did not end 
up having as much time to write tests.

## Known bugs
No known bugs.

## Design details specific to your code, including how you fit each of the prior project’s codebases together
### Graph
The graph package is based on Seiji’s graph implementation, with some changes to make it more generalizable (removing 
assumptions made in tIMDb).

The classes and interfaces in the graph package has parameters `T` and `W`, which indicate the types of 
objects that are stored in the vertices and edges, respectively. The `Vertex` and `Edge` interfaces have methods to 
return the objects they store. A `Vertex` can also return the edges leading out of the vertex, and an `Edge` can return 
its weight and the vertices at the source and destination. A `Graph` has one method, which is to return the `Vertex` 
that stores a certain value within the `Graph`.

The `SourcedGraph` and associated classes are an implementation of the above interfaces that
represent a graph that pulls its data from an external source, such as a 
SQL database. The `GraphSource` interface creates the connection between the graph and the database, defining how to 
get vertices from the graph and how to find the edges leading out of a vertex. The `SourcedVertex` is an edge
that will lazily call for its edges from its `GraphSource`, and the `SourceEdge` object is an object created to be
easily modified by mutator methods for user-defined behavior.

### Data interaction
For Maps, our `World` and `GraphSource` use `DatabaseProxy`s to define different types of queries to the database and 
to cache results. (The `DatabaseProxy` interfaces used here are based on one from Calder’s tIMDb, but expanded to allow 
for more input/output options.) We also used `DatabaseProxy`s for tIMDb.

Our Stars is pretty much the same as Calder’s Stars implementation, so it uses the `CSVParser` from that (although it 
would have been nice to make a better one using regex).

We tried to implement as much logic as we could into our SQL queries to take advantage of the highly-optimized
SQL data retrieval process. This yielded very specific SQL queries tailored to each command (save for
the queries that supply the `GraphSource`, as they are required to be more general). Here are a few examples:
-  `AllTraversableNodesQuery` retrieves all traversable nodes in the maps database. This query was able to run
    within 5-6 seconds on our laptops, and the `KDTree` took an addition 3-4 seconds to construct the entire tree.
    We thought ~10 seconds of overhead on database load-in was reasonable, and decided to construct the entire tree
    without any explicit caching infrastructure.
-   `CrossStreetToNodesQuery` retrieves all traversable nodes at the intersection of two names streets. 
-   `BoundBoxToWaysQuery` retrieves all ways within the bounded-box.

For efficiency, we chose to include a more complex-row validation procedure on a large table <code>JOIN</code>
rather than a <code>UNION</code> of simpler smaller-subqueries (and table <code>JOIN</code>s).


### REPL
This REPL combines ideas from both of our earlier implementations.

From Calder:
- `World` classes for each application/project, which store the commands, keep track of data, and allow other objects 
(e.g. GUI things) to interact with the data.
- The `REPL` class, which runs commands for any number of `World`s.
- The exception handling system.

From Seiji:
- The idea of having each command associated with certain types of arguments, represented using `ArgType` objects.
- Using `ArgType`s to identify whether the input to a command is valid.
- Using `ArgType`s to parse a command.

New things (added by Calder):
- `ArgType`s can convert valid `String`s to the types they represent.
- `ArgType`s are stored in a _tree_ (of `Argument`s), rather than an array, to allow for a `Command` to accept multiple
types of input (e.g. `route`, which can either take four numbers or four strings).
- Each `Argument` has a method that gets the “child” `Argument`s (the ones that can come
next). These could be generated on the spot (allowing for a command with arbitrarily many arguments), or they can be
stored in a list (here, a `ListArgument`).
- When the user inputs a correct command name but with invalid input, the `Command` automatically generates a specific 
error message based on the argument structure.
- `SimpleCommand`s, `SimpleArgument`s, and `CommandRunner`s, so that you can make commands without having to think 
about trees.

A lot of the things that were interfaces are now abstract classes, since that allows for better encapsulation and allows
more functionality to be passed on to subclasses.

### A* Search Algorithm
In timdb, Seiji defined a `GraphSearch` interface to define the behavior of any path-searching algorithm on a `Graph`.
`AStar` is yet another search algorithm to implement that interface. Because implementations of
 the Dijkstra and A* algorithms only differ in the calculation of the path weight, the local-search logic was abstracted
 away (as `LocalSearch`) with new abstract `Path` object to define the behavior of a path necessary for the local search. 
 Our implementations of Dijkstra and A* each implement this `Path` object (where `AStar` adds the global distance from
 the path end to the goal) and then use the machinery offered `LocalSearch` to run the algorithm.
 
 To allow for tight integration with the `KDTree`, we ensure that `AStar` must run on objects that implement
 the `Locatable` interface (defined in Stars) for consistent Haversine distance calculation.
 
## Any runtime/space optimizations you made beyond the minimum requirements
We don’t think there are any, aside from how we used SQL queries.

## How to run your tests

### Junit Tests
run <code>mvn test</code>

### System Tests
Our tests are divided into "universal" and "local" tests. Universal tests can be run on any machine, whereas local
tests must be run after copying large database data into the correct directory.

Running universal tests: <code>./cs32-test tests/student/maps/universal_tests/*.test</code>

Running local tests: copy `maps.sqlite3` into the <code>data/maps</code> directory. Then run
<code>./cs32-test tests/student/maps/local_tests/*.test --timeout 120</code>


Note (from Calder): The output from `route_pembroke_cit.test` was generated by our code rather than by hand. However, 
I only added it as a test after I confirmed that it was a good path by getting the coordinates of all the nodes,  
mapping them in Google Earth, and checking that the path appears correct. This route is not the shortest route on the 
map — rather than using Thayer St. (which OpenStreetMap correctly says to use), it uses Brown St. However, we believe 
this is because `maps.sqlite3` does not contain all of the OpenStreetMap data (in particular, it appears not to have 
the sidewalks on Thayer), so it is unable to find the connection from Meeting to Thayer. (Using Dijkstra’s algorithm 
gives the same result, so it’s not an A* problem.)

## Any tests you wrote and tried by hand
Calder: I created a `World` with some `Command`s to test that the REPL parses the input correctly, runs the right 
commands based on the input types, and gives the intended error messages. This test is in the `repl` branch.

## How to build/run your program from the command line
In project directory, run `./run`. To start the GUI interface, include the `--gui` tag, and then open
 [localhost:4567/timdb](localhost:4567/timdb) in a browser.

## What browser you used to test your GUI (we prefer Chrome, but we'll accept other common web browsers)
When Seiji was making tIMDb, he used Brave (chromium-based).

When Calder was making Stars, he used Safari.
