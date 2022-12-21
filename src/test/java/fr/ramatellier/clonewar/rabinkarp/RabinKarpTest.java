package fr.ramatellier.clonewar.rabinkarp;

import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import fr.ramatellier.clonewar.rabin.RabinKarp;
import fr.ramatellier.clonewar.util.AsmParser;
import fr.ramatellier.clonewar.util.ByteResourceReader;
import fr.ramatellier.clonewar.util.JarReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class RabinKarpTest {

    private int scoreFromPath(String p1, String p2) throws IOException {
        var test = Path.of("src/test/resources/tests/" + p1);
        var clonedTest = Path.of("src/test/resources/tests/" + p2);
        var testBytes = assertDoesNotThrow(() -> Files.readAllBytes(test));
        var clonedBytes = assertDoesNotThrow(() -> Files.readAllBytes(clonedTest));

        var testInstructions = InstructionBuilder.buildInstructionFromJar("test", testBytes);
        var clonedInstructions = InstructionBuilder.buildInstructionFromJar("cloned", clonedBytes);
        return RabinKarp.compareJarInstructions(testInstructions, clonedInstructions);
    }

    @Test
    public void test2() throws IOException {
        var score = scoreFromPath("Test2-1.0.jar", "ClonedTest2-1.0.jar");
        assertTrue(score > 80);
    }

    @Test
    public void jsonTest() throws IOException {
        var score = scoreFromPath("json-1.0.jar", "jsoncloned-1.0.jar");
        assertTrue(score > 50);
    }

    @Test
    public void ymcaTest() throws IOException {
        var score = scoreFromPath("ymca-0.1.jar", "ymcacloned-1.0.jar");
        System.out.println(score);
        assertTrue(score > 50);
    }
}
