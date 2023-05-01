//package chapter9;
//
//import chapter13.Answer;
//import chapter13.Criteria;
//import chapter2.Criterion;
//
//import java.util.Map;
//
///**
// * 점수 관련 코드.
// */
//public class MatchSet {
//    private Map<String, Answer> answers;
//    private int score = 0;
//    // 추가
//    private Criteria criteria;
//
//    public MatchSet(Map<String, Answer> answers, Criteria criteria) {
//        this.answers = answers;
//        this.criteria = criteria;
//        calculateScore(criteria);
//    }
//
//    private void calculateScore(Criteria criteria) {
//        for (Criterion criterion: criteria)
//            if (criterion.matches(answerMatching(criterion)))
//                score += criterion.getWeight().getValue();
//    }
//
//    private Answer answerMatching(Criterion criterion) {
//        return answers.get(criterion.getAnswer().getQuestionText());
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    //추가
///*    public boolean matches(Criteria criteria) {
//        MatchSet matchSet = new MatchSet(answers, criteria);
//        score = matchSet.getScore();
//        return matchSet.matches();
//    }*/
//}