package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;

/**
 * Data Access Object (DAO) interface for the Score entity.
 * <p>
 * This interface provides methods to interact with the database to perform CRUD operations on the Score entity.
 * </p>
 */
@Dao
public interface QuizQuestionDAO {
    /**
     * Inserts a new score record into the database.
     *
     * @param score The score entity to be inserted.
     */
    @Insert()
    void insert(Score score);

    /**
     * Retrieves the top 10 scores from the database, ordered in descending order.
     * @return List of top 10 scores.
     */
    @Query("SELECT * FROM Score  ORDER BY player_score DESC LIMIT 10")
     List<Score> getTopScores();

    /**
     * Deletes a specific score from the database based on its unique ID.
     * @param id The unique identifier of the score to be deleted.
     */
    @Query("DELETE FROM Score WHERE id = :id")
    void deleteScore(int id);

    @Query("DELETE FROM Score WHERE player_score < :minScore")
    void deleteScoresBelowValue(int minScore);

    
}
