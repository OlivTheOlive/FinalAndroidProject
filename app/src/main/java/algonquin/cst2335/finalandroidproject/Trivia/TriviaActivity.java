package algonquin.cst2335.finalandroidproject.Trivia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;
import androidx.room.Room;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;
import algonquin.cst2335.finalandroidproject.databinding.FragmentScoresBinding;
import algonquin.cst2335.finalandroidproject.databinding.ScoreItemBinding;


public class TriviaActivity extends AppCompatActivity {
    ArrayList<QuestionObj> getQuestions;
    static QuizActivityViewModel quizModel;
    private ScoreAdapter scoreAdapter;
    SharedPreferences sharedPreferences;
    public static QuizQuestionDAO quizQuestionDAO;
    private List<Score> scores;
    QuestionDatabase db;
    SharedPreferences.Editor editor;
    protected ActivityTriviaBinding variableBinding;
    protected FragmentScoresBinding binding;
    private EditText userNameEditText;


    protected ScoreItemBinding variableBindings;
    private ArrayList<QuestionObj> questions = new ArrayList<>();

    static ArrayList<Score> scoreArrayList;

    private static RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        setSupportActionBar(variableBinding.myToolbar);
        FragmentScoresBinding binding = FragmentScoresBinding.inflate(getLayoutInflater());
        ScoreItemBinding variableBindings = ScoreItemBinding.inflate(getLayoutInflater());


