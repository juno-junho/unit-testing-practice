package com.luv2code.junitdemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class AssumptionsDemo {

    @Test
    void testOnlyOnCiServer() {
        assumeTrue("CI".equals(System.getenv("ENV")));
        // remainder of test
    }

    @Test
    void testOnlyOnDeveloperWorkstation() {
        assumeTrue("C:\\Java\\jDdk1.8.0_251".equals(System.getenv("JAVA_HOME")),
                () -> "Aborting test: not on developer workstation");
        // remainder of test
    }

    public static void main(String[] args) {
        String env = System.getenv("JAVA_HOME");
        System.out.println(env);
    }
}
