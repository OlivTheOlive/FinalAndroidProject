/*package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.RecyclerviewCurrencyConverterBinding;

public class CurrencyHistory extends AppCompatActivity {

    private CurrencyViewModel currencyViewModel;
    private MyAdapter myAdapter;

    private RecyclerviewCurrencyConverterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RecyclerviewCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.currencyToolbar);

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(myAdapter);


        // Observe changes in the currencyConvertedList LiveData
        currencyViewModel.conversionResultsList.observe(this, conversionList -> {
            myAdapter.updateList(conversionList);
            myAdapter.notifyDataSetChanged();
        });

        // Retrieve the selected item from ViewModel and perform deletion if needed
        CurrencySelected selected = currencyViewModel.selectAmount.getValue();
        if (selected != null) {
            int position = myAdapter.getPosition(selected);
            if (position >= 0) {
                myAdapter.removeAt(position);
            }
            currencyViewModel.selectAmount.setValue(null); // Clear the selected item
        }

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


        CurrencySelected selected = currencyViewModel.selectAmount.getValue();
        TextView amountInput = findViewById(R.id.amountInput);

        if (item.getItemId() == R.id.deleteCurrency) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyHistory.this);
            builder.setTitle("Currency History Delete")
                    .setMessage("Please confirm you want to delete this history")
                    .setNegativeButton("No", (dialog, clk) -> {})
                    .setPositiveButton("Yes", (dialog, clk) -> {
                        // Pass the selected item to CurrencyHistory activity
                        currencyViewModel.selectAmount.setValue(selected);
                        Intent nextPage = new Intent(CurrencyHistory.this, CurrencyHistory.class);
                        startActivity(nextPage);
                    })
                    .create().show();
        }else if (item.getItemId() == R.id.about){

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyHistory.this);
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
        }

        return true;

    }
}
*/

package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.RecyclerviewCurrencyConverterBinding;

public class CurrencyHistory extends AppCompatActivity {

    private CurrencyViewModel currencyViewModel;
    private MyAdapter myAdapter;
    private RecyclerviewCurrencyConverterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RecyclerviewCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.currencyToolbar);

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(myAdapter);

        // Observe changes in the currencyConvertedList LiveData
       currencyViewModel.getAllAmount().observe(this, conversionList -> {
           ArrayList<CurrencySelected> arrayList = new ArrayList<>(conversionList);
           myAdapter.updateList(arrayList);
           myAdapter.notifyDataSetChanged();
        });

        // Retrieve the selected item from ViewModel and perform deletion if needed
        CurrencySelected selected = currencyViewModel.getSelectedAmount().getValue();
        if (selected != null) {
            int position = myAdapter.getPosition(selected);
            if (position >= 0) {
                myAdapter.removeAt(position);
            }
            currencyViewModel.getSelectedAmount().setValue(null); // Clear the selected item
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String conversionResult = extras.getString("conversion_result");
            String time = extras.getString("time");

            // Create a new CurrencySelected object
            CurrencySelected convert = new CurrencySelected(conversionResult, time);

            // Add the new item to the RecyclerView through the adapter
            myAdapter.addItem(convert);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CurrencySelected selected = currencyViewModel.getSelectedAmount().getValue();

        if (item.getItemId() == R.id.deleteCurrency) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Currency History Delete")
                    .setMessage("Please confirm you want to delete this history")
                    .setNegativeButton("No", (dialog, clk) -> {})
                    .setPositiveButton("Yes", (dialog, clk) -> {
                        // Pass the selected item to CurrencyHistory activity
                        currencyViewModel.getSelectedAmount().setValue(selected);
                        Intent nextPage = new Intent(this, CurrencyHistory.class);
                        startActivity(nextPage);
                    })
                    .create().show();
        }else if (item.getItemId() == R.id.about){

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyHistory.this);
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
        }

        return true;
    }
}
