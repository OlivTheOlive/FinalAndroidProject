package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;

import android.os.AsyncTask;


public class CurrencyConverterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    protected RequestQueue queue = null;
    //this is used for binding the elements in ActivityCurrencyConverter
    private ActivityCurrencyConverterBinding binding;

    //This is used for creating an array list of object enterAmount
    private ArrayList<CurrencySelected> conversionResult = new ArrayList<>();


    //This is calling the CurrencyViewModel
    private CurrencyViewModel currencyViewModel;

    private MyAdapter myAdapter;

    //This is calling the RecyclerView
    private RecyclerView recyclerView;

    //These is the variables in Currency Converter Activity
    private TextView textView;

    CurrencyHistory currencyHistory = new CurrencyHistory();

    private CurrencyDatabase currencyDatabase;

    protected String countryFrom;
    protected String countryTo;
    protected String amountInput;


    //calling DAO
    CurrencyDAO currencyDAO;

    //Import CurrencySelected
    CurrencySelected currencySelected = new CurrencySelected();



    @Override
    @SuppressLint("NotifyDataSetChange")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        currencyDatabase = CurrencyDatabase.getInstance(this);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.currencyToolbar);

       // CurrencyViewModel currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);


        if (binding.amountInput == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please input a number")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (binding.amountInput != null) {

            Spinner from = binding.currencyFrom;
            Spinner to = binding.currencyTo;
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.country1, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            from.setAdapter(adapter1);

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.country2, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            to.setAdapter(adapter2);
            from.setOnItemSelectedListener(this);
            to.setOnItemSelectedListener(this);

            binding.convert.setOnClickListener(clk -> {

                countryFrom = binding.currencyFrom.getSelectedItem().toString();
                countryTo = binding.currencyTo.getSelectedItem().toString();
                amountInput = binding.amountInput.getText().toString();

                String stringURL = "https://api.getgeoapi.com/v2/currency/convert?format=json&from="
                        + URLEncoder.encode(countryFrom)
                        + "&to="
                        + URLEncoder.encode(countryTo)
                        + "&amount="
                        + URLEncoder.encode(amountInput)
                        + "&api_key=dc153509ab280aba2316ab0c64b6e9b04620eebe&format=json";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                        (response -> {
                            try {


                                String baseCurrencyCode = response.getString("base_currency_code");
                                double amount = response.getDouble("amount");

                                JSONObject ratesObject = response.getJSONObject("rates");
                                JSONObject userCurrencyObject = ratesObject.getJSONObject(countryTo);

                                String currencyName = userCurrencyObject.getString("currency_name");
                                double rate = userCurrencyObject.getDouble("rate");
                                double convertedAmount = userCurrencyObject.getDouble("rate_for_amount");

                                runOnUiThread(() -> {



                                    String resultText = baseCurrencyCode + "=" + amount + " " + countryTo + "=" + convertedAmount;
                                    binding.conversionResult.setText(resultText);

                                    Toast.makeText(getApplicationContext(), "Amount Converted", Toast.LENGTH_LONG).show();
                                });

                                SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
                                String timeCov = time.format(new Date());
                                CurrencySelected convert = new CurrencySelected(binding.conversionResult.getText().toString(), timeCov);

                                Executors.newSingleThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        currencyDatabase.cDAO().insertAmount(convert);
                                        convert.getConversionResult();
                                        currencyViewModel.addConversionResult(convert);

                                       /* Intent nextPage = new Intent(CurrencyConverterActivity.this, CurrencyHistory.class);
                                        nextPage.putExtra("conversion_result", convert.getConversionResult());
                                        nextPage.putExtra("time", convert.getTime());
                                        startActivity(nextPage);*/
                                    }
                                });

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }),
                        (error) -> {
                            int i = 0;
                        });
                queue.add(request);

            });




        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.currency_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        setSupportActionBar(binding.currencyToolbar);
        CurrencyViewModel currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        CurrencySelected selected = currencyViewModel.getSelectedAmount().getValue();
//

        if (item.getItemId() == R.id.deleteCurrency) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverterActivity.this);
            builder.setTitle("Currency History Delete")
                    .setMessage("Please confirm you want to delete this history")
                    .setNegativeButton("No", (dialog, clk) -> {})
                    .setPositiveButton("Yes", (dialog, clk) -> {
                        // Pass the selected item to CurrencyHistory activity
                        currencyViewModel.setSelectedAmount(selected);
                        Intent nextPage = new Intent(CurrencyConverterActivity.this, CurrencyHistory.class);
                        startActivity(nextPage);
                    })
                    .create().show();
        }else if (item.getItemId() == R.id.about){

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverterActivity.this);
            builder.setTitle("How to use the application")
                    .setMessage("Please input amount you wish to convert, then press CONVERT")
                    .setNegativeButton("",(dialog, clk) -> {})
                    .setPositiveButton("Ok", (dialog, clk) -> {
                    }).create().show();
        } else if (item.getItemId() == R.id.history){
            Intent nextPage = new Intent(this, CurrencyHistory.class);
            startActivity(nextPage);

        } else if (item.getItemId() == R.id.plane){
            Intent nextPage = new Intent(this, AviationTrackerActivity.class);
            startActivity(nextPage);

        } else if (item.getItemId() == R.id.bear){
            Intent nextPage = new Intent(this, BearActivity.class);
            startActivity(nextPage);

        } else if (item.getItemId() == R.id.trivia){
            Intent nextPage = new Intent(this, TriviaActivity.class);
            startActivity(nextPage);

        } else if (item.getItemId() == R.id.convert){
            Intent nextPage = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage);
        }

        return true;

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
