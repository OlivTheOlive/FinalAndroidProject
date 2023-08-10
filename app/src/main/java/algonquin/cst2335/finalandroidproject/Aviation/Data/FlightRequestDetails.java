package algonquin.cst2335.finalandroidproject.Aviation.Data;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.AviationDetailsBinding;

/**
 * A DialogFragment class for displaying details of a flight request.
 */
public class FlightRequestDetails extends DialogFragment {
    /**
     * The selected flight request object to display details for.
     */
    private FlightRequest selected;

    /**
     * Constructs a FlightRequestDetails instance with the given flight request.
     *
     * @param newValue The flight request object for which to display details.
     */
    public FlightRequestDetails(FlightRequest newValue) {
        selected = newValue;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AviationDetailsBinding binding = AviationDetailsBinding.inflate(LayoutInflater.from(getContext()));
        Dialog dialog = new Dialog(requireContext());

        binding.getRoot().setEnabled(false);
        binding.flightNumID.setText("");
        binding.delayNumID.setText("");
        binding.flightStatusID.setText("");
        binding.gateNumID.setText("");
        binding.terminalNumID.setText("");
        binding.arrivalID.setText("");

        if (selected != null) {
            binding.flightNumID.setText(selected.getFlightID());
            binding.delayNumID.setText(selected.getNameID());
            binding.flightStatusID.setText(selected.getStatusID());
            binding.gateNumID.setText(selected.date);
            binding.terminalNumID.setText(selected.departureAP);
            binding.arrivalID.setText(selected.arrivalAP);
        }

        dialog.setContentView(binding.getRoot());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().setCancelable(true);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
