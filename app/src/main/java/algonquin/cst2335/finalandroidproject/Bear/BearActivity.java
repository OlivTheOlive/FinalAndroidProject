package algonquin.cst2335.finalandroidproject.Bear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.Data.BearPicture;
import algonquin.cst2335.finalandroidproject.Bear.Data.BearPictureDAO;
import algonquin.cst2335.finalandroidproject.Bear.Data.BearPictureViewModel;
import algonquin.cst2335.finalandroidproject.Bear.Data.PictureDatabase;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalandroidproject.databinding.ActivityBearBinding;

public class BearActivity extends AppCompatActivity {

    ActivityBearBinding binding;

    TextView messageText;
    RequestQueue queue = null;

    protected String height;
    protected String width;

    static BearPictureViewModel pictureModel;
    String bearPic;
    Bitmap image;
    BearPictureDAO myDAO;
    static int position;
    Context context = this;

    public static ArrayList<BearPicture> pictures;


    RecyclerView.Adapter myAdapter;

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
        PictureDatabase db = Room.databaseBuilder(getApplicationContext(), PictureDatabase.class,"database-name").build();
        myDAO = db.bpDAO();
        setSupportActionBar(binding.toolBar);
        setContentView(binding.getRoot());
        pictureModel = new ViewModelProvider(this).get(BearPictureViewModel.class);
        pictures = pictureModel.pictures.getValue();
        EditText hET = binding.heightEditText;
        EditText wET = binding.widthEditText;
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedHeight = prefs.getString("Height", "");
        String savedWidth = prefs.getString("Width", "");
        hET.setText(savedHeight);
        wET.setText(savedWidth);

        if(pictures == null){
            pictureModel.pictures.postValue(pictures = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<BearPicture> fromDatabase = myDAO.getAllPictures();
                pictures.addAll(fromDatabase);

                runOnUiThread(() -> binding.recyclerView2.setAdapter(myAdapter));
            });

        }
        pictureModel.selectedPicture.observe(this, newMessageValue -> {
            if(newMessageValue != null){
                PictureDetailsFragment fragment = new PictureDetailsFragment( newMessageValue );
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();

                tx.replace(R.id.fragmentLocation, fragment);
                tx.addToBackStack("");
                tx.commit();
            }

        });
        messageText = binding.titleText;
        binding.generateButton.setOnClickListener(clk -> {
            height = hET.getText().toString();
            width = wET.getText().toString();
            String stringURL = "https://placebear.com/" + width + "/" + height + ".png";


            StringRequest request = new StringRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                        try {
                            String removePNG = stringURL.replace("png", "jpg");
                            bearPic = width + "-" + height;


                            ImageRequest imgReq = new ImageRequest(removePNG, bitmap -> {
                                image = bitmap;
                                binding.theBear.setImageBitmap(image);

                            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    (error) -> {
                                    });
                            queue.add(imgReq);
                        } catch (Exception e) {
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
                        BearPicture newPicture = new BearPicture(Integer.valueOf(height),Integer.valueOf(width));
                        Executor thread1 = Executors.newSingleThreadExecutor();
                        pictures.add(newPicture);

                        thread1.execute(() -> {
                            newPicture.id = myDAO.insertPicture(newPicture);

                        });
                        myAdapter.notifyDataSetChanged();
                        binding.recyclerView2.scrollToPosition(pictures.size()-1);
                        try {
                            image.compress(Bitmap.CompressFormat.PNG, 100, BearActivity.this.openFileOutput(bearPic + ".png", Activity.MODE_PRIVATE));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(messageText, String.format(getResources().getString(R.string.img_save)), Snackbar.LENGTH_LONG).setAction(String.format(getResources().getString(R.string.undo)), click -> {
                            BearPicture m = pictures.get(pictures.size()-1);
                            Executor thread3 = Executors.newSingleThreadExecutor();
                            thread3.execute(() -> {
                                myDAO.deletePicture(m);
                                pictures.remove(m);
                                runOnUiThread(()->{
                                    myAdapter.notifyDataSetChanged();
                                });
                        });
                        }).show();
                    }).create().show();
        });

        binding.showPictures.setOnClickListener(clk -> {

            if (binding.showPictures.getText().equals(String.format(getResources().getString(R.string.show_save)))) {
                runOnUiThread(() -> {
                    binding.showPictures.setText(String.format(getResources().getString(R.string.hide_save)));
                    binding.recyclerView2.setVisibility(View.VISIBLE);
                });
            } else
                runOnUiThread(() -> {
                    binding.showPictures.setText(String.format(getResources().getString(R.string.show_save)));
                    binding.recyclerView2.setVisibility(View.GONE);
                });
        });



        binding.recyclerView2.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {


            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(BearActivity.this).inflate(R.layout.bear_picture,parent,false);
                return new MyRowHolder(view);

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                BearPicture picture = pictures.get(position);
                String files= new File(context.getFilesDir(), picture.getWidth() + "-" + picture.getHeight() + ".png").getPath();

                Bitmap newImage = BitmapFactory.decodeFile(files);
                holder.imgView.setImageBitmap(newImage);


            }

            @Override
            public int getItemCount() {
                return pictures.size();
            }

            @Override
            public int getItemViewType(int position) {
                BearPicture picture = pictures.get(position);
                return 1;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BearActivity.this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerView2.setLayoutManager(linearLayoutManager);
        binding.recyclerView2.setAdapter(myAdapter);
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
        } else if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder( BearActivity.this );
            builder.setMessage(String.format(getResources().getString(R.string.ask_delete)))
                    .setTitle(String.format(getResources().getString(R.string.question)))
                    .setNegativeButton(String.format(getResources().getString(R.string.no)), (dialog,cl) ->{})
                    .setPositiveButton(String.format(getResources().getString(R.string.yes)), (dialog,cl) ->{
                        BearPicture m = pictures.get(position);
                        Executor thread3 = Executors.newSingleThreadExecutor();
                        thread3.execute(() -> {
                            myDAO.deletePicture(m);
                            pictures.remove(position);
                            runOnUiThread(()->{
                                myAdapter.notifyDataSetChanged();
                            });

                        });
                    }).create().show();
        }
        return true;
    }
}

    class MyRowHolder extends RecyclerView.ViewHolder {
    ImageView imgView;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.theBear);
            itemView.setOnClickListener(clk->{
                BearActivity.position = getAbsoluteAdapterPosition();
                 BearPicture selected = BearActivity.pictures.get(BearActivity.position);
                 BearActivity.pictureModel.selectedPicture.postValue(selected);

                    });

        }





    }




