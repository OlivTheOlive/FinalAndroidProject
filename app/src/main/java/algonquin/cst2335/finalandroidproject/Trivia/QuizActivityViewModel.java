package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class QuizActivityViewModel extends ViewModel {

    private MutableLiveData<QuestionObj> selectedQuestion = new MutableLiveData<>();


    public MutableLiveData<QuestionObj> getSelectedQuestion() {
        return selectedQuestion;
    }
    public void setSelectedQuestion(QuestionObj question) {
        selectedQuestion.setValue(question);
    }
}
