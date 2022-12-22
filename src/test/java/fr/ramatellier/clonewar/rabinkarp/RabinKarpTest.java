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
    private int scoreFromPath(String p1, String p2) throws IOException {
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
        var score = scoreFromPath("Test2-1.0.jar", "ClonedTest2-1.0.jar");
        assertTrue(score >= 80);
    }

    @Test
    public void jsonTest() throws IOException {
        var score = scoreFromPath("json-1.0.jar", "jsoncloned-1.0.jar");
        assertTrue(score <= 45);
    }

    @Test
    public void ymcaTest() throws IOException {
        var score = scoreFromPath("ymca-0.1.jar", "ymcacloned-1.0.jar");
        assertTrue(score <= 50);
    }

    @Test
    public void test3() throws IOException{
        var score = scoreFromPath("ClonedTest3-1.0.jar", "Test3-1.0.jar");
        assertTrue( score >= 50);
    }
}
