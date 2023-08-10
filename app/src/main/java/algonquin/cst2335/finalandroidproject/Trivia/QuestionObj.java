package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;


/**
 *  Represents a question object in a trivia game.
 *  <p>
 *  This class contains details about a trivia question, including
 *  the main question, the correct answer, and a list of incorrect answers.
 *  * </p>
 */
public class QuestionObj implements Serializable  {

    /** Unique ID for the question. */
    public int id;

    /** The trivia question as a string. */
    private String questionString;

    /** The correct answer for the trivia question. */
    private String correctAnswer;

    /** List of incorrect answers for the trivia question. */
    private List<String> incorrectAnswers;


    /** Default constructor. */
public QuestionObj() {

}
    /**
     * Parameterized constructor to create a new trivia question.
     *
     * @param questionString    The trivia question as a string.
     * @param correctAnswer     The correct answer for the trivia question.
     * @param incorrectAnswers  List of incorrect answers for the trivia question.
     */
    public QuestionObj(String questionString, String correctAnswer, List<String> incorrectAnswers) {
        this.questionString = questionString;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }


    /**
     * Gets the trivia question string.
     * @return The trivia question.
     */
    public String getQuestionString() {
        return questionString;
    }

    /**
     * Sets the trivia question string.
     * @param questionString The trivia question.
     */
    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    /**
     * Gets the correct answer for the trivia question.
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer for the trivia question.
     * @param correctAnswer The correct answer.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the list of incorrect answers for the trivia question.
     * @return List of incorrect answers.
     */
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /**
     * Sets the list of incorrect answers for the trivia question.
     * @param incorrectAnswers List of incorrect answers.
     */
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }


}
