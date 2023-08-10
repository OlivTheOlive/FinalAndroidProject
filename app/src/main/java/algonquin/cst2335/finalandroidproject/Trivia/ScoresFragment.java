package algonquin.cst2335.finalandroidproject.Trivia;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.finalandroidproject.databinding.FragmentScoresBinding;

/**
 * A fragment class that displays the score details of a player.
 * <p>
 * This fragment shows details like the player's name, score, and the category they played in.
 * </p>
 */
public class ScoresFragment extends Fragment {
    QuizActivityViewModel quizModel;
    /** The selected score details to be displayed */
    Score selected;

    /**
     * Overridden method to create and return the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentScoresBinding binding = FragmentScoresBinding.inflate(inflater);

        if (selected != null) {
            binding.fragPlayerName.setText(selected.getPlayerName());
            binding.fragScore.setText(String.valueOf(selected.getPlayerScore()));
            binding.fragCategory.setText(selected.getCategory());
        }

        return binding.getRoot();
    }

    /**
     * Constructor for ScoresFragment that sets the selected score.
     *
     * @param s The score to be displayed in the fragment
     */
    public ScoresFragment(Score s) {
selected = s;
    }
}