package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.R;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<QuestionObj> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questions = (ArrayList<QuestionObj>) getIntent().getSerializableExtra("questions");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up your RecyclerView adapter using the 'questions' data
        ArrayAdapter<QuestionObj> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questions);
        recyclerView.setAdapter(adapter);
    }

            }


