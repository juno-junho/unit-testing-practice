package chapter12;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileTest {

    private Profile profile;
    private BooleanQuestion questionIsThereLocation;
    private Answer answerThereIsLocation;

    @BeforeEach
    void createProfile() {
        profile = new Profile();
    }

    @BeforeEach
    void createQuestionAndAnswer() {
        questionIsThereLocation = new BooleanQuestion(1, "Relocation package?");
        answerThereIsLocation = new Answer(questionIsThereLocation, Bool.TRUE);
    }

    @Test
    void matchesNotingWhenProfileEmpty() {
        Criterion criterion = new Criterion(answerThereIsLocation, Weight.DontCare);

        boolean result = profile.matches(criterion);

        assertThat(result).isFalse();
    }

    @Test
    void matchesWhenProfileContainsMatchingAnswer() {
        profile.add(answerThereIsLocation);
        Criterion criterion = new Criterion(answerThereIsLocation, Weight.Important);

        boolean result = profile.matches(criterion);

        assertThat(result).isTrue();
    }
}
