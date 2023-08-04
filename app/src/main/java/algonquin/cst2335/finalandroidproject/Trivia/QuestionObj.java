package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.List;

@Entity
public class QuestionObj implements Serializable {
    private String questionString;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public QuestionObj(String questionString, String correctAnswer, List<String> incorrectAnswers) {
        this.questionString = questionString;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}
