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

/**
 * The main activity class for the Aviation Tracker application.
 */
public class AviationTrackerActivity extends AppCompatActivity {

    /**
     * Key for accessing the shared preferences.
     */
    private static final String SHARED_PREF = "Data";
    /**
     * Key for storing and retrieving request codes in shared preferences.
     */
    private static final String REQ_CODE = "Code";
    /**
     * Adapter for managing the RecyclerView.
     */
    private RecyclerView.Adapter myAdapter;
    /**
     * Binding instance for the ActivityAviationTracker layout.
     */
    private ActivityAviationTrackerBinding binding;
    /**
     * Data Access Object for interacting with flight request data.
     */
    private FlightRequestDAO DAO;
    /**
     * ArrayList containing flight request objects.
     */
    private ArrayList<FlightRequest> requests;
    /**
     * SharedPreferences instance for storing persistent data.
     */
    private SharedPreferences sharedPreferences;
    /**
     * RequestQueue for managing network requests.
     */
    private RequestQueue queue = null;
    /**
     * ViewModel for managing UI-related data and operations.
     */
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
            builder.setTitle(about).setMessage(aboutMess).setPositiveButton(message, (dialog, cl) -> {
            }).create().show();

        } else if (item.getItemId() == R.id.howID) {
            String howTo = String.format(getResources().getString(R.string.AviationHowTo));
            String howToMess = String.format(getResources().getString(R.string.AviationHowToMessage));
            String message = String.format(getResources().getString(R.string.Okkii));
            AlertDialog.Builder builder2 = new AlertDialog.Builder(AviationTrackerActivity.this);
            builder2.setTitle(howTo).setMessage(howToMess).setPositiveButton(message, (dialog, cl) -> {
            }).create().show();
        } else if (item.getItemId() == R.id.saved) {
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
            thread.execute(() -> {
                requests.addAll(DAO.getAllMessage());
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });
        }

        setSupportActionBar(binding.toolbar);
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String savedCode = sharedPreferences.getString(REQ_CODE, "");
        binding.editTextText.setText(savedCode);


        ATViewModel.selectedRequest.observe(this, (newValue) -> {
            FlightRequestDetails dialogFragment = new FlightRequestDetails(newValue);
            dialogFragment.show(getSupportFragmentManager(), "FlightRequestDetailsDialog");
        });


        binding.button.setOnClickListener(click -> {
            String typed = binding.editTextText.getText().toString().toUpperCase();
            String url = "https://api.aviationstack.com/v1/flights?access_key=9694046b348f410681e40e36750f3730&dep_iata=" + typed;
            if (checkCode(typed)) {
                queue = Volley.newRequestQueue(this);
                Executor thread = Executors.newSingleThreadExecutor();

                ArrayList<FlightRequest> savedItems = new ArrayList<>();
                for (FlightRequest request : requests) {
                    if (request.getSaveID().equals("Saved")) {
                        savedItems.add(request);
                    }
                }
                requests.clear();
                requests.addAll(savedItems);
                myAdapter.notifyDataSetChanged();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject position = dataArray.getJSONObject(i);
                            JSONObject arrival = position.getJSONObject("arrival");
                            String delay = arrival.getString("delay");
                            JSONObject flight = position.getJSONObject("flight");
                            String flightString = flight.getString("number");
                            String status = position.getString("flight_status");
                            String gate = arrival.getString("gate");
                            String terminal = arrival.getString("terminal");
                            String arrivalAP = arrival.getString("airport");
                            JSONObject airline = position.getJSONObject("airline");
                            String airlineN = airline.getString("name");

                            String apCode = typed;
                            String flightNum = flightString;
                            String delayTime = delay;
                            String statusString = status;
                            String saveState = "Not Saved";
                            String gateNum = gate;
                            String terminalNum = terminal;
                            String arrivalAirport = arrivalAP;
                            String airlineName = airlineN;

                            FlightRequest newReq = new FlightRequest(apCode, flightNum, delayTime, statusString, saveState, gateNum, terminalNum, arrivalAirport, airlineName);

                            if (!isDuplicateFlight(newReq)) {

                                requests.add(newReq);
                                myAdapter.notifyItemInserted(requests.size() - 1);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(this, String.format(getResources().getString(R.string.apiCrash)), duration).show();
                    error.printStackTrace();

                });
                queue.add(request);
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


    /**
     * Checks if the typed code is valid by verifying its length and the number of capital letters.
     *
     * @param cd The typed code to check
     * @return True if the code is valid, false otherwise
     */
    boolean checkCode(@NonNull String cd) {
        int duration = Toast.LENGTH_SHORT;
        int counter = 0;
        char c;
        if (cd.length() != 3) {
            Toast.makeText(this, String.format(getResources().getString(R.string.codeCheckNegative)), duration).show();
            return false;
        } else {
            for (int i = 0; i < cd.length(); i++) {
                c = cd.charAt(i);
                if (isDigit(c)) {
                    counter++;
                }
            }
        }
        if (cd.length() - counter == 3) {
            Toast.makeText(this, String.format(getResources().getString(R.string.codeCheck)), duration).show();
            return true;
        } else {
            Toast.makeText(this, String.format(getResources().getString(R.string.codeCheckNegative)), duration).show();
            return false;
        }
    }

    /**
     * Checks if a flight request is a duplicate based on the code and flight ID.
     *
     * @param newReq The new flight request to check for duplicates
     * @return True if the flight request is a duplicate, false otherwise
     */
    private boolean isDuplicateFlight(FlightRequest newReq) {
        for (FlightRequest request : requests) {
            if (request.getCode().equals(newReq.getCode()) && request.getFlightID().equals(newReq.getFlightID())) {
                return true; // Found a duplicate
            }
        }
        return false; // No duplicates found
    }

    /**
     * ViewHolder class for displaying and managing individual rows in a RecyclerView.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        /**
         * TextView for displaying the code, the flightid and the saveID.
         */
        TextView code, flightId, saveID;

        /**
         * Checks if a given flight ID is unique within the database.
         *
         * @param flightId The flight ID to check for uniqueness.
         * @return True if the flight ID is unique (not found in the database), otherwise false.
         */
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

        /**
         * Constructor which creates a MyRowHolder instance.
         *
         * @param itemView The view representing an individual row in the RecyclerView.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                FlightRequest m = requests.get(position);
                ATViewModel.selectedRequest.postValue(m);

            });


            itemView.setOnLongClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                FlightRequest m = requests.get(position);

                if (!m.getSaveID().equals("Saved") && isFlightIdUnique(m.getFlightID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                    builder.setTitle(String.format(getResources().getString(R.string.question_title))).setMessage(String.format(getResources().getString(R.string.dataSetQestionSave)) + flightId.getText()).setNegativeButton(String.format(getResources().getString(R.string.doYouSave)), (dialog, cl) -> {
                        if (!m.getSaveID().equals("Saved")) {
                            m.setSaveID("Saved");
                            FlightRequest deletedItem = requests.get(position);
                            myAdapter.notifyItemChanged(position); // Refresh the item view at the given position
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> DAO.insertCode(m));
                            Snackbar.make(flightId, String.format(getResources().getString(R.string.youSaved)) + (position + 1), Snackbar.LENGTH_LONG).setAction(String.format(getResources().getString(R.string.undoAviation)), click -> {
                                // Undo the "Save" action
                                m.setSaveID("Saved");
                                requests.add(position, deletedItem);
                                myAdapter.notifyItemInserted(position);
                            }).show();
                        } else {
                            Snackbar.make(flightId, String.format(getResources().getString(R.string.dataSetNum)) + (position + 1) + String.format(getResources().getString(R.string.alreadySaveAv)), Snackbar.LENGTH_LONG).show();
                        }
                    }).create().show();


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                    builder.setTitle(String.format(getResources().getString(R.string.question_title))).setMessage(String.format(getResources().getString(R.string.doYouDelete)) + flightId.getText()).setNegativeButton(String.format(getResources().getString(R.string.keepAviation)), ((dialog, which) -> {
                            })).setPositiveButton(String.format(getResources().getString(R.string.delete_button)), (dialog, cl) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(() -> DAO.deleteCode(m));

                                // Save a reference to the deleted item
                                FlightRequest deletedItem = requests.get(position);

                                // Remove the item from the dataset and update the RecyclerView
                                requests.remove(position);
                                myAdapter.notifyItemRemoved(position);

                                Snackbar.make(flightId, String.format(getResources().getString(R.string.youDelete)) + (position + 1), Snackbar.LENGTH_LONG).setAction(String.format(getResources().getString(R.string.undoAviation)), click -> {
                                    // Re-add the deleted item to the dataset and update the RecyclerView
                                    requests.add(position, deletedItem);
                                    myAdapter.notifyItemInserted(position);

                                    if (m.getSaveID().equals("Saved")) {// Re-insert the item into the database during the Undo action
                                        Executor undoThread = Executors.newSingleThreadExecutor();
                                        undoThread.execute(() -> DAO.insertCode(deletedItem));
                                    }
                                }).show();
                            })

                            .create().show();
                }

                return false;
            });

            code = itemView.findViewById(R.id.airCodeID);
            flightId = itemView.findViewById(R.id.flighNum);
            saveID = itemView.findViewById(R.id.saveID);
        }


    }

}