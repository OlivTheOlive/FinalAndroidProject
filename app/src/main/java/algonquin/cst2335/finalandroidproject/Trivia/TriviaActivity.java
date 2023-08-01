package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;


public class TriviaActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    protected ActivityTriviaBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        setSupportActionBar(variableBinding.myToolbar);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        variableBinding.button.setOnClickListener(clk -> {
            String inputText = variableBinding.editTextNumber.getText().toString();
            editor.putString("userInputKey", inputText);
            editor.apply();

            Toast.makeText(getApplicationContext(), "You have generated " + inputText + " questions", Toast.LENGTH_SHORT).show();


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("This is an AlertDialog message.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    View view = findViewById(android.R.id.content);
                    String message = "Thank you for generating a Snackbar message.";
                    int duration = Snackbar.LENGTH_SHORT;
                    Snackbar snackbar = Snackbar.make(view, message, duration);
                    snackbar.show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    View view = findViewById(android.R.id.content);
                    String message = "Why did you cancel :(";
                    int duration = Snackbar.LENGTH_SHORT;
                    Snackbar snackbar = Snackbar.make(view, message, duration);
                    snackbar.show();
                }
            });


            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });


        // Retrieve saved user input and populate the EditText
        String savedUserInput = sharedPreferences.getString("userInputKey", "");
        variableBinding.editTextNumber.setText(savedUserInput);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.currency_img) {
            Intent nextPage = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.aviation_tracker) {
            Intent nextPage = new Intent(this, AviationTrackerActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.bear_img) {
            Intent nextPage = new Intent(this, BearActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help Guide");
            builder.setMessage("Welcome to the trivia game! Select a type of question for the trivia and pick a number from 1-50 questions. Tap on the different icons on the top right to navigate to the apps shown. ");

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return false;
    }

}

