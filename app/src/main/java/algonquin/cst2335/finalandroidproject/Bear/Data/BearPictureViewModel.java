package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class BearPictureViewModel extends ViewModel {

    public MutableLiveData<ArrayList<BearPicture>> pictures = new MutableLiveData<>();
    public MutableLiveData<BearPicture> selectedPicture = new MutableLiveData<>(null);
}

