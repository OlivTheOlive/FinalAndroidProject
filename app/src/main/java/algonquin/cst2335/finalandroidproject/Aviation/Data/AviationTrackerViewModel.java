package algonquin.cst2335.finalandroidproject.Aviation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AviationTrackerViewModel extends ViewModel {
    public MutableLiveData<ArrayList<FlightRequest>> requests = new MutableLiveData<>();
    public MutableLiveData<FlightRequest> selectedRequest = new MutableLiveData< >();

}
