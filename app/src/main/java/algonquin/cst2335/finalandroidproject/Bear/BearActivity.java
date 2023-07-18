package algonquin.cst2335.finalandroidproject.Bear;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;


import algonquin.cst2335.finalandroidproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    ActivityBearBinding binding;

    TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditText hET = binding.heightEditText;
        EditText wET = binding.widthEditText;
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedHeight = prefs.getString("Height","");
        String savedWidth = prefs.getString("Width","");
        hET.setText(savedHeight);
        wET.setText(savedWidth);


        messageText = binding.titleText;
        binding.generateButton.setOnClickListener(clk ->{
            String height = hET.getText().toString();
            String width = wET.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor
                    .putString("Height",height)
                    .putString("Width",width)
                    .apply();
            Toast.makeText(this, "Bear Image Size: " + height + "x" + width , Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder( BearActivity.this );
            builder.setMessage("Do you want to save this image? " )
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog,cl) ->{})
                    .setPositiveButton("Yes", (dialog,cl) ->{
                        Snackbar.make(messageText,"Image Saved", Snackbar.LENGTH_LONG).setAction("Undo", click -> {
                        }).show();
                    }).create().show();
        });




}
}