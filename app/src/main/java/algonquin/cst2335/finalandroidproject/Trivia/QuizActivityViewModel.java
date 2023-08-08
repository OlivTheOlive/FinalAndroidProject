package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


public class QuizActivityViewModel extends ViewModel {


    private MutableLiveData<ArrayList<Score>> score = new MutableLiveData<>();

    public MutableLiveData<List<Score>> selectedScore = new MutableLiveData<java.util.List<Score>>(null);
}
