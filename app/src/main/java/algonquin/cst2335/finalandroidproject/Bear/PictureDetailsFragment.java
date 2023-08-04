package algonquin.cst2335.finalandroidproject.Bear;

import static algonquin.cst2335.finalandroidproject.Bear.BearActivity.pictures;
import static algonquin.cst2335.finalandroidproject.Bear.BearActivity.position;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Bear.Data.BearPicture;
import algonquin.cst2335.finalandroidproject.Bear.Data.BearPictureDAO;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.PictureDetailsFragmentBinding;

public class PictureDetailsFragment extends Fragment implements View.OnLongClickListener {

    BearPicture selected;

    public PictureDetailsFragment(BearPicture bp){
        selected = bp;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PictureDetailsFragmentBinding binding = PictureDetailsFragmentBinding.inflate(inflater);
        String files =  "/data/data/algonquin.cst2335.finalandroidproject/files/" + selected.getWidth() + "-" + selected.getHeight() + ".png";
        Bitmap newImage = BitmapFactory.decodeFile(files);

        binding.heightFragment.setText(String.format(getResources().getString(R.string.frag_height)) + selected.getHeight());
        binding.widthFragment.setText(String.format(getResources().getString(R.string.frag_width)) + selected.getWidth());
        binding.fileFragment.setText(String.format(getResources().getString(R.string.frag_file)) + selected.getWidth() +"-" + selected.getHeight() + ".png");
        binding.fragmentPic.setImageBitmap(newImage);

        binding.fragmentPic.setOnLongClickListener(this);


        return binding.getRoot();
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setMessage(String.format(getResources().getString(R.string.ask_delete)))
                .setTitle(String.format(getResources().getString(R.string.question)))
                .setNegativeButton(String.format(getResources().getString(R.string.no)), (dialog,cl) ->{})
                .setPositiveButton(String.format(getResources().getString(R.string.yes)), (dialog,cl) ->{
                    BearPicture m = pictures.get(position);
                    Executor thread3 = Executors.newSingleThreadExecutor();
                    thread3.execute(() -> {
                        BearActivity bearVariables = (BearActivity) getActivity();

                        bearVariables.myDAO.deletePicture(m);
                        pictures.remove(position);
                        getActivity().runOnUiThread(() -> bearVariables.myAdapter.notifyDataSetChanged() );
                    });
                    getFragmentManager().beginTransaction().remove(PictureDetailsFragment.this).commit();
                }).create().show();

        return false;
    }
}
