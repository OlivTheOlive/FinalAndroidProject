package algonquin.cst2335.finalandroidproject.Currency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

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

    private CurrencyDAO currencyDAO;

    public CurrencyViewModel(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }
    public LiveData<List<CurrencySelected>> getAllAmount() {
        return currencyDAO.getAllAmount();
    }

    public void addConversionResult(CurrencySelected currencySelected) {
        List<CurrencySelected> currentList = conversionResultsList.getValue();

        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        currentList.add(currencySelected);
        conversionResultsList.setValue((ArrayList<CurrencySelected>) currentList);
    }
}
