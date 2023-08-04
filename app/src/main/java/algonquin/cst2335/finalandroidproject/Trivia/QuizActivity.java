package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.R;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<QuestionObj> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ArrayList<QuestionObj> questions = (ArrayList<QuestionObj>) getIntent().getSerializableExtra("questions");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null) {
            questions = (ArrayList<QuestionObj>) intent.getSerializableExtra("questions");
            if (questions != null) {
                questionAdapter = new QuestionAdapter(questions);
                recyclerView.setAdapter(questionAdapter);
            }


        }
    }
}