package chapter12;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileTest {

    private Profile profile;
    private BooleanQuestion questionIsThereLocation;
    private Answer answerThereIsLocation;
    private Answer answerThereIsNoRelocation;

    @BeforeEach
    void createProfile() {
        profile = new Profile();
    }

    @BeforeEach
    void createQuestionAndAnswer() {
        questionIsThereLocation = new BooleanQuestion(1, "Relocation package?");
        answerThereIsLocation = new Answer(questionIsThereLocation, Bool.TRUE);
        answerThereIsNoRelocation = new Answer(questionIsThereLocation, Bool.FALSE);
    }

    @Test
    void doesNotMatchWhenNoMatchingAnswer() {
        profile.add(answerThereIsNoRelocation);
        Criterion criterion = new Criterion(answerThereIsLocation, Weight.DontCare);

        boolean result = profile.matches(criterion);

        assertThat(result).isFalse();
    }

    @Test
    void matchesWhenContainsMultipleAnswers() {
        profile.add(answerThereIsLocation);
        profile.add(answerThereIsNoRelocation);

        Criterion criterion = new Criterion(answerThereIsLocation, Weight.DontCare);

        boolean result = profile.matches(criterion);

        assertThat(result).isTrue();
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
