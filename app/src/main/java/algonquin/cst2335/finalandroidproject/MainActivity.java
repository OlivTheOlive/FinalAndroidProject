package algonquin.cst2335.finalandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    protected ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        binding.aviationButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(this, AviationTrackerActivity.class);
            startActivity(nextPage);
        });
        binding.currencyButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage);
        });
        binding.triviaButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(this, TriviaActivity.class);
            startActivity(nextPage);
        });
        binding.bearButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(this, BearActivity.class);
            startActivity(nextPage);
        });
    }
}