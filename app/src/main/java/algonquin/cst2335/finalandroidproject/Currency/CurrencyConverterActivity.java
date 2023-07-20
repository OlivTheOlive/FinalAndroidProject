package algonquin.cst2335.finalandroidproject.Currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityCurrencyConverterBinding;

public class CurrencyConverterActivity extends AppCompatActivity {

    private ActivityCurrencyConverterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String cad = prefs.getString("editTextText1","");
        String ff = prefs.getString("editTextText2","");
        String php = prefs.getString("editTextText3","");

        binding.editTextText.setText(cad);
        binding.editTextText2.setText(ff);
        binding.editTextText3.setText(php);

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



        //Toast for converter button
        binding.button.setOnClickListener(v -> {
            String inputType = binding.editTextText.getText().toString();
            String inputType2 = binding.editTextText2.getText().toString();
            String inputType3 = binding.editTextText3.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("", inputType);
            editor.putString("", inputType2);
            editor.putString("", inputType3);
            editor.apply();

                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType,+ Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType2,+ Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "Amount Converted is " + inputType3,+ Toast.LENGTH_LONG).show();

        });

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