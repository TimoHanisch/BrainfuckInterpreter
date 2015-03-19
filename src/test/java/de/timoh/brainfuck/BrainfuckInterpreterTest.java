package de.timoh.brainfuck;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import junit.framework.TestCase;

/**
 *
 * @author  <a href="mailto:timohanisch@gmail.com">Timo Hanisch</a>
 */
public class BrainfuckInterpreterTest extends TestCase {

    public BrainfuckInterpreterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of interpret method, of class BrainfuckInterpreter.
     *
     * @throws java.io.IOException
     */
    public void testInterpret() throws IOException {
        System.out.println("Hello World - Simple");
        // Hello World Simple
        String input = new String(Files.readAllBytes(new File("examples/helloworld_simple.bf").toPath()), StandardCharsets.UTF_8);
        BrainfuckInterpreter instance = new BrainfuckInterpreter();
        instance.interpret(input);
        assertEquals("Hello World!", instance.getOutput());

        System.out.println("Hello World - Complex");
        // Hello World Complex
        input = new String(Files.readAllBytes(new File("examples/helloworld_complex.bf").toPath()), StandardCharsets.UTF_8);
        instance.interpret(input);
        assertEquals("Hello World!", instance.getOutput());

        System.out.println("Segmentation fault");
        input = "<";
        instance = new BrainfuckInterpreter(10);
        try {
            instance.interpret(input);
            fail("Should throw an segmentation fault exception");
        } catch (RuntimeException e) {
            // Ignore
        }
        input = ">>>>>>>>>>>";
        try {
            instance.interpret(input);
            fail("Should throw an segmentation fault exception");
        } catch (RuntimeException e) {
            // Ignore
        }
    }

}
