package algonquin.cst2335.finalandroidproject.Currency;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {

    public MutableLiveData<ArrayList<CurrencySelected>> amounts = new MutableLiveData<>();

    public MutableLiveData<CurrencySelected> selectAmount = new MutableLiveData<>;
}
