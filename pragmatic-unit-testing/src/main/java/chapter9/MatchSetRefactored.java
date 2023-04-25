package chapter9;

import chapter2.Answer;
import chapter2.Criteria;
import chapter2.Criterion;
import chapter2.Weight;

import java.util.Map;

public class MatchSetRefactored {
    private Map<String, Answer> answers;
    private int score = 0;
    private Criteria criteria;

    public MatchSetRefactored(Map<String, Answer> answers, Criteria criteria) {
        this.answers = answers;
        this.criteria = criteria;
        calculateScore();
    }

    private void calculateScore() {
        // ...
        for (Criterion criterion: criteria)
            if (criterion.matches(answerMatching(criterion)))
                score += criterion.getWeight().getValue();
    }
    // ...

    private Answer answerMatching(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public int getScore() {
        return score;
    }

    public boolean matches() {
        if (doesNotMeetAnyMustMatchCriterion())
            return false;
        return anyMatches();
    }

    private boolean doesNotMeetAnyMustMatchCriterion() {
        // ...
        for (Criterion criterion: criteria) {
            boolean match = criterion.matches(answerMatching(criterion));
            if (!match && criterion.getWeight() == Weight.MustMatch)
                return true;
        }
        return false;
    }

    private boolean anyMatches() {
        // ...
        boolean anyMatches = false;
        for (Criterion criterion: criteria)
            anyMatches |= criterion.matches(answerMatching(criterion));
        return anyMatches;
    }
}
