package algonquin.cst2335.finalandroidproject.Currency;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {

    private MutableLiveData<ArrayList<CurrencySelected>> conversionResultsList = new MutableLiveData<>();
    private MutableLiveData<CurrencySelected> selectedAmount = new MutableLiveData<>();

    public MutableLiveData<ArrayList<CurrencySelected>> getConversionResultsList() {
        return conversionResultsList;
    }

    public void setConversionResultsList(ArrayList<CurrencySelected> list) {
        conversionResultsList.setValue(list);
    }

    public MutableLiveData<CurrencySelected> getSelectedAmount() {
        return selectedAmount;
    }

    public void setSelectedAmount(CurrencySelected amount) {
        selectedAmount.setValue(amount);
    }
}
