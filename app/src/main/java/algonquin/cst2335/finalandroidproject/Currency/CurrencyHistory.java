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

        // Set up the RecyclerView
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
}
