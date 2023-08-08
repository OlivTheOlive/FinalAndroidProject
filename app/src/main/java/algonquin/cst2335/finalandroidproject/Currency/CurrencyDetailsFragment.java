package algonquin.cst2335.finalandroidproject.Currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.databinding.CurrencyDetailsLayoutBinding;

public class CurrencyDetailsFragment extends Fragment {

    private CurrencySelected selected;

    public CurrencyDetailsFragment(CurrencySelected c) {
        selected = c;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CurrencyDetailsLayoutBinding binding = CurrencyDetailsLayoutBinding.inflate(inflater, container, false);

        binding.conversionResult.setText(selected.getConversionResult());
        binding.time.setText(selected.getTime());
        binding.id.setText(String.valueOf("Id = " + selected.id));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // binding = null;  Detach the binding
    }
}
