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

public class FlightRequestDetails extends DialogFragment {

    private FlightRequest selected;

    public FlightRequestDetails(FlightRequest newValue) {
        selected = newValue;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AviationDetailsBinding binding = AviationDetailsBinding.inflate(LayoutInflater.from(getContext()));
        Dialog dialog = new Dialog(requireContext()); // Use your custom dialog style here

        // Set the binding's root view to be non-focusable and non-clickable
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
            getDialog().setCancelable(true); // Make the dialog non-cancelable
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Make the dialog background transparent
        }
    }
}
