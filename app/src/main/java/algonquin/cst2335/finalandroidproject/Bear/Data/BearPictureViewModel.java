package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for managing BearPicture data and selected picture in the application.
 */
public class BearPictureViewModel extends ViewModel {

    /**
     * MutableLiveData that holds a list of BearPicture Objects
     */
    public MutableLiveData<ArrayList<BearPicture>> pictures = new MutableLiveData<>();
    /**
     * MutableLiveData holding the selected BearPicture Object
     */
    public MutableLiveData<BearPicture> selectedPicture = new MutableLiveData<>(null);
}

