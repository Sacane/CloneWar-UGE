package fr.ramatellier.clonewar.rabinkarp;

import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import fr.ramatellier.clonewar.rabin.RabinKarp;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RabinKarpTest {

    private static final String TEST_PATH = "src/test/resources/tests/";
    private int scoreFromFile(String p1, String p2) throws IOException {
        var test = Path.of(TEST_PATH + p1);
        var clonedTest = Path.of(TEST_PATH + p2);
        var testBytes = Files.readAllBytes(test);
        var clonedBytes = Files.readAllBytes(clonedTest);
        var testInstructions = InstructionBuilder.buildInstructionFromJar("test", testBytes);
        var clonedInstructions = InstructionBuilder.buildInstructionFromJar("cloned", clonedBytes);
        var score = RabinKarp.onInstructions(testInstructions, clonedInstructions);
        System.out.println(score);
        return score;
    }

    @Test
    public void test2() throws IOException {
        var score = scoreFromFile("Test2-1.0.jar", "ClonedTest2-1.0.jar");
        assertTrue(score >= 80);
    }

    @Test
    public void test1() throws IOException {
        var score = scoreFromFile("json-1.0.jar", "jsoncloned-1.0.jar");
        assertTrue(score <= 45);
    }

    @Test
    public void test5() throws IOException {
        var score = scoreFromFile("ymca-0.1.jar", "ymcacloned-1.0.jar");
        assertTrue(score > 50);
    }

    @Test
    public void test3() throws IOException{
        var score = scoreFromFile("ClonedTest3-1.0.jar", "Test3-1.0.jar");
        assertTrue( score >= 50);
    }

    @Test
    public void test4() throws IOException{
        var score = scoreFromFile("Wrapper-1.0.jar", "WrapperCloned-1.0.jar");
        assertTrue(score >= 90);
    }
}
