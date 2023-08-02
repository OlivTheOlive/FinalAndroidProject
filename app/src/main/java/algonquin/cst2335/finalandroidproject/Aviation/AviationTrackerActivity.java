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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import algonquin.cst2335.finalandroidproject.Aviation.DAO.FlightRequestDAO;
import algonquin.cst2335.finalandroidproject.Aviation.Data.AviationTrackerViewModel;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequest;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequestDB;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityGetFlightDataBinding;

public class AviationTrackerActivity extends AppCompatActivity {

    private static  final String SHARED_PREF = "Data";
    private static final String REQ_CODE = "Code";
    private  RecyclerView.Adapter myAdapter;
    private ActivityAviationTrackerBinding binding;
    private ActivityGetFlightDataBinding binding2;
    private FlightRequestDAO DAO;
    private ArrayList<FlightRequest> requests;
    private FlightRequest newReq;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue = null;

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
            String typed = binding.editTextText.getText().toString().toUpperCase();
            String url = "https://api.aviationstack.com/v1/flights?access_key=9694046b348f410681e40e36750f3730&dep_iata="+typed;
            if(checkCode(typed)){
                queue = Volley.newRequestQueue(this);
                Executor thread = Executors.newSingleThreadExecutor();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                JSONArray dataArray = response.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject position = dataArray.getJSONObject(i);
                                    JSONObject depart = position.getJSONObject("departure");
                                    String airportName= depart.getString("airport");
                                    JSONObject flight = position.getJSONObject("flight");
                                    String flightString = flight.getString("number");
                                    String status = position.getString("flight_status");
                                    String date = position.getString("flight_date");



                                    FlightRequest newReq = new FlightRequest("AP code: "+ typed,
                                                                                "Flight #: "+flightString,
                                                                                "Airport Dep.: "+ airportName,
                                                                                "Status: "+status);
                                    requests.add(newReq);
                                    myAdapter.notifyItemInserted(requests.size() - 1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Handle the error case if needed
                            error.printStackTrace();
                        });

                    queue.add(request);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(REQ_CODE, typed);
                    editor.apply();

                binding.editTextText.setText("");
            }
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));


        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ActivityGetFlightDataBinding binding = ActivityGetFlightDataBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.code.setText("");
                holder.nameID.setText("");
                holder.flightId.setText("");
                holder.statusID.setText("");


                FlightRequest obj = requests.get(position);
                holder.code.setText(obj.getCode());
                holder.flightId.setText((obj.getFlightID()));
                holder.nameID.setText(obj.getNameID());
                holder.statusID.setText(obj.getStatusID());

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
        TextView code, nameID, flightId, statusID;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                FlightRequest m = requests.get(position);
                if (m != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you want to delete the dataset: " + flightId.getText())
                            .setNegativeButton("Save", (dialog, cl) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(() -> DAO.insertCode(m));
                                requests.add(position, m);
                                myAdapter.notifyItemInserted(position);
                                Snackbar.make(flightId, "You saved dataset #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", click -> {
                                            requests.remove(position);
                                            runOnUiThread(() -> myAdapter.notifyItemRemoved(position));
                                        })
                                        .show();
                            })
                            .setPositiveButton("Delete", (dialog, cl) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(() -> DAO.deleteCode(m));
                                requests.remove(position);
                                myAdapter.notifyItemRemoved(position);
                                Snackbar.make(flightId, "You deleted dataset #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", click -> {
                                            requests.add(position, m);
                                            runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                        })
                                        .show();
                            })
                            .create().show();
                }
                return false;
            });

            code= itemView.findViewById(R.id.airCodeID);
            nameID= itemView.findViewById(R.id.nameID);
            flightId= itemView.findViewById(R.id.flighNum);
            statusID= itemView.findViewById(R.id.statusID);


        }


    }
}