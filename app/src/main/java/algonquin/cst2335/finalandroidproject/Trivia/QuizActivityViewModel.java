package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for managing and storing UI-related data for the Quiz Activity.
 * <p>
 * This ViewModel contains data related to scores in the quiz. It provides a layer of abstraction
 * between the UI components and the underlying data sources, ensuring that the UI remains consistent
 * and data changes are reflected across components.
 * </p>
 */
public class QuizActivityViewModel extends ViewModel {

    /** MutableLiveData to hold the list of scores. */
    private MutableLiveData<ArrayList<Score>> score = new MutableLiveData<>();

    /** MutableLiveData to hold a selected score. */
    public MutableLiveData<List<Score>> selectedScore = new MutableLiveData<java.util.List<Score>>(null);
}
