package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;


public class QuestionObj implements Serializable  {

    public int id;


    private String questionString;

    private String correctAnswer;

    private List<String> incorrectAnswers;


public QuestionObj() {

}
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
