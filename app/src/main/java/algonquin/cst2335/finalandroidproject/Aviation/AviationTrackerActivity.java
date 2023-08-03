package algonquin.cst2335.finalandroidproject.Aviation;

import static java.lang.Character.isDigit;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityGetFlightDataBinding;
import algonquin.cst2335.finalandroidproject.databinding.AviationDetailsBinding;

public class AviationTrackerActivity extends AppCompatActivity {

    private static  final String SHARED_PREF = "Data";
    private static final String REQ_CODE = "Code";
    private  RecyclerView.Adapter myAdapter;
    private ActivityAviationTrackerBinding binding;
    private AviationDetailsBinding binding2;
    private FlightRequestDAO DAO;
    private ArrayList<FlightRequest> requests;
    private FlightRequest newReq;
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
        switch( item.getItemId() )
        {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(AviationTrackerActivity.this);
                builder.setTitle("Who Created this! ")
                        .setMessage("Version 1.0, created by Olivie Bergeron")
                        .setPositiveButton("OKiii", (dialog, cl) -> {})
                        .create().show();
                break;
            case R.id.howID:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(AviationTrackerActivity.this);
                builder2.setTitle(" How To?")
                        .setMessage("Just type an airport code to get your data")
                        .setPositiveButton("OKiii", (dialog, cl) -> {})
                        .create().show();
                break;
            default:
                break;
        }

        return true;
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


        ATViewModel.selectedRequest.observe(this, (fragReq) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            FlightRequestDetails frd = new FlightRequestDetails(fragReq);
            tx.replace(R.id.fragLocation, frd);
            tx.addToBackStack(null); // Use a unique name (e.g., "FlightRequestDetails") instead of null
            tx.commit();
        });

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
                                    JSONObject arrival = position.getJSONObject("arrival");
                                    String airportName= depart.getString("airport");
                                    JSONObject flight = position.getJSONObject("flight");
                                    String flightString = flight.getString("number");
                                    String status = position.getString("flight_status");
                                    String date = position.getString("flight_date");
                                    String departureAP = depart.getString("airport");
                                    String arrivalAP = arrival.getString("airport");
                                    JSONObject airline = position.getJSONObject("airline");
                                    String airlineN = airline.getString("name");




                                    FlightRequest newReq = new FlightRequest("AP code: "+ typed,
                                                                                "Flight #: "+flightString,
                                                                                "Airport Dep.: "+ airportName,
                                                                                "Status: "+status,
                                                                                "Not Saved");

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
                holder.saveID.setText("Not Saved");
                holder.detDate.setText("");
                holder.departureAirport.setText("");
                holder.airlineName.setText("");
                holder.ArrivalAirport.setText("");
                holder.detFlightID.setText("");


                FlightRequest obj = requests.get(position);
                holder.code.setText(obj.getCode());
                holder.flightId.setText(obj.getFlightID());
                holder.nameID.setText(obj.getNameID());
                holder.statusID.setText(obj.getStatusID());
                holder.saveID.setText(obj.getSaveID());
                holder.detStatus.setText(obj.getStatusID());
                holder.detDate.setText(obj.getDate());
                holder.departureAirport.setText(obj.getDepartureAP());
                holder.airlineName.setText(obj.getAirlineN());
                holder.ArrivalAirport.setText(obj.getArrivalAP());
                holder.detFlightID.setText(obj.getFlightID());


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
        TextView code, nameID, flightId, statusID, saveID, airlineName, ArrivalAirport,departureAirport, detDate, detFlightID, detStatus;



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
                                    m.setSaveID("Saved");
                                    FlightRequest deletedItem = requests.get(position);
                                    myAdapter.notifyItemChanged(position); // Refresh the item view at the given position
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> DAO.insertCode(m));
                                    Snackbar.make(flightId, "You saved dataset #" + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click -> {
                                                // Undo the "Save" action
                                                m.setSaveID("Not Saved");
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
            nameID= itemView.findViewById(R.id.nameID);
            flightId= itemView.findViewById(R.id.flighNum);
            statusID= itemView.findViewById(R.id.statusID);
            saveID=itemView.findViewById(R.id.saveID);
            airlineName= itemView.findViewById(R.id.airlineName);
            ArrivalAirport= itemView.findViewById(R.id.ArrivalAirport);
            departureAirport= itemView.findViewById(R.id.departureAirport);
            detDate= itemView.findViewById(R.id.detDate);
            detFlightID = itemView.findViewById(R.id.detFlightID);
            detStatus= itemView.findViewById(R.id.detStatus);


        }


    }

}