package algonquin.cst2335.finalandroidproject.Currency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;

public class CurrencyConverterActivity extends AppCompatActivity {

    //Import CurrencySelected
    CurrencySelected currencySelected = new CurrencySelected();

    //this is used for binding the elements in ActivityCurrencyConverter
    private ActivityCurrencyConverterBinding binding;

    //This is used for creating an array list of object enterAmount
    private ArrayList<CurrencySelected> amount;

    //This is calling the CurrencyViewModel
    private CurrencyViewModel currencyModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //this is the ViewModel provider for Currency, calling CurrencyViewModel
        currencyModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        amount = currencySelected.amount.intValue();

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       // String amount = prefs.getInt("editTextText1","");
        Integer amount = prefs.getInt("amount", Integer.parseInt(""));


        binding.amount.setText(amount);


       /* TextView messageText = null;

        Snackbar snackbar = Snackbar.make(messageText, "Snackbar is working", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),"UNDO action", Toast.LENGTH_LONG).show();
            }
        });*/

        //This is the binding for CAD button
        binding.CADButton.setOnClickListener(v -> {
            int type = 1;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeOfCurrency = time.format(new Date());
            CurrencySelected CAD = new CurrencySelected(binding.amount.getText().toString(),timeOfCurrency,type);
            amount.add(CAD);
            myAdapter.notifyDataSetChanged();
            binding.amount.setText("");

        });

        //This is the binding for FF button
        binding.FFButton.setOnClickListener(v -> {
            int type = 2;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeOfCurrency = time.format(new Date());
            CurrencySelected FF = new CurrencySelected(binding.amount.getText().toString(),timeOfCurrency,type);

        });


        //This is an alert

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