package algonquin.cst2335.finalandroidproject.Trivia;

import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Score")
public class Score implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="player_name")
    private String playerName;
    @ColumnInfo(name="player_score")
    private int playerScore;
    @ColumnInfo(name="category")
    private String category;


    public Score() {

    }

    public Score(int id, String playerName, int playerScore, String category) {
        this.id = id;
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int score) {
        this.playerScore = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
