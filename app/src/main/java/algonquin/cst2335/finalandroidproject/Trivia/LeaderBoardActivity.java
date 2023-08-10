package algonquin.cst2335.finalandroidproject.Trivia;

import static algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity.quizQuestionDAO;
import static algonquin.cst2335.finalandroidproject.Trivia.TriviaActivity.scoreArrayList;

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

/**
 * Activity to display the leader board for the trivia game.
 * <p>
 * This activity showcases the top scores of the trivia game in a RecyclerView. Each score is an instance
 * of the {@link Score} class and is presented in a list format. The scores can be clicked to view
 * more details, although that functionality appears to be commented out in this version.
 * </p>
 */
public class LeaderBoardActivity extends AppCompatActivity {

    /** ViewModel to interact with quiz data. */
    private QuizActivityViewModel quizModel;

    /** RecyclerView to display the list of scores. */
    private RecyclerView recyclerView;

    /** List of scores to be displayed in the leader board. */
    private ArrayList<Score> scores;

    /** Binding instance for the activity layout. */
    private ActivityLeaderBoardBinding binding;

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

        recyclerView.setOnClickListener(view -> {

        });

    }




    /**
     * Overrides the default back button behavior. If there are fragments on the back stack,
     * it will pop the last fragment. Otherwise, the super method is called.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}





