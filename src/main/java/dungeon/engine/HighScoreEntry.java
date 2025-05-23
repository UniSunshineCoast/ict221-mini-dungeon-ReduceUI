package dungeon.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HighScoreEntry implements Comparable<HighScoreEntry> {
    private final int score;
    private final Date date;

    public HighScoreEntry(int score, Date date) {
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(HighScoreEntry o) {
        int scoreCompared = Integer.compare(o.score, this.score);
        if (scoreCompared != 0) {
            return scoreCompared;
        }
        return this.date.compareTo(o.date);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("%02d", score) + " " + sdf.format(date);
    }

}
