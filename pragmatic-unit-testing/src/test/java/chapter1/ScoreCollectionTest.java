package chapter1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreCollectionTest {

    /**
     * 항상 그 테스트가 실패하는지 확인하는 것을 고려해라.
     * 의도하지않게 생각하는 것을 실제로 검증하지않는 나쁘고 품이 많이 드는 테스트를 작성할 수도 있다.

     * 아래 코드를 보고 물어봐라
     *  - 코드가 정상적으로 동작하는지 확신하려고 추가적인 테스트를 작성할 필요가 있는가?
     *  - 내가 클래스에서 결함이나 한계점을 드러낼 수 있는 테스트를 작성할 수 있을까?
     */
    @DisplayName("5와 7을 더해서 6을 평균으로 반환하는지 확인")
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