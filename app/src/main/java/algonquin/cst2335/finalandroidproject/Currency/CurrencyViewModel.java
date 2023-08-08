package algonquin.cst2335.finalandroidproject.Currency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CurrencyViewModel extends ViewModel {

    private CurrencyDAO currencyDAO;

    public MutableLiveData<ArrayList<CurrencySelected>> conversionResultsList = new MutableLiveData<>();
    public MutableLiveData<CurrencySelected> selectedAmount = new MutableLiveData<>();


}
