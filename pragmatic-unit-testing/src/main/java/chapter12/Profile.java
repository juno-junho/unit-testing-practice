package chapter12;

import java.util.HashMap;
import java.util.Map;

class Profile {
    private Map<String, Answer> answers = new HashMap<>();

    private Answer getMatchingProfileAnswer(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    private Answer answer;
    public boolean matches(Criterion criterion) {
        Answer answer = getMatchingProfileAnswer(criterion);
        return answer != null && answer.match(criterion.getAnswer());
        // null check를 answer로 변경
    }

    public void add(Answer answer) {
        this.answers.put(answer.getQuestionText(), answer);
    }
}
