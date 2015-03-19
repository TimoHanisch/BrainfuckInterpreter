# BrainfuckInterpreter

The BrainfuckInterpreter is a simple interpreter implementation of the Brainfuck (http://en.wikipedia.org/wiki/Brainfuck) programming language.
 
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

The default size is set to **1024**.

Maven is used to build this lib (Tested with 3.1.1).
