package de.timoh.brainfuck;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This interpreter implements the Brainfuck programming language
 * {@link http://en.wikipedia.org/wiki/Brainfuck}.
 *
 * @author  <a href="mailto:timohanisch@gmail.com">Timo Hanisch</a>
 */
public class BrainfuckInterpreter {

    /**
     * Reserved keywords for the language
     */
    public static final char INC_POINTER = '>';
    public static final char DEC_POINTER = '<';
    public static final char INC_CELL = '+';
    public static final char DEC_CELL = '-';
    public static final char PRINT_CELL = '.';
    public static final char READ_INPUT = ',';
    public static final char LOOP_OPEN = '[';
    public static final char LOOP_CLOSE = ']';

    /**
     * The default size for the cell array.
     */
    public static final int DEFAULT_CELL_COUNT = 1024;

    /**
     * The cells being manipulated by the program
     */
    private char[] cells;

    /**
     * The size of the cells array.
     */
    private int cellsCount;

    /**
     * The array storing the given program. Used to interpret the characters of
     * the program.
     */
    private char[] input;

    /**
     * The pointer for the current cell.
     */
    private int currentCell = 0;

    /**
     * The pointer for the current input array index
     */
    private int currentInterpretingCharacter = 0;

    /**
     * Used to read user input starting with the ',' character
     */
    private final DataInputStream in;

    /**
     * Currently only set internally. Used to output with the '.' character
     */
    private final PrintStream out;

    /**
     * If set to true debug messages are displayed while interpreting
     */
    private boolean debug = false;

    /**
     * Used to store the output of the running program. Can be accessed with
     * {@link #getOutput()} after successfully executing a program.
     */
    private StringBuilder outputBuilder;

    /**
     * Creates a BrainfuckInterpreter instance with the default cell array size
     * {@link #DEFAULT_CELL_COUNT}.
     */
    public BrainfuckInterpreter() {
        this(DEFAULT_CELL_COUNT);
    }

    /**
     * Creates a BrainfuckInterpreter instance with the default cell array size
     * {@link #DEFAULT_CELL_COUNT} and debug output to the default output
     * stream.
     *
     * @param debug - If set to {@code true} debug output is displayed
     */
    public BrainfuckInterpreter(boolean debug) {
        this(DEFAULT_CELL_COUNT, debug);
    }

    /**
     * Creates a BrainfuckInterpreter instance with the given cell array size.
     *
     * @param cellCount - Cell array size
     */
    public BrainfuckInterpreter(int cellCount) {
        this.cellsCount = cellCount;
        this.in = new DataInputStream(System.in);
        this.out = System.out;
    }

    /**
     * Creates a BrainfuckInterpreter instance with the given cell array size
     * and debug output to the default output stream.
     *
     * @param cellCount - Cell array size
     * @param debug - If set to {@code true} debug output is displayed
     */
    public BrainfuckInterpreter(int cellCount, boolean debug) {
        this.cellsCount = cellCount;
        this.in = new DataInputStream(System.in);
        this.debug = debug;
        this.out = System.out;
    }

    /**
     * Interpretes the given input string. If any error is encountered the
     * program will throw a {@link RuntimeException}.
     *
     * @param input - The Brainfuck program code
     */
    public void interpret(String input) {
        if (this.debug) {
            out.println("Converting input");
        }
        prepareInterpreter(input);
        if (this.debug) {
            out.println("Interpreting input");
        }
        doInterpretation();
    }

    /**
     * Returns the output string from the last interpretation run.
     *
     * @return
     */
    public String getOutput() {
        if (outputBuilder != null) {
            return outputBuilder.toString().trim();
        }
        return "";
    }

    /**
     * Sets the size of the cell array. The size will change on the next
     * interpretation run.
     *
     * @param size - Cell array size
     */
    public void setCellArraySize(int size) {
        this.cellsCount = size;
    }

    /**
     * Returns the count of cells used by the interpreter.
     *
     * @return
     */
    public int getCellArraySize() {
        return this.cellsCount;
    }

    /**
     * Prepares the interpreter for a new input string.
     *
     * @param input - The Brainfuck program code
     */
    private void prepareInterpreter(String input) {
        cleanup();
        inputToCharArray(input);
    }

    /**
     * Creates a new cells array and resets all pointers.
     */
    private void cleanup() {
        this.cells = new char[this.cellsCount];
        this.currentCell = 0;
        this.currentInterpretingCharacter = 0;
        this.outputBuilder = new StringBuilder();
    }

    /**
     * Iterates through the input and interpretes each character.
     */
    private void doInterpretation() {
        while (this.currentInterpretingCharacter < this.input.length) {
            interpretChar(this.input[this.currentInterpretingCharacter]);
            ++this.currentInterpretingCharacter;
        }
    }

    /**
     * Transforms the input Brainfuck code string to a character array.
     *
     * @param input - The Brainfuck program code
     */
    private void inputToCharArray(String input) {
        List<Character> inputList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] chars = token.split("");
            for (String s : chars) {
                if (!s.isEmpty()) {
                    inputList.add(s.charAt(0));
                }
            }
        }
        this.input = new char[inputList.size()];
        for (int i = 0; i < inputList.size(); i++) {
            this.input[i] = inputList.get(i);
        }
    }

    /**
     * Interpretes the given character. All illegal characters are ignored and
     * therefor can be used as comment characters.
     *
     * @param c - Character to interprete
     */
    private void interpretChar(char c) {
        switch (c) {
            // Increases the cell pointer, throws an exception if 
            // the pointer is greater than the cells array size
            case INC_POINTER:
                if (this.currentCell + 1 < this.cells.length) {
                    ++this.currentCell;
                } else {
                    throw new RuntimeException("Segmentation fault");
                }
                break;
            // Decreases the cell pointer, throws an exception if 
            // the pointer is less than the cells array size
            case DEC_POINTER:
                if (this.currentCell - 1 >= 0) {
                    --this.currentCell;
                } else {
                    throw new RuntimeException("Segmentation fault");
                }
                break;
            // Increases the value of the current cell being operated on
            case INC_CELL:
                ++this.cells[this.currentCell];
                break;
            // Decreases the value of the current cell being operated on
            case DEC_CELL:
                --this.cells[this.currentCell];
                break;
            // Prints the current cell to the output stream and saves
            // the output to the StringBuilder
            case PRINT_CELL:
                out.print(this.cells[this.currentCell]);
                outputBuilder.append(this.cells[this.currentCell]);
                break;
            // Reads the input from the stdin stream, throws
            // an exception if the input is an illegal character
            case READ_INPUT:
                try {
                    char inputChar = (char) this.in.readByte();
                    this.cells[this.currentCell] = inputChar;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            // Stops a loop if the current cell is equal to 0,
            // otherwise repeats the content of the loop
            case LOOP_OPEN:
                if (this.cells[this.currentCell] == 0) {
                    findMatchingCloseLoop();
                }
                break;
            // Jumps to the start of the matching opening loop bracket if the 
            // value of the current cell is not equal to 0
            case LOOP_CLOSE:
                if (this.cells[this.currentCell] != 0) {
                    findMatchingOpenLoop();
                }
                break;
            default:
            // By default, all non language characters are used as comment strings.
        }
    }

    /**
     * Tries to find the matching closing loop bracket.
     */
    private void findMatchingCloseLoop() {
        ++this.currentInterpretingCharacter;
        // Used to find the correct matching bracket when nested loops are present
        int loopDepth = 0;
        while (true) {
            // Inspect the current character to interpret and check if it 
            // is a closing loop and then jump right behind it. Otherwise check 
            // if the found character is an opening loop bracket, if so increase 
            // the loop depth since a nested loop was found, otherwise we can 
            // ignore the character
            switch (this.input[this.currentInterpretingCharacter]) {
                case LOOP_OPEN:
                    ++loopDepth;
                    break;
                case LOOP_CLOSE:
                    if (loopDepth <= 0) {
                        return;
                    }
                    --loopDepth;
                    break;
                default:
                    break;
            }
            ++this.currentInterpretingCharacter;
            // If the closing loop bracket cannot be found and therfor the end 
            // is reached, throw an exception
            if (this.currentInterpretingCharacter >= this.input.length) {
                throw new RuntimeException("Loop error");
            }
        }
    }

    /**
     * Tries to find the matching opening loop bracket.
     */
    private void findMatchingOpenLoop() {
        --this.currentInterpretingCharacter;
        int loopDepth = 0;
        while (true) {
            // Inspect the current character to interpret and check if it 
            // is an opening loop and then jump right behind it. Otherwise check 
            // if the found character is a closing loop bracket, if so decrease 
            // the loop depth since a nested loop was exited, otherwise we can 
            // ignore the character
            switch (this.input[this.currentInterpretingCharacter]) {
                case LOOP_OPEN:
                    if (loopDepth == 0) {
                        return;
                    }
                    --loopDepth;
                    break;
                case LOOP_CLOSE:
                    loopDepth++;
                    break;
                default:
                    break;
            }
            --this.currentInterpretingCharacter;
            // If the opening loop bracket cannot be found and therfor the end 
            // is reached, throw an exception
            if (this.currentInterpretingCharacter < 0) {
                throw new RuntimeException("Loop error");
            }
        }
    }
}
