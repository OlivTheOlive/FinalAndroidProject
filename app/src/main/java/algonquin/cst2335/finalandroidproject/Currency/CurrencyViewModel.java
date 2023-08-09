package algonquin.cst2335.finalandroidproject.Currency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel class that provides data related to currency conversion history and selected amounts.
 */
public class CurrencyViewModel extends ViewModel {

    private CurrencyDAO currencyDAO;
    /**
     * LiveData representing the list of currency conversion results.
     */
    public MutableLiveData<ArrayList<CurrencySelected>> conversionResultsList = new MutableLiveData<>();
    /**
     * LiveData representing the selected currency conversion amount.
     */
    public MutableLiveData<CurrencySelected> selectedAmount = new MutableLiveData<>();


}
