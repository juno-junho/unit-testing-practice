package chapter12;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    void matchAgainstNullAnswerReturnsFalse() {
        assertThat(new Answer(new BooleanQuestion(0, ""), Bool.TRUE).match(null))
                .isFalse();
    }

}