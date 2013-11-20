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
public class Interpreter {

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

    public Interpreter() {
        this(DEFAULT_CELL_COUNT);
    }

    public Interpreter(int cellCount) {
        cells = new char[cellCount];
        in = new DataInputStream(System.in);
    }

    public void interpret(String input) {
        prepareInterpret(input);
        doInterpretation();
    }

    private void prepareInterpret(String input) {
        System.out.println("Converting input");
        inputToCharArray(input);
        System.out.println("Interpreting input");
    }

    private void doInterpretation() {
        while (currentInterpretingCharacter < input.length) {
            interpretChar(input[currentInterpretingCharacter]);
            currentInterpretingCharacter++;
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
                currentCell++;
                break;
            case DEC_POINTER:
                currentCell--;
                if (currentCell < 0) {
                    currentCell = cells.length - 1;
                }
                break;
            case INC_CELL:
                cells[currentCell]++;
                break;
            case DEC_CELL:
                cells[currentCell]--;
                break;
            case PRINT_CELL:
                System.out.print(cells[currentCell]);
                break;
            case READ_INPUT:
                try {
                    char inputChar = (char)in.readByte();
                    cells[currentCell] = inputChar;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case LOOP_OPEN:
                if (cells[currentCell] == 0) {
                    findMatchingCloseLoop();
                }
                break;
            case LOOP_CLOSE:
                if (cells[currentCell] != 0) {
                    findMatchingOpenLoop();
                }
                break;
            default:
            // Ignore Input
        }
    }

    private void findMatchingCloseLoop() {
        currentInterpretingCharacter++;
        int loopDepth = 0;
        while (true) {
            switch (input[currentInterpretingCharacter]) {
                case LOOP_OPEN:
                    loopDepth++;
                    break;
                case LOOP_CLOSE:
                    if (loopDepth <= 0) {
                        return;
                    }
                    loopDepth--;
                    break;
                default:
                    break;
            }
            currentInterpretingCharacter++;
        }
    }

    private void findMatchingOpenLoop() {
        currentInterpretingCharacter--;
        int loopDepth = 0;
        while (true) {
            switch (input[currentInterpretingCharacter]) {
                case LOOP_OPEN:
                    if (loopDepth == 0) {
                        return;
                    }
                    loopDepth--;
                    break;
                case LOOP_CLOSE:
                    loopDepth++;
                    break;
                default:
                    break;
            }
            currentInterpretingCharacter--;
        }
    }
}
