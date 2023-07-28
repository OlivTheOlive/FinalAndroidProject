package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.CadConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.FfConverterBinding;

public class CurrencyConverterActivity extends AppCompatActivity {

    //Import CurrencySelected
    CurrencySelected currencySelected = new CurrencySelected();

    //this is used for binding the elements in ActivityCurrencyConverter
    private ActivityCurrencyConverterBinding binding;

    //This is used for creating an array list of object enterAmount
    private ArrayList<CurrencySelected> amounts;

    //This is calling the CurrencyViewModel
    private CurrencyViewModel currencyModel;

    //This is calling the RecyclerView
    private RecyclerView.Adapter myAdapter;
    private RecyclerView recyclerView;

    //These is the variables in Currency Converter Activity
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        //this is the ViewModel provider for Currency, calling CurrencyViewModel
        currencyModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        //recyclerView = binding.recyclerview;
        amounts = currencyModel.amountCov.getValue();

        /**
         * This is an object that represents everything that goes on a row in a list
         */
        class MyRowHolder extends RecyclerView.ViewHolder{
            //These are the variable from currency models CAD and FF
            TextView amountConvertText;
            TextView timeText;
            public MyRowHolder(@NonNull View itemView){
                    super(itemView);
                    amountConvertText = itemView.findViewById(R.id.amountConvert);
                    timeText = itemView.findViewById(R.id.time);
        }
        }

        /**
         * This is the recyclerview
         */
        binding.recyclerview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    CadConverterBinding binding = CadConverterBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    FfConverterBinding binding = FfConverterBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                CurrencySelected object = amounts.get(position);
                holder.amountConvertText.setText(object.getCADOrFF());
                holder.timeText.setText("");
            }

            @Override
            public int getItemCount() {
                return amounts.size();
            }

        });

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        /**
         * This is the button function for CAD button
         */
        binding.CADButton.setOnClickListener(v -> {
            int type = 1;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeCov = time.format(new Date());
            CurrencySelected CAD = new CurrencySelected(binding.amountInput.getText().toString(), timeCov, type);
            amounts.add(CAD);
            myAdapter.notifyDataSetChanged();
            binding.amountInput.setText("");

            if(binding.amountInput!= null) {
                Toast.makeText(getApplicationContext(), "Amount Converted", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("there is an alert")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

        /**
         * This is the button function for FF button
         */
        binding.FFButton.setOnClickListener(v -> {
            int type = 2;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeCov = time.format(new Date());
            CurrencySelected FF = new CurrencySelected(binding.amountInput.getText().toString(),timeCov,type);
            amounts.add(FF);
            myAdapter.notifyDataSetChanged();
            binding.amountInput.setText("");

           if(binding.amountInput!= null) {
               Toast.makeText(getApplicationContext(), "Amount Converted", Toast.LENGTH_LONG).show();
           } else {
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setMessage("there is an alert")
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                           public void onClick(DialogInterface dialog, int id){
                               dialog.cancel();
                           }
                       });
               AlertDialog alert = builder.create();
               alert.show();
           }

        });






       /* SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
       // String amount = prefs.getInt("editTextText1","");
        Integer amount = prefs.getInt("amount", "");


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



        //This is an alert*/

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