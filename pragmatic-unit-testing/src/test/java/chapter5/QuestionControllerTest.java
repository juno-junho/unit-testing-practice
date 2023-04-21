package chapter5;

import chapter2.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionControllerTest {
/*
    @Test
    void question_Answers_Date_Added() {
        Instant now = new Date().toInstant();
        controller.setClock(Clock.fixed(now, ZoneId.of("America/Denver")));
        int id = controller.addBoolean("text");

        Question question = controller.find(id);
        assertThat(question.getCreateTimeStamp()).isEqualTo(now);
    }*/
}
