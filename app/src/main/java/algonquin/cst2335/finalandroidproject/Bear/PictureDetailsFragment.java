package algonquin.cst2335.finalandroidproject.Bear;

import static algonquin.cst2335.finalandroidproject.Bear.BearActivity.pictures;
import static algonquin.cst2335.finalandroidproject.Bear.BearActivity.position;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Bear.Data.BearPicture;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.PictureDetailsFragmentBinding;

public class PictureDetailsFragment extends Fragment {

    BearPicture selected;
    TextView fileName;
    PictureDetailsFragment context = this;


    public PictureDetailsFragment(BearPicture bp){
        selected = bp;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        PictureDetailsFragmentBinding binding = PictureDetailsFragmentBinding.inflate(inflater);
        String files= new File(getActivity().getFilesDir(), selected.getWidth() + "-" + selected.getHeight() + "_" + selected.getTimeDownloaded()  + ".png").getPath();
        Bitmap newImage = BitmapFactory.decodeFile(files);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), newImage);
        final float roundPx = (float) newImage.getWidth() * 0.06f;
        roundedBitmapDrawable.setCornerRadius(roundPx);

        fileName = binding.fileFragment;



        getActivity().runOnUiThread(() ->{
            binding.fragmentPic.setMaxWidth(selected.getWidth());
            binding.fragmentPic.setMaxWidth(selected.getHeight());
            binding.heightFragment.setText(String.format(getResources().getString(R.string.frag_height)) + " " + selected.getHeight());
            binding.widthFragment.setText(String.format(getResources().getString(R.string.frag_width)) + " " + selected.getWidth());
            fileName.setText(String.format(getResources().getString(R.string.frag_file)) + " " + selected.getWidth() +"-" + selected.getHeight() + "_" + selected.getTimeDownloaded() + ".png");
            binding.fragmentPic.setImageDrawable(roundedBitmapDrawable);
        });

        binding.trash.setOnClickListener(clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
            builder.setMessage(String.format(getResources().getString(R.string.ask_delete)))
                    .setTitle(String.format(getResources().getString(R.string.question_title)))
                    .setNegativeButton(String.format(getResources().getString(R.string.no)), (dialog,cl) ->{})
                    .setPositiveButton(String.format(getResources().getString(R.string.yes)), (dialog,cl) ->{
                        BearPicture m = pictures.get(position);
                        Executor thread3 = Executors.newSingleThreadExecutor();
                        thread3.execute(() -> {
                            BearActivity bearVariables = (BearActivity) getActivity();

                            File fileDelete= new File(getActivity().getFilesDir(), selected.getWidth() + "-" + selected.getHeight() + ".png");
                            fileDelete.delete();
                            bearVariables.myDAO.deletePicture(m);
                            pictures.remove(position);

                            getActivity().runOnUiThread(() -> bearVariables.myAdapter.notifyDataSetChanged() );
                        });
                        getFragmentManager().beginTransaction().remove(PictureDetailsFragment.this).commit();
                    }).create().show();
        });


        return binding.getRoot();
    }
}
