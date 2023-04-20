package chapter3;

import chapter1.ScoreCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreCollectionTest {
    @Test
    void answersArithmeticMeanOfTwoNumbers() {
        // 준비 (arrange)
        ScoreCollection collection = new ScoreCollection();
        collection.add(() -> 5);
        collection.add(() -> 7);

        // 실행 (act)
        int actualResult = collection.arithmeticMean();

        // 단언 (assert)
        assertThat(actualResult).isEqualTo(6);
    }

}