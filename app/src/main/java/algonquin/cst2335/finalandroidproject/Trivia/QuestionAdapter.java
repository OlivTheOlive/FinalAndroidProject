package algonquin.cst2335.finalandroidproject.Trivia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

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

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

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