        QuizActivityViewModel scoreModel = new ViewModelProvider(this).get(QuizActivityViewModel.class);
        scores = scoreModel.selectedScore.getValue();
        db = Room.databaseBuilder(getApplicationContext(), QuestionDatabase.class, "scores_database")
                .fallbackToDestructiveMigration()
                .build();
        quizQuestionDAO = db.qqDAO();
        if (scores == null) {

            scoreModel.selectedScore.setValue(scores = new ArrayList<Score>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                scores.addAll(quizQuestionDAO.getTopScores());
          //      runOnUiThread(() -> variableBindings.setAdapter(myAdapter));
            });
        }

        userNameEditText = findViewById(R.id.usrName);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        quizModel = new ViewModelProvider(this).get(QuizActivityViewModel.class);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String storedNumQuestions = sharedPreferences.getString("NumQuestions", "0");
        variableBinding.editTextNumber.setText(storedNumQuestions);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        variableBinding.animalButton.setOnClickListener(clk -> {
            executorService.execute(() -> {
                questions.clear();

                String numQuestions = variableBinding.editTextNumber.getText().toString();
                editor.putString("NumQuestions", numQuestions);
                editor.apply();

                String url = "https://opentdb.com/api.php?amount=" + numQuestions + "&category=27&type=multiple";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TriviaActivity.this, "Number of questions generated: " + numQuestions, Toast.LENGTH_SHORT).show();
                    }
                });

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Network Response", "Response received!");
                                try {
                                    JSONArray results = response.getJSONArray("results");
                                    int length = results.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject question = results.getJSONObject(i);
                                        String questionString = question.getString("question");
                                        String correctAnswer = question.getString("correct_answer");
                                        JSONArray incorrectAnswers = question.getJSONArray("incorrect_answers");
                                        int incorrectLength = incorrectAnswers.length();
                                        ArrayList<String> incorrectTexts = new ArrayList<>();
                                        for (int j = 0; j < incorrectLength; j++) {
                                            incorrectTexts.add(incorrectAnswers.getString(j));
                                        }
                                        int q = 0;
                                        questions.add(new QuestionObj(questionString, correctAnswer, incorrectTexts));

                                    }

                                    Intent intent = new Intent(TriviaActivity.this, QuizActivity.class);
                                    intent.putExtra("questions", questions);
                                    intent.putExtra("UserName", variableBinding.usrName.getText().toString());
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("JSON Parsing Error", "Error parsing JSON response: " + e.getMessage());
                                }
                            }
                        },

                        error -> {
                            int i = 0;
                        });

                requestQueue.add(request);
            });
        });

        variableBinding.geographyButton.setOnClickListener(clk -> {
            executorService.execute(() -> {
                questions.clear();

                String numQuestions = variableBinding.editTextNumber.getText().toString();
                editor.putString("NumQuestions", numQuestions);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TriviaActivity.this, "Number of questions generated: " + numQuestions, Toast.LENGTH_SHORT).show();
                    }
                });

                String url = "https://opentdb.com/api.php?amount=" + numQuestions + "&category=22&type=multiple";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray results = response.getJSONArray("results");
                                    int length = results.length();

                                    for (int i = 0; i < length; i++) {
                                        JSONObject question = results.getJSONObject(i);
                                        String questionString = question.getString("question");
                                        String correctAnswer = question.getString("correct_answer");
                                        JSONArray incorrectAnswers = question.getJSONArray("incorrect_answers");
                                        int incorrectLength = incorrectAnswers.length();
                                        ArrayList<String> incorrectTexts = new ArrayList<>();
                                        for (int j = 0; j < incorrectLength; j++) {
                                            incorrectTexts.add(incorrectAnswers.getString(j));
                                        }
                                        int q = 0;
                                        questions.add(new QuestionObj(questionString, correctAnswer, incorrectTexts));

                                    }

                                    String userName = variableBinding.usrName.getText().toString();

                                    Intent intent = new Intent(TriviaActivity.this, QuizActivity.class);
                                    intent.putExtra("questions", questions);
                                    intent.putExtra("UserName", userName);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSON parsing error if necessary
                                }
                            }

                        },

                        error -> {
                            int i = 0;
                        });

                requestQueue.add(request);


            });
        });
        variableBinding.leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TriviaActivity.this, LeaderBoardActivity.class);
                startActivity(intent);
            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.currency_img) {
            Intent nextPage = new Intent(this, CurrencyConverterActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.aviation_tracker) {
            Intent nextPage = new Intent(this, AviationTrackerActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.bear_img) {
            Intent nextPage = new Intent(this, BearActivity.class);
            startActivity(nextPage);
        } else if (item.getItemId() == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help Guide");
            builder.setMessage("Welcome to the trivia game! Select a type of question for the trivia and pick a number from 1-50 questions. Tap on the different icons on the top right to navigate to the apps shown. ");

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return false;


    }

    static class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

        private ArrayList<QuestionObj> questions;

        public QuestionAdapter(ArrayList<QuestionObj> questions) {
            this.questions = questions;
        }

        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
            return new QuestionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            QuestionObj question = questions.get(position);
            Log.d("QuizActivity", "Question at position " + position + ": " + question);
            holder.bindQuestion(question);
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        class QuestionViewHolder extends RecyclerView.ViewHolder {

            private TextView questionTextView;
            private RadioGroup correctAnswerRadioGroup;
            private TextView incorrectAnswersTextView;

            public QuestionViewHolder(@NonNull View itemView) {
                super(itemView);
                questionTextView = itemView.findViewById(R.id.questionTextView);
                correctAnswerRadioGroup = itemView.findViewById(R.id.answersRadioGroup);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton1);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton2);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton3);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton4);
            }

            public void bindQuestion(QuestionObj question) {
                questionTextView.setText(question.getQuestionString());
                correctAnswerRadioGroup.removeAllViews();  // clear all old views

                ArrayList<String> answers = new ArrayList<>();
                answers.add(question.getCorrectAnswer());
                answers.addAll(question.getIncorrectAnswers());

                Collections.shuffle(answers);

                for (String answer : answers) {
                    RadioButton button = new RadioButton(itemView.getContext());
                    button.setText(answer);
                    correctAnswerRadioGroup.addView(button);
                }

                // set OnCheckedChangeListener here
                correctAnswerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedButton = group.findViewById(checkedId);
                        if (selectedButton.getText().equals(question.getCorrectAnswer())) {
                            ((QuizActivity) itemView.getContext()).incrementScore();
                        }

                    }
                });
            }


        }
    }


    public static class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

        public List<Score> scores;
        private Context context;

        public ScoreAdapter(Context context, List<Score> scores) {
            this.context = context;
            this.scores = (scores != null) ? scores : new ArrayList<>();
        }

        @NonNull
        @Override
        public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
            return new ScoreViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAbsoluteAdapterPosition();
                    Score selectedScore = scoreArrayList.get(position);

                    // pass the selected score to your ViewModel
                    quizModel.selectedScore.setValue((List<Score>) selectedScore);

                    // Replace the existing fragment with your ScoresFragment
                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new ScoresFragment(selectedScore));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });

            Score currentScore = scores.get(position);
            holder.playerNameTextView.setText(currentScore.getPlayerName());
            holder.playerScoreTextView.setText(String.valueOf(currentScore.getPlayerScore()));
            holder.categoryTextView.setText(currentScore.getCategory());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Confirmation")
                            .setMessage("Do you want to delete the score: " + currentScore.getPlayerScore() + "?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Score s = scores.get(position);
                                    scores.remove(position);
                                    notifyItemRemoved(position);

                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        quizQuestionDAO.deleteScore(s.getId());
                                    });

                                    Snackbar.make(holder.itemView, "Score deleted", Snackbar.LENGTH_LONG)
                                            .setAction("Undo", v1 -> {
                                                scores.add(position, s);
                                                notifyItemInserted(position);
                                                thread.execute(() -> {
                                                    quizQuestionDAO.insert(s);
                                                });
                                            })
                                            .show();
                                }
                            })
                            .create()
                            .show();

                }
            });
        }


        @Override
        public int getItemCount() {
            return scores.size();
        }

        public class ScoreViewHolder extends RecyclerView.ViewHolder {
            private Context context;
            private TextView playerNameTextView;
            private TextView playerScoreTextView;
            private TextView categoryTextView;

            private TextView deleteButton;

            public ScoreViewHolder(@NonNull View itemView) {
                super(itemView);
                this.context = context;
                itemView.setOnClickListener(clk -> {

                    int position = getAbsoluteAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Question:")
                            .setMessage("Do you want to delete the score: " + playerScoreTextView.getText())
                            .setNegativeButton("No", (dialog, cl) -> {
                            })
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                Score s = scoreArrayList.get(position);
                                thread.execute(() -> {
                                    quizQuestionDAO.deleteScore(s.id);
                                });
                                scoreArrayList.remove(position);
                                myAdapter.notifyItemRemoved(position);
                                Snackbar.make(playerScoreTextView, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", click -> {
                                            scoreArrayList.add(position, s);
                                        })

                                        .show();
                            })

                            .create().show();


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyItemInserted(position);
                        }
                    });

                });

                playerNameTextView = itemView.findViewById(R.id.player_name);
                playerScoreTextView = itemView.findViewById(R.id.score);
                categoryTextView = itemView.findViewById(R.id.category);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }

        }
    }

    }

