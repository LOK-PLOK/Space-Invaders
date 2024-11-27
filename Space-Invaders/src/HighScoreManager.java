public class HighScoreManager {
    public static int highScore = 0;

    public static int getHighScore() {
        return highScore;
    }

    public static void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }
}