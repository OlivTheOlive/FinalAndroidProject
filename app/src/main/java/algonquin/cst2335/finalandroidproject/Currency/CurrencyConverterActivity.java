package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.CurrencyDataDetailsBinding;
import algonquin.cst2335.finalandroidproject.databinding.RecyclerviewCurrencyConverterBinding;


/**
 * This activity allows users to convert currency between different countries using real-time
 * exchange. It also provides a history of currency conversion results.
 */
public class CurrencyConverterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * the Volley request queue for making API requests.
     */
    protected RequestQueue queue = null;
    /**
     * The data binding instance for the activity layout.
     */
    private ActivityCurrencyConverterBinding binding;
    /**
     * List of currency conversion results.
     */
    ArrayList<CurrencySelected> conversionResultsList;
    /**
     * ViewModel for managing currency conversion data.
     */
    private CurrencyViewModel currencyViewModel;
    /**
     * RecyclerView for displaying currency conversion history.
     */
    protected RecyclerView recyclerView;
    /**
     * The selected source country for currency conversion
     */
    protected String countryFrom;
    /**
     * The select target country for currency conversion.
     */
    protected String countryTo;
    /**
     * The user-input amount for currency conversion.
     */
    protected String amountInput;
    /**
     * The DAO for interacting with the currency database.
     */
    protected CurrencyDAO myDAO;
    /**
     * The RecyclerView adapter for displaying currency conversion history items.
     */
    protected RecyclerView.Adapter<MyRowHolder> myAdapter;


    /**
     * @author Hanna Felix
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    @SuppressLint("NotifyDataSetChange")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        CurrencyDatabase db = Room.databaseBuilder(getApplicationContext(), CurrencyDatabase.class, "MyCurrencyDatabase").build();
        myDAO = db.cDAO();

        conversionResultsList = currencyViewModel.conversionResultsList.getValue();

        if(conversionResultsList == null){
            currencyViewModel.conversionResultsList.setValue(conversionResultsList = new ArrayList<CurrencySelected>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                conversionResultsList.addAll(myDAO.getAllAmount());

                runOnUiThread(() -> {
                   binding.recyclerView.setAdapter( myAdapter );
                });

            });

        }

        if(conversionResultsList == null){
            currencyViewModel.conversionResultsList.postValue(conversionResultsList = new ArrayList<>());
        }

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /**
         * Adapter for binding currency conversion history data to the RecyclerView.
         */
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                /**
                 * Called when a new ViewHolder is needed to represent a currency conversion history item.
                 *
                 * @param parent   The parent ViewGroup into which the new View will be added.
                 * @param viewType The type of the new View.
                 * @return A new MyRowHolder instance to represent a currency conversion history item.
                 */
                @NonNull
                @Override
                public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    CurrencyDataDetailsBinding binding = CurrencyDataDetailsBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }

                /**
                 * Binds data to the given ViewHolder to represent a currency conversion history item.
                 *
                 * @param holder   The ViewHolder to bind data to.
                 * @param position The position of the item within the data source.
                 */
                @Override
                public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                    holder.amountInputText.setText("");
                    holder.timeText.setText("");

                    CurrencySelected currencySelected = conversionResultsList.get(position);
                    holder.amountInputText.setText(currencySelected.getConversionResult());
                    holder.timeText.setText(currencySelected.getTime());
                }

            /**
             * Returns the total number of currency conversion history items in the data source.
             *
             * @return The total number of items in the currency conversion history.
             */
            @Override
                public int getItemCount() {
                    return conversionResultsList.size();
                }

                /**
                 * Returns the view type of the item at the specified position for the purpose of view recycling.
                 *
                 * @param position The position of the item within the data source.
                 * @return The view type of the item at the specified position.
                 */
                @Override
                public int getItemViewType(int position){
                    return 0;
                }

            });

        /**
         * Set the app's toolbar as the action bar for the activity.
         */
        setSupportActionBar(binding.currencyToolbar);

        /**
         * Check if the input amount is null. If so, display an AlertDialog informing the user to input a number.
         */
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
            /**
             * Configure the 'from' and 'to' spinners for selecting source and target countries for currency conversion.
             */
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

                /**
                 * Handle the currency conversion when the 'Convert' button is clicked.
                 */
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
                                conversionResultsList.add(convert);
                                myAdapter.notifyItemInserted(conversionResultsList.size() - 1);
                                binding.amountInput.setText("");
                                //recyclerViewBinding.recyclerView.scrollToPosition(conversionResultsList.size() - 1);
                                binding.recyclerView.scrollToPosition(conversionResultsList.size() - 1);

                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(new Runnable() {
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

            /**
             * Set the layout manager for the RecyclerView to display currency conversion history items.
             */
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

            /**
             * Observe changes in the 'selectedAmount' LiveData and replace the current fragment with
             * a new instance of CurrencyDetailsFragment to display details of the selected currency conversion.
             */
            currencyViewModel.selectedAmount.observe(this,(newCurrencyValue)-> {
                CurrencyDetailsFragment currencyFragment = new CurrencyDetailsFragment( newCurrencyValue );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, currencyFragment)
                        .addToBackStack("")
                        .commit();
            });

            }

    }

    /**
     * ViewHolder class to hold and manage the views for each currency conversion history item.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        CurrencyDataDetailsBinding currencyDataDetailsBinding;
        /**
         * TextView to display the converted currency amount.
         */
        TextView amountInputText;
        /**
         * TextView to display the timestamp of the currency conversion.
         */
        TextView timeText;

        /**
         * Constructs a new MyRowHolder instance and binds the UI elements.
         *
         * @param itemView The View representing the currency conversion history item.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            currencyDataDetailsBinding = CurrencyDataDetailsBinding.bind(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                CurrencySelected selected = conversionResultsList.get(position);
                currencyViewModel.selectedAmount.postValue(selected);

            });

            amountInputText = itemView.findViewById(R.id.conversionResultTextView);
            timeText = itemView.findViewById(R.id.timeTextView);

        }
    }


    /**
     * Callback method to be invoked when an item in the 'from' or 'to' spinner is selected.
     *
     * @param parent   The AdapterView where the selection happened.
     * @param view     The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that is selected (not used in this implementation).
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    /**
     * Callback method to be invoked when no item is selected in the 'from' or 'to' spinner.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initialize the options menu for the activity.
     *
     * @param menu The options menu in which you place your items.
     * @return True for the menu to be displayed, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.currency_menu, menu);

        return true;
    }

    /**
     * Called when an item in the options menu is selected.
     *
     * @param item The selected menu item.
     * @return True to consume the event here, false to allow normal menu processing to proceed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        if (item.getItemId() == R.id.deleteCurrency) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverterActivity.this);
            builder.setTitle("Currency History Delete")
                    .setMessage("Please confirm you want to delete this history")
                    .setNegativeButton("No", (dialog, clk) -> {})
                    .setPositiveButton("Yes", (dialog, clk) -> {

                        int position = item.getOrder();
                        CurrencySelected currencyRemove = conversionResultsList.get(position);
                        conversionResultsList.remove(position);
                        myAdapter.notifyItemRemoved(position);

                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            myDAO.deleteAmount(currencyRemove);
                        });

                        Snackbar.make( binding.convert, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", (click)-> {

                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                    thread2.execute(() -> {
                                        myDAO.insertAmount(currencyRemove);
                                        conversionResultsList.add(position,currencyRemove);
                                        runOnUiThread(()->myAdapter.notifyDataSetChanged());
                                    });
                                })
                                .show();
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

            binding.recyclerView.setVisibility(View.VISIBLE);

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


}
