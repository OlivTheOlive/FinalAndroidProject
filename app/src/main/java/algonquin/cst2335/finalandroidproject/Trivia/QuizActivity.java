package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityQuizBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;
import algonquin.cst2335.finalandroidproject.databinding.FragmentScoresBinding;
import algonquin.cst2335.finalandroidproject.databinding.ScoreItemBinding;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<QuestionObj> questions;
    private TextView scoreTextView;
    private SharedPreferences sharedPreferences;
    private int score = 0;
    private QuizQuestionDAO quizQuestionDAO;
    protected ActivityQuizBinding binding;
    protected ActivityTriviaBinding variableBinding;

    protected ScoreItemBinding variableBindings;
    private TriviaActivity.ScoreAdapter scoreAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("UserName");
        setContentView(R.layout.activity_quiz);

        ActivityQuizBinding binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        QuestionDatabase db = Room.databaseBuilder(getApplicationContext(),
                        QuestionDatabase.class, "question-database")
                .fallbackToDestructiveMigration()
                .build();
        quizQuestionDAO = db.qqDAO();

        scoreTextView = binding.scoreTextView;

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        scoreTextView = binding.scoreTextView;
        scoreTextView.setText("Score: " + score); // initial score



        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<QuestionObj> questions = (ArrayList<QuestionObj>) getIntent().getSerializableExtra("questions");
        if (questions != null) {
            recyclerView.setAdapter(new TriviaActivity.QuestionAdapter(questions));
        } else {
            Log.e("QuizActivity", "Questions list is null");
            // handle error condition here...
        }
        Button finishQuizButton = binding.quizDoneButton;

        finishQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Executor executor = Executors.newSingleThreadExecutor();

                String userCategory = "N/A";
                int id = 0;
                final Score scoreObj = new Score(id, userName, score, userCategory);

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                // Insert the score into the database on a background thread
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Score scoreObj = new Score(id, userName, score, userCategory);
                        quizQuestionDAO.insert(scoreObj);
                    }
                });

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Score scoreObj = new Score(id,userName, score, userCategory);
                        quizQuestionDAO.deleteScore(id);
                    }
                });
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Score> scores = quizQuestionDAO.getTopScores();
                        Log.d("Database Fetch", "Scores Size: " + scores.size());



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(QuizActivity.this, LeaderBoardActivity.class);
                                intent.putExtra("scores", new ArrayList<>(scores)); // put the scores into the intent
                                startActivity(intent);
                            }
                        });
                    }
                });


                // Navigate to LeaderBoardActivity after score has been inserted
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(QuizActivity.this, LeaderBoardActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }




    public void incrementScore() {
        this.score++;
        scoreTextView.setText("Score: " + this.score); // Update the TextView text
    }





    // Display score method

}
