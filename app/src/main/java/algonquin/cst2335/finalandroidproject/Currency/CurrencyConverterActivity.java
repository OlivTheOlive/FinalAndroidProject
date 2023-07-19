package algonquin.cst2335.finalandroidproject.Currency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;

public class CurrencyConverterActivity extends AppCompatActivity {

    private ActivityCurrencyConverterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toast for converter button
        binding.button.setOnClickListener(v -> {
            String inputType = binding.editTextText.getText().toString();
            String inputType2 = binding.editTextText2.getText().toString();
            String inputType3 = binding.editTextText3.getText().toString();


                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType,+ Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType2,+ Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType3,+ Toast.LENGTH_LONG).show();

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}