package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.CurrencyDataDetailsBinding;
import algonquin.cst2335.finalandroidproject.databinding.RecyclerviewCurrencyConverterBinding;


public class CurrencyConverterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected RequestQueue queue = null;
    private ActivityCurrencyConverterBinding binding;

    private ArrayList<CurrencySelected> conversionResult = new ArrayList<>();

    private CurrencyViewModel currencyViewModel;

    private TextView textView;

    CurrencyHistory currencyHistory = new CurrencyHistory();

    private CurrencyDatabase currencyDatabase;

    protected String countryFrom;
    protected String countryTo;
    protected String amountInput;

    protected CurrencyDAO myDAO;

    protected RecyclerView.Adapter<MyRowHolder> myAdapter;

    protected RecyclerviewCurrencyConverterBinding recyclerViewBinding;


    @Override
    @SuppressLint("NotifyDataSetChange")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        currencyDatabase = CurrencyDatabase.getInstance(this);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.currencyToolbar);

        CurrencyDatabase db = Room.databaseBuilder(getApplicationContext(), CurrencyDatabase.class, "MyCurrencyDatabase").build();
        myDAO = db.cDAO();


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

            myDAO = currencyDatabase.cDAO();

            RecyclerviewCurrencyConverterBinding recyclerViewBinding = RecyclerviewCurrencyConverterBinding.inflate(getLayoutInflater());
            RecyclerView recyclerView = recyclerViewBinding.recyclerView;


            recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                @NonNull
                @Override
                public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    CurrencyDataDetailsBinding binding = CurrencyDataDetailsBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }

                @Override
                public int getItemViewType(int position){
                    return 0;
                }

                @Override
                public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                    CurrencySelected currencySelected = conversionResult.get(position);
                    holder.amountInputText.setText(currencySelected.getConversionResult());
                    holder.timeText.setText(currencySelected.getTime());
                }

                @Override
                public int getItemCount() {
                    return conversionResult.size();
                }

            });

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

                @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
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
                                conversionResult.add(convert);
                                myAdapter.notifyDataSetChanged();
                                binding.amountInput.setText("");
                                recyclerView.scrollToPosition(conversionResult.size() - 1);

                                Executors.newSingleThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        convert.id = (int) myDAO.insertAmount(convert);

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

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        CurrencySelected selected = currencyViewModel.getSelectedAmount().getValue();

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

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView amountInputText;

        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                CurrencySelected selected = conversionResult.get(position);

                currencyViewModel.selectedAmount.postValue(selected);

            });

            amountInputText = itemView.findViewById(R.id.amountInput);
            timeText = itemView.findViewById(R.id.time);

        }
    }
}
