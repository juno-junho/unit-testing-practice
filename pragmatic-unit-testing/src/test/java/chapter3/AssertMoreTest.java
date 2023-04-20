package chapter3;

import org.junit.jupiter.api.*;

public class AssertMoreTest {
    @BeforeAll
    public static void initializeSomethingReallyExpensive() {
            // ...
        System.out.println("initializeSomethingReallyExpensive");
    }

    @AfterAll
    public static void cleanUpSomethingReallyExpensive() {
        // ...
        System.out.println("cleanUpSomethingReallyExpensive");
    }

    @BeforeEach
    public void createAccount() {
        // ...
        System.out.println("createAccount");
    }

    @AfterEach
    public void closeConnections() {
        // ...
        System.out.println("closeConnections");
    }

    @Test
    public void depositIncreasesBalance() {
        // ...
        System.out.println("depositIncreasesBalance");
    }

    @Test
    public void hasPositiveBalance() {
        // ...
        System.out.println("hasPositiveBalance");
    }
}