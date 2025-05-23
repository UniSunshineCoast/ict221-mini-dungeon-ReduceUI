import dungeon.engine.HighScoreEntry;
import dungeon.engine.ScoreManager;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScoreManager {

    @Test
    void TestAddScore() {
        ScoreManager scoreManager = new ScoreManager();
        scoreManager.addScore(5);
        scoreManager.addScore(10);

        List<HighScoreEntry> scores = scoreManager.getHighScores();
        assertEquals(2, scores.size());

        assertEquals(10, scores.get(0).getScore());
        assertEquals(5, scores.get(1).getScore());
    }

    @Test
    void TestAddScore_MaxScore() {
        ScoreManager scoreManager = new ScoreManager();
        scoreManager.addScore(1);
        scoreManager.addScore(2);
        scoreManager.addScore(3);
        scoreManager.addScore(4);
        scoreManager.addScore(5);
        scoreManager.addScore(6);
        scoreManager.addScore(4);
        scoreManager.addScore(1);

        List<HighScoreEntry> scores = scoreManager.getHighScores();

        assertEquals(5, scores.size());
        assertEquals(6, scores.get(0).getScore());
        assertEquals(5, scores.get(1).getScore());
        assertEquals(4, scores.get(2).getScore());
        assertEquals(4, scores.get(3).getScore());
        assertEquals(3, scores.get(4).getScore());

        //scoreManager.displayHighScores();
    }

    @Test
    void TestAddScore_Date() throws IOException {
        ScoreManager scoreManager = new ScoreManager();

        scoreManager.addScore(5);
        scoreManager.addScore(10);
        scoreManager.addScore(5);

        List<HighScoreEntry> scores = scoreManager.getHighScores();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(sdf.format(new Date()), sdf.format(scores.get(0).getDate()));
        assertEquals(sdf.format(new Date()), sdf.format(scores.get(1).getDate()));
        assertEquals(sdf.format(new Date()), sdf.format(scores.get(2).getDate()));

        scoreManager.saveHighScores();
        //scoreManager.displayHighScores();
    }

    @Test
    void TestLoadHighScores() {
        ScoreManager scoreManager = new ScoreManager();
    }
}
