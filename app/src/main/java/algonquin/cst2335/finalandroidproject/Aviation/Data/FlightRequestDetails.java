package algonquin.cst2335.finalandroidproject.Aviation.Data;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.databinding.AviationDetailsBinding;

public class FlightRequestDetails extends Fragment {

    private FlightRequest selected;

    public FlightRequestDetails(FlightRequest newValue) {
        selected=newValue;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AviationDetailsBinding binding = AviationDetailsBinding.inflate(inflater, container, false);
        binding.detFlightID.setText("");
        binding.airlineName.setText("");
        binding.detStatus.setText("");
        binding.detDate.setText("");
        binding.departureAirport.setText("");
        binding.ArrivalAirport.setText("");
        if (selected != null) {

            binding.detFlightID.setText(selected.getFlightID());
            binding.airlineName.setText(selected.getNameID());
            binding.detStatus.setText(selected.getStatusID());
            binding.detDate.setText(selected.date);
            binding.departureAirport.setText(selected.departureAP);
            binding.ArrivalAirport.setText(selected.arrivalAP);
        }

        return binding.getRoot();
    }
}
