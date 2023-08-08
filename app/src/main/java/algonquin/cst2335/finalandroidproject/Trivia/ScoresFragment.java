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


public class ScoresFragment extends Fragment {
    QuizActivityViewModel quizModel;
    Score selected;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentScoresBinding binding = FragmentScoresBinding.inflate(inflater);

        quizModel = new ViewModelProvider(requireActivity()).get(QuizActivityViewModel.class);

        Score selectedScore = (Score) quizModel.selectedScore.getValue();

        if (selectedScore != null) {
            binding.fragPlayerName.setText(selectedScore.getPlayerName());
            binding.fragScore.setText(String.valueOf(selectedScore.getPlayerScore()));
            binding.fragCategory.setText(selectedScore.getCategory());
        }

        return binding.getRoot();
    }

    public ScoresFragment(Score s) {
selected = s;
    }
}