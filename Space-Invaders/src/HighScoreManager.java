/**
 * Manages the high score for the game.
 * This class provides methods to get and set the high score.
 */
public class HighScoreManager {
    public static int highScore = 0;

    /**
     * Gets the current high score.
     *
     * @return the high score
     */
    public static int getHighScore() {
        return highScore;
    }

    /**
     * Sets a new high score if the provided score is higher than the current high score.
     *
     * @param score the new high score
     */
    public static void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }
}