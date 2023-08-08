package algonquin.cst2335.finalandroidproject.Trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Score.class}, version = 2)
public abstract class QuestionDatabase extends RoomDatabase {


    public abstract QuizQuestionDAO qqDAO();


}


