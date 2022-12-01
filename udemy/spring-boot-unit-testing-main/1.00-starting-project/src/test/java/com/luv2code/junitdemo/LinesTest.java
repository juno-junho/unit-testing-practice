package com.luv2code.junitdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LinesTest {

    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals() {
        ArrayList<String> expectedList = new ArrayList<>(List.of("luv", "2", "code"));
        LinkedList<String> actualList =  new LinkedList<>(List.of("luv", "2", "code"));

        assertIterableEquals(expectedList, actualList, "Expected list should be same as actual list");
    }

    @DisplayName("Lines Match")
    @Test
    void testLinesMatchExpectLineEquals() {
        List<String> expectedList = List.of("luv", "2", "code");
        List<String> actualList = List.of("luv", "2", "code");

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }

    @DisplayName("Regex for any number")
    @Test
    void testLinesMatchRegularExpressionMatchAnyNumber() {
        List<String> expectedList = List.of("luv", "\\d+", "code");
        List<String> actualList = List.of("luv", "9", "code");

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }

    @DisplayName("Regex for numbers 2 through 5")
    @Test
    void testLinesMatchRegularExpressionMatchNumbers2Through7() {
        List<String> expectedList = List.of("luv", "[2-7]+", "code");
        List<String> actualList = List.of("luv", "5", "code");

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }

    @DisplayName("Fast-forward marker, skip expected lines with markers")
    @Test
    void testLinesMatchFastForward() {
        List<String> expectedList = List.of("luv", "2", ">> just ignore this line >>", "code");
        List<String> actualList = List.of("luv", "2", "", "random","string", "code");

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }

    @DisplayName("Fast-forward marker, skip 4 lines")
    @Test
    void testLinesMatchFastForwardSkipLines() {
        List<String> expectedList = List.of("luv", "my", ">> 4 >>", "code");
        List<String> actualList = List.of("luv", "my", "one", "two", "three", "four", "code");

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }

    @DisplayName("Fast-forward marker, skip 4 lines - using files")
    @Test
    void testLinesMatchFastForwardSkipLinesUsingFiles() throws IOException {
        List<String> expectedList = Files.readAllLines(Paths.get("src/test/resources/expected-data-file.txt"));
        List<String> actualList = Files.readAllLines(Paths.get("src/test/resources/actual-data-file.txt"));

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
    @DisplayName("Fast-forward marker, skip 4 lines - using files streams")
    @Test
    void testLinesMatchFastForwardSkipLinesUsingFilesStreams() throws IOException {

        Stream<String> expectedList = Files.lines(Paths.get("src/test/resources/expected-data-file.txt"));
        Stream<String> actualList = Files.lines(Paths.get("src/test/resources/actual-data-file.txt"));

        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
}