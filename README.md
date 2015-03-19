# BrainfuckInterpreter v1.0.1-Snapshot

** This branch is used for the snapshot version ** 

The BrainfuckInterpreter is a simple interpreter implementation of the Brainfuck (http://en.wikipedia.org/wiki/Brainfuck) programming language.

## Setup
Maven is used to build this lib (Tested with 3.1.1).
```
# Use maven install to install the library to your local repository
mvn install
```

To create an instance, all you need to do is:

```Java
BrainfuckInterpreter interpreter = new BrainfuckInterpreter();
interpreter.interpret("some brainfuck code");
```

When initializing the interpreter you have the possibility to chose the size of the used cell array, which are used by the interpreter. 

```Java
BrainfuckInterpreter interpreter = new BrainfuckInterpreter(128);
interpreter.interpret("some brainfuck code");
```

The default size chosen for the cell array in this interpreter is **1024**.

## Tests
There are currently four test cases.
* Hello World - Simple, which runs a simple implementation of "Hello World!"
* Hello World - Complex, running a more complex "Hello World!" program, crashing some interpreters according to http://esolangs.org/wiki/Brainfuck
* Segmentation Fault, testing if runtime exceptions are thrown if the cell pointer leaves the range of the cell array

## Credits
I've included some code examples which can be run with the interpreter. Those example were found at:
* http://esolangs.org/wiki/Brainfuck
* http://esoteric.sange.fi/brainfuck/utils/mandelbrot/ (Created by Erik Bosman)
* http://www.hevanet.com/cristofd/brainfuck/ (Created by Daniel B. Cristofani)

## License

The MIT License (MIT), for more information see LISENCE.txt
