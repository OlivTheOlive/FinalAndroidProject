package algonquin.cst2335.finalandroidproject.Aviation.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for the Aviation Tracker application.
 */
public class AviationTrackerViewModel extends ViewModel {

    /**
     * MutableLiveData holding an ArrayList of flight request objects.
     */
    public MutableLiveData<ArrayList<FlightRequest>> requests = new MutableLiveData<>();

    /**
     * MutableLiveData holding a selected flight request object.
     */
    public MutableLiveData<FlightRequest> selectedRequest = new MutableLiveData<>();
}

