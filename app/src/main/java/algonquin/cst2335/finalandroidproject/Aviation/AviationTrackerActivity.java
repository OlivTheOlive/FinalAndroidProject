package algonquin.cst2335.finalandroidproject.Aviation;

import static java.lang.Character.isDigit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.zip.Inflater;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityAviationTrackerBinding;

public class AviationTrackerActivity extends AppCompatActivity {

    private ActivityAviationTrackerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(click ->{
            String typed = binding.editTextText.getText().toString();

            if(checkCode(typed) == true){
            }else {
            }
        });
    }
    //if the amount of capital letters do not match the length, show error in typed code.
    boolean checkCode(String cd) {
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
}