package algonquin.cst2335.finalandroidproject.Trivia;

import static algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity.quizQuestionDAO;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityLeaderBoardBinding;
import algonquin.cst2335.finalandroidproject.databinding.ScoreItemBinding;

public class LeaderBoardActivity extends AppCompatActivity {

    private QuizActivityViewModel quizModel;
    private RecyclerView recyclerView;
    private ArrayList<Score> scores;
    private ActivityLeaderBoardBinding binding;
    private ScoreItemBinding variableBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        quizModel = new ViewModelProvider(this).get(QuizActivityViewModel.class);
        binding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recycleView); // Replace with your RecyclerView id
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        scores = (ArrayList<Score>) intent.getSerializableExtra("scores");

        if (scores != null) {
            Log.d("LeaderBoardActivity", "Scores Size: " + scores.size());
            for(Score score: scores){
                Log.d("LeaderBoardActivity", "Score: " + score); //Add toString method in your Score class to print the details
            }
        } else {
            Log.d("LeaderBoardActivity", "Scores are null");
        }

        TriviaActivity.ScoreAdapter adapter = new TriviaActivity.ScoreAdapter(this, scores);
        recyclerView.setAdapter(adapter);

        quizModel.selectedScore.observe(this, (newValue) -> {
            ScoresFragment leaderBoardFragment = new ScoresFragment((Score) newValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragment_container,  leaderBoardFragment);
            tx.addToBackStack("");
            tx.commit();
        });



    }
}





