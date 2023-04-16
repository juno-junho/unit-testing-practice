package chapter3;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertTest {
    static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }

        private static final long serialVersionUID = 1L;
    }

    static class Account {
        int balance;

        String name;

        Account(String name) {
            this.name = name;
        }

        void deposit(int dollars) {
            balance += dollars;
        }

        void withdraw(int dollars) {
            if (balance < dollars) {
                throw new InsufficientFundsException("balance only " + balance);
            }
            balance -= dollars;
        }

        public String getName() {
            return name;
        }

        public int getBalance() {
            return balance;
        }

        public boolean hasPositiveBalance() {
            return balance > 0;
        }
    }

    private Account account;

    @BeforeEach
    public void createAccount() {
        this.account = new Account("an account name");
    }

    @Test
    void hasPositiveBalance() {
        account.deposit(50);
        assertTrue(account.hasPositiveBalance());
    }

    @Test
    public void depositIncreasesBalance() {
        int initialBalance = account.getBalance();
        account.deposit(100);
//        assertTrue(account.getBalance() < initialBalance); // 안좋은 방법
        assertThat(account.getBalance()).isGreaterThan(initialBalance);
        assertThat(account.getBalance()).isEqualTo(100);
    }


    @Test
    @DisplayName("부동 소수점 비교")
    void floatAndDoubleTest() {
//        assertThat(2.32 * 3).isEqualTo(6.96);    // 실패한다.
        assertThat(2.32 * 3).isCloseTo(6.96, Percentage.withPercentage(0.00005));
    }

   /* @Test
    @Disabled
    public void assertFailure() {
        assertTrue(account.getName().startsWith("xyz"));
    }

    @Test
    @Disabled
    public void matchesFailure() {
        assertThat(account.getName()).startsWith("xyz");
    }*/
}
