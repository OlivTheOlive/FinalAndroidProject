package algonquin.cst2335.finalandroidproject.Aviation.Data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.databinding.AviationDetailsBinding;

public class FlightRequestDetails extends Fragment {

    FlightRequest selected;


    public FlightRequestDetails(Object fragReq) {
        fragReq=selected;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        AviationDetailsBinding binding = AviationDetailsBinding.inflate(inflater);


        return binding.getRoot();
    }
}
