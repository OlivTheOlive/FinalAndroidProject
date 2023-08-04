package algonquin.cst2335.finalandroidproject.Trivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalandroidproject.Aviation.AviationTrackerActivity;
import algonquin.cst2335.finalandroidproject.Bear.BearActivity;
import algonquin.cst2335.finalandroidproject.Currency.CurrencyConverterActivity;
import algonquin.cst2335.finalandroidproject.R;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;
import algonquin.cst2335.finalandroidproject.databinding.FragmentQuestionsBinding;


public class TriviaActivity extends AppCompatActivity {
    ArrayList<QuestionObj> getQuestions;
    static QuizActivityViewModel quizModel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    protected ActivityTriviaBinding variableBinding;
    protected FragmentQuestionsBinding binding;
    private ArrayList<QuestionObj> questions = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        //    setContentView(binding.getRoot());
        setSupportActionBar(variableBinding.myToolbar);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        variableBinding.animalButton.setOnClickListener(clk -> {

            String numQuestions = variableBinding.editTextNumber.getText().toString();
            String url = "https://opentdb.com/api.php?amount=" + numQuestions + "&category=27&type=multiple";

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
                                //   myAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(TriviaActivity.this, QuizActivity.class);
                                intent.putExtra("questions", questions);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSON Parsing Error", "Error parsing JSON response: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Network Error", "Error response: " + error.toString());
                        }
                    });

            requestQueue.add(request);
        });

        variableBinding.geographyButton.setOnClickListener(clk -> {
            String numQuestions = variableBinding.editTextNumber.getText().toString();
            String url = "https://opentdb.com/api.php?amount=" + numQuestions + "&category=27&type=multiple";

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

                                    Intent intent = new Intent(TriviaActivity.this, QuizActivity.class);
                                    intent.putExtra("questions", questions);
                                    startActivity(intent);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error if necessary
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error in the network request, if any
                        }


                    });


            // Retrieve saved user input and populate the EditText
            String savedUserInput = sharedPreferences.getString("userInputKey", "");
            //    variableBinding.editTextNumber.setText(savedUserInput);

        });

    }


    // Outside the click listeners, define the method to start the new activity

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

        class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

            private ArrayList<QuestionObj> questions;

            public QuestionAdapter(ArrayList<QuestionObj> questions) {
                this.questions = questions;
            }

            @NonNull
            @Override
            public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_questions, parent, false);
                return new QuestionViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
                QuestionObj question = questions.get(position);
                holder.bindQuestion(question);
            }

            @Override
            public int getItemCount() {
                return questions.size();
            }

             class QuestionViewHolder extends RecyclerView.ViewHolder {

                private TextView questionTextView;
                private TextView correctAnswerTextView;
                private TextView incorrectAnswersTextView;

                public QuestionViewHolder(@NonNull View itemView) {
                    super(itemView);
                    questionTextView = itemView.findViewById(R.id.questionTextView);
                    correctAnswerTextView = itemView.findViewById(R.id.answersRadioGroup);
                    incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton1);
                    incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton2);
                    incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton3);
                    incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton4);
                }

                public void bindQuestion(QuestionObj question) {
                    questionTextView.setText(question.getQuestionString());
                    correctAnswerTextView.setText("Correct Answer: " + question.getCorrectAnswer());
                    incorrectAnswersTextView.setText("Incorrect Answers: " + question.getIncorrectAnswers().toString());
                }
            }
        }


    }


}


