package algonquin.cst2335.finalandroidproject.Bear;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidproject.Bear.Data.BearPicture;
import algonquin.cst2335.finalandroidproject.databinding.PictureDetailsFragmentBinding;

public class PictureDetailsFragment extends Fragment {

    BearPicture selected;

    public PictureDetailsFragment(BearPicture bp){
        selected = bp;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PictureDetailsFragmentBinding binding = PictureDetailsFragmentBinding.inflate(inflater);
        String files =  "/data/data/algonquin.cst2335.finalandroidproject/files/" + selected.getWidth() + "-" + selected.getHeight() + ".png";
        Bitmap newImage = BitmapFactory.decodeFile(files);

        binding.heightFragment.setText("Height: " + selected.getHeight());
        binding.widthFragment.setText("Width: " + selected.getWidth());
        binding.fileFragment.setText("File: " + selected.getWidth() +"-" + selected.getHeight() + ".png");
        binding.fragmentPic.setImageBitmap(newImage);

        return binding.getRoot();
    }
}
