package algonquin.cst2335.finalandroidproject.Trivia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
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
import algonquin.cst2335.finalandroidproject.databinding.ActivityLeaderBoardBinding;
import algonquin.cst2335.finalandroidproject.databinding.ActivityTriviaBinding;
import algonquin.cst2335.finalandroidproject.databinding.FragmentScoresBinding;
import algonquin.cst2335.finalandroidproject.databinding.ScoreItemBinding;


/**
 * Main activity for the trivia game.
 * <p>
 * This activity allows users to select a trivia category and specify the number of questions they'd like to attempt.
 * The categories currently supported are 'Animals' and 'Geography'.
 * The activity also provides an options menu with shortcuts to other features or activities within the application.
 * </p>
 */
public class TriviaActivity extends AppCompatActivity {

    /** List of questions retrieved for the trivia game. */
    ArrayList<QuestionObj> getQuestions;

    /** Static reference to the quiz ViewModel. */
    static QuizActivityViewModel quizModel;

    /** Adapter to display scores. */

    private ScoreAdapter scoreAdapter;
    /** Preferences to store user's data like number of questions they chose in the past. */
    SharedPreferences sharedPreferences;
    /** Data Access Object for the quiz questions. */
    public static QuizQuestionDAO quizQuestionDAO;

    /** List to store the scores of the quiz. */
    private List<Score> scores;
    /** Reference to the trivia database. */
    QuestionDatabase db;
    /** Editor for shared preferences to modify stored values. */

    SharedPreferences.Editor editor;
    /** Binding instance for the activity's layout. */
    protected ActivityTriviaBinding variableBinding;
    /** EditText field to input the user's name. */
    private EditText userNameEditText;
    /** ArrayList to store the questions for the quiz. */
    private ArrayList<QuestionObj> questions = new ArrayList<>();

    /** Static list to store the scores. */
    static ArrayList<Score> scoreArrayList;

    /** Static reference to the adapter for the RecyclerView. */
    private static RecyclerView.Adapter myAdapter;

    /**
     * Initializes the activity, sets up UI components, retrieves stored preferences, and configures button click events.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        setSupportActionBar(variableBinding.myToolbar);
        FragmentScoresBinding binding = FragmentScoresBinding.inflate(getLayoutInflater());
        ScoreItemBinding variableBindings = ScoreItemBinding.inflate(getLayoutInflater());
       ActivityLeaderBoardBinding binding1 = ActivityLeaderBoardBinding.inflate(getLayoutInflater());

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
                runOnUiThread(() -> binding1.recycleView.setAdapter(myAdapter));
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
                        String animalQuestions = "Number of animal questions generated: ";
                        Toast.makeText(TriviaActivity.this, animalQuestions + numQuestions, Toast.LENGTH_SHORT).show();
                        String.format(getResources().getString(R.string.animalQuestions));
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
                        String geographyQuestions = "Number of geography questions generated: ";
                        Toast.makeText(TriviaActivity.this, geographyQuestions + numQuestions, Toast.LENGTH_SHORT).show();
                        String.format(getResources().getString(R.string.geographyQuestions));
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

    }


    /**
     * Inflates the options menu for the activity.
     *
     * @param menu The menu into which the items are to be placed.
     * @return true if the menu should be displayed; false otherwise.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Handles the selection of an item in the options menu.
     *
     * @param item The selected menu item.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
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
            String welcomeMessage = "Welcome to the trivia game! Select a type of question for the trivia and pick a number from 1-50 questions. Tap on the different icons on the top right to navigate to the apps shown.";
            builder.setMessage(welcomeMessage);
            String.format(getResources().getString(R.string.welcomeMessage));


            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return false;


    }

    /**
     * An adapter class for displaying a list of questions within a RecyclerView.
     */
    static class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {


        private ArrayList<QuestionObj> questions;

        /**
         * Constructor for QuestionAdapter.
         * @param questions List of QuestionObj to display.
         */
        public QuestionAdapter(ArrayList<QuestionObj> questions) {
            this.questions = questions;
        }


        /**
         *  Called when RecyclerView needs a new {@link QuestionViewHolder} of the given type
         *  to represent an item. This new ViewHolder should be constructed with a new View
         *  that can represent the items of the given type.
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return
         */
        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
            return new QuestionViewHolder(itemView);
        }

        /**
         *  Called by RecyclerView to display the data at the specified position. This method
         *  should update the contents of the {@link QuestionViewHolder#itemView} to reflect
         *  the item at the given position.
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            QuestionObj question = questions.get(position);
            Log.d("QuizActivity", "Question at position " + position + ": " + question);
            holder.bindQuestion(question);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter's data set.
         */
        @Override
        public int getItemCount() {
            return questions.size();
        }

        /**
         * A ViewHolder class that represents the UI for each item in the list of questions.
         */
        class QuestionViewHolder extends RecyclerView.ViewHolder {


            /**
             * TextView that displays the question to the user.
             */
            private TextView questionTextView;

            /**
             * RadioGroup that contains radio buttons for possible answers.
             * One of the options should be the correct answer for the question.
             */
            private RadioGroup correctAnswerRadioGroup;

