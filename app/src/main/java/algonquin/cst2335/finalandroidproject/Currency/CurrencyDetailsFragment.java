package algonquin.cst2335.finalandroidproject.Currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.databinding.CurrencyDetailsLayoutBinding;

/**
 * A fragment to display details of a selected currency conversion.
 */
public class CurrencyDetailsFragment extends Fragment {

    private CurrencySelected selected;

    /**
     * Constructs a new instance of CurrencyDetailsFragment with a selected currency conversion.
     *
     * @param c The CurrencySelected object representing the selected currency conversion.
     */
    public CurrencyDetailsFragment(CurrencySelected c) {
        selected = c;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CurrencyDetailsLayoutBinding binding = CurrencyDetailsLayoutBinding.inflate(inflater, container, false);

        binding.conversionResult.setText(selected.getConversionResult());
        binding.time.setText(selected.getTime());
        binding.id.setText(String.valueOf("Id = " + selected.id));

        return binding.getRoot();
    }

    /**
     * Called when the view previously created by onCreateView() has been detached.
     * This is where you should release resources associated with the UI.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // binding = null;  Detach the binding
    }
}
