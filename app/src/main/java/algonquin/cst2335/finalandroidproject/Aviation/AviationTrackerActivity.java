package algonquin.cst2335.finalandroidproject.Aviation;

import static java.lang.Character.isDigit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algonquin.cst2335.finalandroidproject.Aviation.DAO.FlightRequestDAO;
import algonquin.cst2335.finalandroidproject.Aviation.Data.AviationTrackerViewModel;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequest;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequestDB;
import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequestDetails;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityGetFlightDataBinding;

public class AviationTrackerActivity extends AppCompatActivity {

    private static  final String SHARED_PREF = "Data";
    private static final String REQ_CODE = "Code";
    private  RecyclerView.Adapter myAdapter;
    private ActivityAviationTrackerBinding binding;
    private FlightRequestDAO DAO;
    private ArrayList<FlightRequest> requests;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue = null;
    private AviationTrackerViewModel ATViewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu_aviation, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bear1) {
            Intent nextPage = new Intent(this, BearActivity.class);
            startActivity(nextPage);

        } else if (item.getItemId() == R.id.currency_converter_1) {
            Intent nextPage2 = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage2);

        } else if (item.getItemId() == R.id.trivia1) {
            Intent nextPage3 = new Intent(this, TriviaActivity.class);
            startActivity(nextPage3);

        } else if (item.getItemId() == R.id.about) {
            String about = String.format(getResources().getString(R.string.AviationAbout));
            String aboutMess = String.format(getResources().getString(R.string.AviationAboutMessage));
            String message = String.format(getResources().getString(R.string.Okkii));

            AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
            builder.setTitle(about)
                    .setMessage(aboutMess)
                    .setPositiveButton(message, (dialog, cl) -> {
                    })
                    .create().show();

        } else if (item.getItemId() == R.id.howID) {
            String howTo = String.format(getResources().getString(R.string.AviationHowTo));
            String howToMess = String.format(getResources().getString(R.string.AviationHowToMessage));
            String message = String.format(getResources().getString(R.string.Okkii));
            AlertDialog.Builder builder2 = new AlertDialog.Builder(AviationTrackerActivity.this);
            builder2.setTitle(howTo)
                    .setMessage("Just type an airport code to get your data")
                    .setPositiveButton(message, (dialog, cl) -> {
                    })
                    .create().show();
        }else if(item.getItemId() == R.id.saved){
            finish();
            startActivity(getIntent());

        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());



        setContentView(binding.getRoot());



        ATViewModel = new ViewModelProvider(this).get(AviationTrackerViewModel.class);
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
        setSupportActionBar(binding.toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String savedCode = sharedPreferences.getString(REQ_CODE,"");
        binding.editTextText.setText(savedCode);




//        ATViewModel.selectedRequest.observe(this, (newValue) -> {
//
//            FragmentManager fMgr = getSupportFragmentManager();
//            FragmentTransaction tx = fMgr.beginTransaction();
//            FlightRequestDetails mdf = new FlightRequestDetails(newValue);
//            tx.replace(R.id.fragLocation, mdf);
//            tx.addToBackStack("");
//            tx.commit();
//        });
        ATViewModel.selectedRequest.observe(this, (newValue) -> {
            FlightRequestDetails dialogFragment = new FlightRequestDetails(newValue);
            dialogFragment.show(getSupportFragmentManager(), "FlightRequestDetailsDialog");
        });



        binding.button.setOnClickListener(click ->{
            // Step 1: Get the text from the EditText and convert it to uppercase
            String typed = binding.editTextText.getText().toString().toUpperCase();
            // Step 2: Prepare the URL for the API call
            String url = "https://api.aviationstack.com/v1/flights?access_key=9694046b348f410681e40e36750f3730&dep_iata="+typed;
            // Step 3: Check if the typed code is valid using the checkCode() method
            if(checkCode(typed)){
                // Step 4: Create a new Volley RequestQueue and Executor
                queue = Volley.newRequestQueue(this);
                Executor thread = Executors.newSingleThreadExecutor();

                // Step 5: Clear the RecyclerView dataset by removing all not saved items
                ArrayList<FlightRequest> savedItems = new ArrayList<>();
                for (FlightRequest request : requests) {
                    if (request.getSaveID().equals("Saved")) {
                        savedItems.add(request);
                    }
                }
                requests.clear();
                requests.addAll(savedItems);
                myAdapter.notifyDataSetChanged();

                // Step 6: Create a JsonObjectRequest to fetch data from the API
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {
                                // Step 7: Parse the JSON response and extract flight data
                                JSONArray dataArray = response.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject position = dataArray.getJSONObject(i);
                                    JSONObject arrival = position.getJSONObject("arrival");
                                    String delay= arrival.getString("delay");
                                    JSONObject flight = position.getJSONObject("flight");
                                    String flightString = flight.getString("number");
                                    String status = position.getString("flight_status");
                                    String gate = arrival.getString("gate");
                                    String terminal = arrival.getString("terminal");
                                    String arrivalAP = arrival.getString("airport");
                                    JSONObject airline = position.getJSONObject("airline");
                                    String airlineN = airline.getString("name");

                                    String apCode=typed;
                                    String flightNum= flightString;
                                    String delayTime = delay;
                                    String statusString= status;
                                    String saveState= "Not Saved";
                                    String gateNum= gate;
                                    String terminalNum= terminal;
                                    String arrivalAirport= arrivalAP;
                                    String airlineName= airlineN;

                                    // Step 8: Create a new FlightRequest object with the extracted data
                                    FlightRequest newReq = new FlightRequest(apCode,flightNum, delayTime, statusString, saveState,gateNum,terminalNum,arrivalAirport,airlineName);


                                    // Step 9: Check if the newReq is a duplicate using the isDuplicateFlight() method
                                    if (!isDuplicateFlight(newReq)) {
                                        // Step 10: Only add the new item if it's not a duplicate
                                        requests.add(newReq);
                                        myAdapter.notifyItemInserted(requests.size() - 1);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Step 11: Handle API call error, show a toast message
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(this, "Api crashed again lol", duration).show();
                            error.printStackTrace();

                        });
                    // Step 12: Add the JsonObjectRequest to the Volley RequestQueue
                    queue.add(request);

                    // Step 13: Save the typed code to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(REQ_CODE, typed);
                    editor.apply();

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
                holder.flightId.setText("");
                holder.saveID.setText("Not Saved");



                FlightRequest obj = requests.get(position);
                holder.code.setText(obj.getCode());
                holder.flightId.setText(obj.getFlightID());
                holder.saveID.setText(obj.getSaveID());


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
    private boolean isDuplicateFlight(FlightRequest newReq) {
        for (FlightRequest request : requests) {
            if (request.getCode().equals(newReq.getCode()) && request.getFlightID().equals(newReq.getFlightID())) {
                return true; // Found a duplicate
            }
        }
        return false; // No duplicates found
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView code, flightId, saveID, airlineName, ArrivalAirport,departureAirport, detDate, detFlightID, detStatus;



        private boolean isFlightIdUnique(String flightId) {
            Executor executor = Executors.newSingleThreadExecutor();
            Future<Boolean> future = ((ExecutorService) executor).submit(() -> DAO.isFlightIdExists(flightId));
            try {
                return !future.get(); // Return true if the flightId is unique (doesn't exist in the database)
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(click->{
                int position = getAbsoluteAdapterPosition();
                FlightRequest m = requests.get(position);
                ATViewModel.selectedRequest.postValue(m);

            });


            itemView.setOnLongClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                FlightRequest m = requests.get(position);

                if (!m.getSaveID().equals("Saved") && isFlightIdUnique(m.getFlightID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you save the dataset: " + flightId.getText())
                            .setNegativeButton("Save", (dialog, cl) -> {
                                if (!m.getSaveID().equals("Saved")) {
                                    m.setSaveID(String.format(getResources().getString(R.string.save)));
                                    FlightRequest deletedItem = requests.get(position);
                                    myAdapter.notifyItemChanged(position); // Refresh the item view at the given position
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> DAO.insertCode(m));
                                    Snackbar.make(flightId, "You saved dataset #" + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click -> {
                                                // Undo the "Save" action
                                                m.setSaveID(String.format(getResources().getString(R.string.saveState)));
                                                requests.add(position, deletedItem);
                                                myAdapter.notifyItemInserted(position);
                                            })
                                            .show();
                                } else {
                                    Snackbar.make(flightId, "Dataset #" + (position + 1) + " already saved", Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            })
                            .create().show();


                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you delete this dataset: " + flightId.getText())
                            .setNegativeButton("Keep",((dialog, which) -> {}))
                            .setPositiveButton("Delete", (dialog, cl) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> DAO.deleteCode(m));

                            // Save a reference to the deleted item
                            FlightRequest deletedItem = requests.get(position);

                            // Remove the item from the dataset and update the RecyclerView
                            requests.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(flightId, "You deleted dataset #" + (position + 1), Snackbar.LENGTH_LONG)
                                    .setAction("Undo", click -> {
                                        // Re-add the deleted item to the dataset and update the RecyclerView
                                        requests.add(position, deletedItem);
                                        myAdapter.notifyItemInserted(position);

                                        if(m.getSaveID().equals("Saved")) {// Re-insert the item into the database during the Undo action
                                            Executor undoThread = Executors.newSingleThreadExecutor();
                                            undoThread.execute(() -> DAO.insertCode(deletedItem));
                                        }
                                    }).show();
                        })

                            .create().show();
                }

                return false;
            });

            code= itemView.findViewById(R.id.airCodeID);
            flightId= itemView.findViewById(R.id.flighNum);
            saveID=itemView.findViewById(R.id.saveID);


//            airlineName= itemView.findViewById(R.id.gateNumID);
//            ArrivalAirport= itemView.findViewById(R.id.arrivalID);
//            departureAirport= itemView.findViewById(R.id.delayNumID);
//            detDate= itemView.findViewById(R.id.terminalNumID);
//            detFlightID = itemView.findViewById(R.id.flightNumID);
//            detStatus= itemView.findViewById(R.id.flightStatusID);


        }


    }

}