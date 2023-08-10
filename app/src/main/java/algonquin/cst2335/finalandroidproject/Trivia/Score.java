package algonquin.cst2335.finalandroidproject.Trivia;

import android.widget.TextView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Represents a player's score in a trivia game.
 * <p>
 * This entity contains details about a player's performance in a trivia game, including
 * their name, score, and the category they played in.
 * </p>
 */
@Entity(tableName = "Score")
public class Score implements Serializable {


    /**
     * Represents a player's score in a trivia game.
     * <p>
     * This entity contains details about a player's performance in a trivia game, including
     * their name, score, and the category they played in.
     * </p>
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    /** Name of the player. */
    @ColumnInfo(name="player_name")
    private String playerName;
    /** Player's score. */
    @ColumnInfo(name="player_score")
    private int playerScore;

    /** Category of the trivia. */
    @ColumnInfo(name="category")
    private String category;

    /** Default constructor. */
    public Score() {

    }

    /**
     *  Parameterized constructor for creating a new Score.
     * @param id Unique identifier for the score entry.
     * @param playerName  Name of the player.
     * @param playerScore Player's score.
     * @param category  Category of the trivia.
     */
    public Score(int id, String playerName, int playerScore, String category) {
        this.id = id;
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.category = category;
    }

    /**
     * Gets the ID of the score.
     * @return Unique identifier for the score entry.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the score.
     * @param id Unique identifier for the score entry.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the player's name.
     * @return Name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player's name.
     * @param playerName Name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the player's score.
     * @return Player's score.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Sets the player's score.
     * @param score Player's score.
     */
    public void setPlayerScore(int score) {
        this.playerScore = score;
    }

    /**
     * Gets the category of the trivia.
     * @return Category of the trivia.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the trivia.
     * @param category Category of the trivia.
     */
    public void setCategory(String category) {
        this.category = category;
    }


}
