package algonquin.cst2335.finalandroidproject.Trivia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;

@Dao
public interface QuizQuestionDAO {
    @Insert()
    void insert(Score score);
    @Query("SELECT * FROM Score  ORDER BY player_score DESC LIMIT 10")
     List<Score> getTopScores();
    @Query("DELETE FROM Score WHERE id = :id")
    void deleteScore(int id);

    
}
