package de.timoh.brainfuck;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Timo Hanisch (timohanisch@gmail.com)
 */
public class BrainfuckInterpreter {

    public static final char INC_POINTER = '>';
    public static final char DEC_POINTER = '<';
    public static final char INC_CELL = '+';
    public static final char DEC_CELL = '-';
    public static final char PRINT_CELL = '.';
    public static final char READ_INPUT = ',';
    public static final char LOOP_OPEN = '[';
    public static final char LOOP_CLOSE = ']';

    public static final int DEFAULT_CELL_COUNT = 1024;

    private char[] cells;
    private char[] input;
    private int currentCell = 0;
    private int currentInterpretingCharacter = 0;

    private final DataInputStream in;

    private boolean debug = false;

    public BrainfuckInterpreter() {
        this(DEFAULT_CELL_COUNT);
    }

    public BrainfuckInterpreter(boolean debug) {
        this(DEFAULT_CELL_COUNT, debug);
    }

    public BrainfuckInterpreter(int cellCount) {
        this.cells = new char[cellCount];
        this.in = new DataInputStream(System.in);
    }

    public BrainfuckInterpreter(int cellCount, boolean debug) {
        this.cells = new char[cellCount];
        this.in = new DataInputStream(System.in);
        this.debug = debug;
    }

    public void interpret(String input) {
        prepareInterpret(input);
        doInterpretation();
    }

    private void prepareInterpret(String input) {
        if (this.debug) {
            System.out.println("Converting input");
        }
        inputToCharArray(input);
        if (this.debug) {
            System.out.println("Interpreting input");
        }
    }

    private void doInterpretation() {
        while (this.currentInterpretingCharacter < this.input.length) {
            interpretChar(this.input[this.currentInterpretingCharacter]);
            ++this.currentInterpretingCharacter;
        }
    }

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

    private void interpretChar(char c) {
        switch (c) {
            case INC_POINTER:
                if (this.currentCell + 1 < this.cells.length) {
                    ++this.currentCell;
                } else {
                    throw new RuntimeException("Segmentation fault");
                }
                break;
            case DEC_POINTER:
                if (this.currentCell - 1 >= 0) {
                    --this.currentCell;
                } else {
                    throw new RuntimeException("Segmentation fault");
                }
                break;
            case INC_CELL:
                ++this.cells[this.currentCell];
                break;
            case DEC_CELL:
                --this.cells[this.currentCell];
                break;
            case PRINT_CELL:
                System.out.print(this.cells[this.currentCell]);
                break;
            case READ_INPUT:
                try {
                    char inputChar = (char) this.in.readByte();
                    this.cells[this.currentCell] = inputChar;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case LOOP_OPEN:
                if (this.cells[this.currentCell] == 0) {
                    findMatchingCloseLoop();
                }
                break;
            case LOOP_CLOSE:
                if (this.cells[this.currentCell] != 0) {
                    findMatchingOpenLoop();
                }
                break;
            default:
            // By default, all non language characters are used as comment strings.
        }
    }

    private void findMatchingCloseLoop() {
        ++this.currentInterpretingCharacter;
        int loopDepth = 0;
        while (true) {
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
            if (this.currentInterpretingCharacter >= this.input.length) {
                throw new RuntimeException("Loop error");
            }
        }
    }

    private void findMatchingOpenLoop() {
        --this.currentInterpretingCharacter;
        int loopDepth = 0;
        while (true) {
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
            if (this.currentInterpretingCharacter < 0) {
                throw new RuntimeException("Loop error");
            }
        }
    }
}