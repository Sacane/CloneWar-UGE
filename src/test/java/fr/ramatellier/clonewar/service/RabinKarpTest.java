package fr.ramatellier.clonewar.service;

import fr.ramatellier.clonewar.instruction.InstructionBuilder;
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
        var score = RabinKapService.onInstructions(testInstructions, clonedInstructions);
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
        assertTrue(score <= 60);
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

    @Test
    public void test6() throws IOException{
        var score = scoreFromFile("StringUtils-1.0.jar", "StringUtilsClone-1.0.jar");
        assertTrue(score <= 10);
    }

    @Test
    public void test7() throws IOException{
        var score = scoreFromFile("StringUtils-2.0.jar", "StringUtilsClone-2.0.jar");
        assertTrue(score <= 20);
    }

    @Test
    public void test8() throws IOException {
        var score = scoreFromFile("slice-1.0.jar", "slicecloned-1.0.jar");
        assertTrue(score >= 70);
    }

    @Test
    public void test9() throws IOException {
        var score = scoreFromFile("numeric-1.0.jar", "numericcloned-1.0.jar");
        assertTrue(score >= 50);
    }

    @Test
    public void test10() throws IOException {
        var score = scoreFromFile("series-1.0.jar", "seriescloned-1.0.jar");
        assertTrue(score >= 60);
    }

    @Test
    public void test11() throws IOException {
        var score = scoreFromFile("Test4-1.0.jar", "ClonedTest4-1.0.jar");
        assertTrue(score <= 50);
    }

    @Test
    public void test12() throws IOException {
        var score = scoreFromFile("Test5-1.0.jar", "ClonedTest5-1.0.jar");
        assertTrue(score >= 80);
    }

    @Test
    public void test13() throws IOException {
        var score = scoreFromFile("Test6-1.0.jar", "ClonedTest6-1.0.jar");
        assertTrue(score >= 90);
    }

    @Test
    public void test14() throws IOException {
        var score = scoreFromFile("Test7-1.0.jar", "ClonedTest7-1.0.jar");
        assertTrue(score <= 20);
    }

    @Test
    public void test15() throws IOException {
        var score = scoreFromFile("Test8-1.0.jar", "ClonedTest8-1.0.jar");
        assertTrue(score <= 20);
    }
}
