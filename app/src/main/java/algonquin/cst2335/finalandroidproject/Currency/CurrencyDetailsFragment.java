package algonquin.cst2335.finalandroidproject.Currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.databinding.CurrencyDetailsLayoutBinding;

public class CurrencyDetailsFragment extends Fragment {

    CurrencySelected selected;

    public CurrencyDetailsFragment( CurrencySelected c ) {selected = c; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencyDetailsLayoutBinding binding = CurrencyDetailsLayoutBinding.inflate(inflater);

        binding.CAD.setText("CAD");
        //need to change the outline
       // binding.CADAmount.setText(  );
        binding.FF.setText("FF");
        //binding.FFAmount.setText( );
        binding.id.setText(selected.id);

        return binding.getRoot();

    }


}