            /**
             * TextView that displays incorrect answers to the user.
             *
             */
            private TextView incorrectAnswersTextView;


            /**
             * Constructor for the {@link QuestionViewHolder} class.
             * Initializes UI components related to the question, possible answers, and incorrect answers.
             *  </p>
             * @param itemView  The view instance representing a single question item in the RecyclerView.
             */
            public QuestionViewHolder(@NonNull View itemView) {
                super(itemView);
                questionTextView = itemView.findViewById(R.id.questionTextView);
                correctAnswerRadioGroup = itemView.findViewById(R.id.answersRadioGroup);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton1);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton2);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton3);
                incorrectAnswersTextView = itemView.findViewById(R.id.answerRadioButton4);
            }

            /**
             *  Bind a QuestionObj to the UI elements within the ViewHolder.
             * @param question The QuestionObj instance to bind.
             */
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

    /**
     * A custom adapter class to bind {@link Score} data to a RecyclerView for displaying player scores.
     * The adapter handles the creation of view items, binding data to these items, and managing
     * interactions such as item clicks and deletions.
     *
     * <p>
     * The adapter uses {@link ScoreViewHolder} inner class for representing and managing each individual
     * item in the RecyclerView.
     * </p>
     */

    public static class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

        /**
         * List of scores to be displayed in the RecyclerView.
         */
        public List<Score> scores;

        /**
         * Context in which the adapter is operating. Useful for accessing resources, layouts, and other UI elements.
         */
        private Context context;

        /**
         * Constructs a new ScoreAdapter.
         *
         * @param context the context in which the adapter operates.
         * @param scores the list of scores to be displayed. If null, initializes an empty list.
         */
        public ScoreAdapter(Context context, List<Score> scores) {
            this.context = context;
            this.scores = (scores != null) ? scores : new ArrayList<>();
        }


        /**
         *  Creates a new ViewHolder for the RecyclerView to represent an item.
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return
         */
        @NonNull
        @Override
        public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
            return new ScoreViewHolder(itemView);
        }

        /**
         *  Binds the data from a specific position in the dataset to a ViewHolder.
         *  Sets up item click listeners and delete button listeners.
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull ScoreViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAbsoluteAdapterPosition();
                    Score selectedScore = scoreArrayList.get(position);

                    // pass the selected score to your ViewModel
                    quizModel.selectedScore.setValue((List<Score>) selectedScore);

                }
            });



            Score currentScore = scores.get(position);
            holder.playerNameTextView.setText(currentScore.getPlayerName());
            holder.playerScoreTextView.setText(String.valueOf(currentScore.getPlayerScore()));
            holder.categoryTextView.setText(currentScore.getCategory());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Handles the click event on the score item view. When triggered, it presents an AlertDialog
                 * to the user asking for confirmation to delete the associated score.
                 *
                 * @param v The view that was clicked.
                 */
                @Override

                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    String deletionConfirmation = "Do you want to delete the score:";
                    builder.setTitle("Confirmation")
                            .setMessage(deletionConfirmation + currentScore.getPlayerScore() + "?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                /**
                                 * Handles the click event for the negative button ("No") of the AlertDialog.
                                 * This will simply dismiss the dialog without performing any actions.
                                 *
                                 * @param dialog The dialog interface where this click event originated.
                                 * @param which  The button that was clicked, as represented by its identifier.
                                 */
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                /**
                                 * Handles the click event for the confirmation dialog to delete a score.
                                 * On confirmation, the selected score is removed from the dataset, the associated database record is deleted,
                                 * and the UI is updated. Additionally, an "Undo" snackbar is displayed, allowing the user to revert the delete operation.
                                 *
                                 * @param dialog The dialog interface where this click event originated.
                                 * @param which  The button that was clicked, as represented by its identifier.
                                 */
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


        /**
         * @return The total number of items in the dataset represented by this adapter.
         */
        @Override
        public int getItemCount() {
            return scores.size();
        }


        /**
         * ViewHolder class for holding and managing UI elements of each score item in the RecyclerView.
         */
        public class ScoreViewHolder extends RecyclerView.ViewHolder {
            /**
             * The context in which the ViewHolder operates.
             */
            private Context context;

            /**
             * TextView displaying the player's name.
             */
            private TextView playerNameTextView;

            /**
             * TextView displaying the player's score.
             */
            private TextView playerScoreTextView;

            /**
             * TextView displaying the category of the score.
             */
            private TextView categoryTextView;

            /**
             * Button to trigger the delete operation for a specific score.
             */
            private TextView deleteButton;

            /**
             * Constructs a new ScoreViewHolder.
             *
             * @param itemView The view that represents an item in the RecyclerView.
             */
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
                    /**
                     * Posts a new task to run on the main application thread. This task will
                     * notify the adapter that an item has been inserted at a specific position.
                     * This is typically used to update the RecyclerView to reflect the changes in the dataset.
                     */
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /**
                         * Overrides the run method of the Runnable interface.
                         * When executed, it notifies the associated adapter that an item has been inserted
                         * at the specified position. This is typically used to update the RecyclerView
                         * to reflect the changes in the dataset.
                         */
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

