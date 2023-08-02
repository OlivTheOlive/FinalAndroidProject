package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.CadConverterBinding;
import algonquin.cst2335.finalandroidproject.databinding.FfConverterBinding;

public class CurrencyConverterActivity extends AppCompatActivity {



    //this is used for binding the elements in ActivityCurrencyConverter
    private ActivityCurrencyConverterBinding binding;

    //This is used for creating an array list of object enterAmount
    private ArrayList<CurrencySelected> amounts;

    //This is calling the CurrencyViewModel
    private CurrencyViewModel currencyModel;

    //This is calling the RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;


    //These is the variables in Currency Converter Activity
    private TextView textView;

    CurrencyDatabase currencyDatabase;

    //calling DAO
    CurrencyDAO currencyDAO;

    //Import CurrencySelected
    CurrencySelected currencySelected = new CurrencySelected();



    @Override
    @SuppressLint("NotifyDataSetChange")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currencyModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        amounts = currencyModel.amountCov.getValue();


        currencyDatabase = Room.databaseBuilder(getApplicationContext()
                        ,CurrencyDatabase.class
                        , "currencyDatabase")
                .addMigrations(CurrencyDatabase.MIGRATION_1_2)
                .build();
        currencyDAO = currencyDatabase.cDAO();


        /**
         * This is to create the database and make a connection with currencyDAO
         */
        if(amounts == null){
            currencyModel.amountCov.setValue( amounts = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
               amounts.addAll( currencyDAO.getAllAmount() );
               runOnUiThread( () -> binding.recyclerview.setAdapter( myAdapter ));
            });
        }

        if(amounts == null){
            currencyModel.amountCov.postValue( amounts = new ArrayList<>());
        }

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /**
         * This is the toolbar
         */
        setSupportActionBar(binding.currencyToolbar);

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
                holder.amountConvertText.setText("");
                holder.timeText.setText("");

                CurrencySelected currencySelected = amounts.get(position);
                holder.amountConvertText.setText(currencySelected.getAmount());
                holder.timeText.setText(currencySelected.getTimeOfCurrency());

            }

            @Override
            public int getItemCount() {
                return amounts.size();
            }

            public int getItemViewType(int position) {
                CurrencySelected selected = amounts.get(position);
                if(selected.getCADOrFF() % 2 == 0){
                    return 1;
                } else {
                    return 2;
                }
            }
        });

        //Keep this in mind I think I need to update this
        recyclerView = binding.recyclerview;

        /**
         * This is the button function for CAD button
         */
        binding.CADButton.setOnClickListener(clk -> {
            int type = 1;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeCov = time.format(new Date());
            CurrencySelected CAD = new CurrencySelected(binding.amountInput.getText().toString(), binding.amountInput.getText().toString(), timeCov, type);

            CurrencySelected FF = new CurrencySelected(binding.amountInput.getText().toString(), binding.amountInput.getText().toString(),timeCov,type);



          /*
          TextView messageText = null;
            Snackbar snackbar = Snackbar.make(messageText, "Snackbar is working", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Toast.makeText(getApplicationContext(),"UNDO action", Toast.LENGTH_LONG).show();
                }
            });
            */

            if(binding.amountInput == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please input a number")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                amounts.add(CAD);
                myAdapter.notifyDataSetChanged();
                binding.amountInput.setText("");
                recyclerView.scrollToPosition(amounts.size() - 1);
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(new Runnable(){

                    @Override
                    public void run() { FF.id = (int) currencyDAO.insertAmount(FF);}

                });
                Toast.makeText(getApplicationContext(), "Amount Converted", Toast.LENGTH_LONG).show();
            }

        });

        /**
         * This is the button function for FF button
         */
        binding.FFButton.setOnClickListener(v -> {
            int type = 2;
            SimpleDateFormat time = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String timeCov = time.format(new Date());
            CurrencySelected CAD = new CurrencySelected(binding.amountInput.getText().toString(), binding.amountInput.getText().toString(), timeCov, type);
            CurrencySelected FF = new CurrencySelected(binding.amountInput.getText().toString(), binding.amountInput.getText().toString(), timeCov, type);

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

            } else {

                amounts.add(FF);
                myAdapter.notifyDataSetChanged();
                binding.amountInput.setText("");
                recyclerView.scrollToPosition(amounts.size() - 1);
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(new Runnable() {

                    @Override
                    public void run() {
                        FF.id = (int) currencyDAO.insertAmount(FF);
                    }

                });
                Toast.makeText(getApplicationContext(), "Amount Converted", Toast.LENGTH_LONG).show();

            }
        });

            binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        currencyModel.selectAmount.observe(this,(newAmount) -> {

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }
    /**
     * This is an object that represents everything that goes on a row in a list
     */
    class MyRowHolder extends RecyclerView.ViewHolder{
        //These are the variable from currency models CAD and FF
        TextView amountConvertText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView){
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                CurrencySelected selected = amounts.get(position);
                currencyModel.selectAmount.postValue(selected);

            });

            amountConvertText = itemView.findViewById(R.id.amountConvert);
            timeText = itemView.findViewById(R.id.timeText);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        CurrencySelected selected = currencyModel.selectAmount.getValue();
        TextView amountInput = findViewById(R.id.amountInput);

        if(item.getItemId() == R.id.deleteCurrency){

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverterActivity.this);
            builder.setTitle("Currency History Delete")
                    .setMessage("Please confirm you want to delete this history")
                    .setNegativeButton("No", (dialog, clk) -> {})
                    .setPositiveButton("Yes", (dialog, clk) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        int position = amounts.indexOf(selected);
                        CurrencySelected m = amounts.get(position);
                        thread.execute(()->{
                            currencyDAO.deleteAmount(m);
                        });

                        amounts.remove(position);
                        myAdapter.notifyItemRemoved(position);
                        Snackbar.make(amountInput, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                .setAction("Undo", click -> {
                                    amounts.add(position, m);
                                    runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                })
                                .show();
                        onBackPressed();
                    }).create().show();

        } else if (item.getItemId() == R.id.about){

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverterActivity.this);
            builder.setTitle("How to use the application")
                    .setMessage("1. Input amount you want to convert" +
                            "2. Click on CAD if the amount is CAD or FF if the amount is in FF")
                    .setNegativeButton("",(dialog, clk) -> {})
                    .setPositiveButton("Ok", (dialog, clk) -> {
                    }).create().show();
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