package chapter2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    private Profile profile;
    private BooleanQuestion question;
    private Criteria criteria;

    @BeforeEach
    void create() {
        profile =  new Profile("Bull Hockey, Inc.");
        question = new BooleanQuestion(1, "Got bonuses?");
        criteria = new Criteria();
    }

    @Test
    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
        profile.add(new Answer(question, Bool.FALSE));
        criteria.add(
                new Criterion(new Answer(question, Bool.TRUE)
                        , Weight.MustMatch));

        boolean matches = profile.matches(criteria);

        assertFalse(matches);
    }
}