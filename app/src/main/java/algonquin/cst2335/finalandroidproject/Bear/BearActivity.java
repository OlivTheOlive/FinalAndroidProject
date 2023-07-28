package algonquin.cst2335.finalandroidproject.Bear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.MainActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    ActivityBearBinding binding;

    TextView messageText;
    RequestQueue queue = null;

    protected  String height;
    protected String width;
    String bearPic;
    Bitmap image;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding = ActivityBearBinding.inflate(getLayoutInflater());
                queue = Volley.newRequestQueue(this);
                setSupportActionBar(binding.toolBar);
                setContentView(binding.getRoot());
                EditText hET = binding.heightEditText;
                EditText wET = binding.widthEditText;
                SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                String savedHeight = prefs.getString("Height", "");
                String savedWidth = prefs.getString("Width", "");

                hET.setText(savedHeight);
                wET.setText(savedWidth);





                messageText = binding.titleText;
                binding.generateButton.setOnClickListener(clk -> {
                    height = hET.getText().toString();
                    width = wET.getText().toString();
                    String stringURL = "https://placebear.com/" + width + "/" + height + ".png";


                    StringRequest request = new StringRequest(Request.Method.GET, stringURL,null,
                            (response) -> {

                            try{
                            String removePNG = stringURL.replace("png","jpg");
                            bearPic = width + "-" + height;


                                ImageRequest imgReq = new ImageRequest(removePNG, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        image = bitmap;
                                        binding.theBear.setImageBitmap(image);
//
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error) -> {
                                        });
                                queue.add(imgReq);
                            } catch (Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    );
                queue.add(request);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor
                            .putString("Height", height)
                            .putString("Width", width)
                            .apply();
                    Toast.makeText(this, String.format(getResources().getString(R.string.bear_size)) + " " + width + " x " + height, Toast.LENGTH_SHORT).show();

                });

                binding.theBear.setOnClickListener(clk -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);
                    builder.setMessage(String.format(getResources().getString(R.string.do_save)))
                            .setTitle(String.format(getResources().getString(R.string.question)))
                            .setNegativeButton(getResources().getString(R.string.no), (dialog, cl) -> {
                            })
                            .setPositiveButton(getResources().getString(R.string.yes), (dialog, cl) -> {

                                    try {
                                        image.compress(Bitmap.CompressFormat.PNG, 100, BearActivity.this.openFileOutput(bearPic + ".png", Activity.MODE_PRIVATE));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                        Snackbar.make(messageText, String.format(getResources().getString(R.string.img_save)), Snackbar.LENGTH_LONG).setAction(String.format(getResources().getString(R.string.undo)), click -> {
                                            File dir = getFilesDir();
                                            File file = new File(dir, bearPic + ".png");
                                            boolean deleted = deleteFile(bearPic + ".png");
                                }).show();
                            }).create().show();
                });
            }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BearActivity.this);
            builder.setMessage(String.format(getResources().getString(R.string.bear_help)))
                    .setTitle(String.format(getResources().getString(R.string.help)))
                    .setPositiveButton(String.format(getResources().getString(R.string.done)), (dialog, cl) -> {
                    }).create().show();
            return true;
        } else if (item.getItemId() == R.id.currency_menu) {
            Intent nextPage = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage);
            return true;
        } else if (item.getItemId() == R.id.aviation_menu) {
            Intent nextPage = new Intent(this, AviationTrackerActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.trivia_menu) {
            Intent nextPage = new Intent(this, TriviaActivity.class);
            startActivity(nextPage);
        }
        return true;
    }



}