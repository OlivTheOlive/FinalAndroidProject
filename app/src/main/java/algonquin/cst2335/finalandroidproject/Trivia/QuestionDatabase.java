package algonquin.cst2335.finalandroidproject.Trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Represents the SQLite database for the trivia application.
 * <p>
 * This class acts as the main database access point. It provides a method to access the
 * {@link QuizQuestionDAO} which offers CRUD operations for the {@link Score} entity.
 * </p>
 */
@Database(entities = {Score.class}, version = 2)
public abstract class QuestionDatabase extends RoomDatabase {


    /**
     * Retrieves the DAO (Data Access Object) for quiz questions.
     * @return Instance of {@link QuizQuestionDAO} to interact with the {@link Score} entity.
     */
    public abstract QuizQuestionDAO qqDAO();


}


