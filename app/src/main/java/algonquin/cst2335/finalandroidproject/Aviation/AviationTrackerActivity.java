package algonquin.cst2335.finalandroidproject.Aviation;

import static java.lang.Character.isDigit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.DAO.FlightRequestDAO;
import algonquin.cst2335.finalandroidproject.Aviation.Data.AviationTrackerViewModel;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequest;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequestDB;
import algonquin.cst2335.finalandroidproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityGetFlightDataBinding;

public class AviationTrackerActivity extends AppCompatActivity {

    private static  final String SHARED_PREF = "Data";
    private static final String REQ_CODE = "Code";
    private  RecyclerView.Adapter myAdapter;
    private ActivityAviationTrackerBinding binding;
    private FlightRequestDAO DAO;
    private ArrayList<FlightRequest> requests;
    private FlightRequest newReq;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AviationTrackerViewModel ATViewModel = new ViewModelProvider(this).get(AviationTrackerViewModel.class);
        requests = ATViewModel.requests.getValue();
        FlightRequestDB db = Room.databaseBuilder(getApplicationContext(), FlightRequestDB.class, "FlightReqDB").build();
        DAO = db.frDAO();

        if (requests == null) {
            ATViewModel.requests.setValue(requests = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                requests.addAll(DAO.getAllMessage());
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });
        }
        //Sharedpref
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String savedCode = sharedPreferences.getString(REQ_CODE,"");
        binding.editTextText.setText(savedCode);

        binding.button.setOnClickListener(click ->{
            String typed = binding.editTextText.getText().toString();
            newReq = new FlightRequest(typed);
            if(checkCode(typed)){
                requests.add(newReq);
                myAdapter.notifyItemInserted(requests.size()-1);

                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> newReq.id = (int) DAO.insertCode(newReq));

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(REQ_CODE,typed);
                editor.apply();
                binding.editTextText.setText("");
            }
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ActivityGetFlightDataBinding binding = ActivityGetFlightDataBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.codeText.setText("");

                FlightRequest obj = requests.get(position);
                holder.codeText.setText(obj.getCode());
            }

            @Override
            public int getItemCount() {
                return requests.size();
            }
        });
    }


    //if the amount of capital letters do not match the length, show error in typed code.
    boolean checkCode(@NonNull String cd) {
        int duration = Toast.LENGTH_SHORT;
        int counter = 0;
        char c;
        if (cd.length() != 3) {
            Toast.makeText(this, "Code is not valid, 3 letters please", duration).show();
            return false;
        } else {
            for (int i = 0; i < cd.length(); i++) {
                c = cd.charAt(i);
                if (isDigit(c)) {
                    counter++;
                }
            }
        }
        if(cd.length() - counter == 3){
            Toast.makeText(this, "Code checks out", duration).show();
            return true;
        }else {
            Toast.makeText(this, "Code is not valid, 3 letters please", duration).show();
            return false;
        }
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView codeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                builder.setTitle("Question:")
                        .setMessage("Do you want to delete the dataset: " + codeText.getText())
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            FlightRequest m = requests.get(position);
                            thread.execute(() -> DAO.deleteCode(m));
                            requests.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(codeText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", click -> {
                                        requests.add(position, m);
                                        runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                    })
                                    .show();
                        })
                        .create().show();
            });
        }


    }
}