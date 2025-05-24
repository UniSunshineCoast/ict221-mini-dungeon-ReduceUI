package dungeon.engine;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;


/**
 * Loads, Manages and Saves High scores to file.
 */
public class ScoreManager {

    private static final int MAX_SCORES = 5;
    private static final String Filename = "scores.dat";
    public static List<HighScoreEntry> highScores;

    public ScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
        //displayHighScores();

    }

    public void addScore(int score) {
        HighScoreEntry newEntry = new HighScoreEntry(score, new Date());
        highScores.add(newEntry); //add congrats message for top 5
        Collections.sort(highScores);
        if (highScores.size() > MAX_SCORES) {
            highScores.removeLast();
        }
    }

    public List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    public void displayHighScores() {
        System.out.println("High Scores:");
        if (highScores.isEmpty()) {
            System.out.println("No high scores found - play a game first");
        } else {
            for (int i = 0; i < highScores.size(); i++) {
                System.out.println("#" + (i + 1) + " " + highScores.get(i).toString());
            }
        }
        System.out.println();
    }

    public void saveHighScores() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(Filename))) {
            out.writeInt(highScores.size());
            for (int i = 0; i < highScores.size(); i++) {
                out.writeUTF(highScores.get(i).toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    public void loadHighScores() {
        highScores.clear();
        try (DataInputStream in = new DataInputStream(new FileInputStream(Filename))) {
            int numScores = in.readInt();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < numScores; i++) {
                try {
                    String entryString = in.readUTF();

                    int spaceReference = entryString.indexOf(" ");
                    String scoreString = entryString.substring(0, spaceReference);
                    int score = Integer.parseInt(scoreString);

                    String dateString = entryString.substring(spaceReference+1).trim();
                    Date date = sdf.parse(dateString);

                    highScores.add(new HighScoreEntry(score, date));
                } catch (ParseException e) {
                    System.err.println("Error parsing score: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());

        }
    }
}
