package algonquin.cst2335.finalandroidproject.Currency;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import algonquin.cst2335.finalandroidproject.R;

public class CurrencyHistory extends AppCompatActivity {

    private CurrencyViewModel currencyViewModel;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_currency_converter);

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Retrieve conversion history data from the ViewModel
        ArrayList<CurrencySelected> conversionResultsList = currencyViewModel.currencyConvertedList.getValue();

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this, conversionResultsList);
        recyclerView.setAdapter(myAdapter);

        // Observe changes in the currencyConvertedList LiveData
        currencyViewModel.currencyConvertedList.observe(this, conversionList -> {
            myAdapter.updateList(conversionList);
            myAdapter.notifyDataSetChanged();
        });
    }
}
