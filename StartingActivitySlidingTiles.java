package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivitySlidingTiles extends AppCompatActivity {

    public static final String name = "SlidingTiles";
    /**
     * The main save file.
     */
    public static String SAVE_FILENAME_SLIDING_TILES = LoginActivity.usersManager.getCurrentUser() + "SlidingTiles.ser";

    /**
     * A temporary save file.
     */
    public static String TEMP_SAVE_FILENAME = LoginActivity.usersManager.getCurrentUser() + "SlidingTiles_temp.ser";
    /**
     * The board manager.
     */
    protected BoardManagerSlidingTiles boardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveToFile(TEMP_SAVE_FILENAME);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addScoreButtonListener();
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSettings();
                //set user score to zero
            }
        });
    }

    /**
     * Activate the score button.
     */
    private void addScoreButtonListener() {
        Button scoreButton = findViewById(R.id.ScoreButton);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreboard();
            }
        });
    }

    private void switchToScoreboard() {
        Intent tmp = new Intent(this, MainListView.class);
        startActivity(tmp);
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(SAVE_FILENAME_SLIDING_TILES);
                saveToFile(TEMP_SAVE_FILENAME);
                if (boardManager != null) {
                    makeToastLoadedText();
                    switchToGame();
                } else {
                    makeNoLoadedGameToast();
                }
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME_SLIDING_TILES);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
            });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivitySlidingTiles view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivitySlidingTiles.class);
        saveToFile(StartingActivitySlidingTiles.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }

    /**
     * Switches to the settings select activity.
     */
    private void switchToSettings() {
        Intent tmp = new Intent(this, SlidingTilesSettingsActivity.class);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        System.out.println("Loading from " + fileName);
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManagerSlidingTiles) input.readObject();
                if (boardManager != null) {
                    setBoardSize(boardManager.getBoard().tiles.length);
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        System.out.println("Saving to " + fileName);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Set the size of the board for applicable games.
     * @param size int
     */
    public void setBoardSize(int size) {
        BoardSlidingTiles.NUM_ROWS = size;
        BoardSlidingTiles.NUM_COLS = size;
    }

    /**
     * Indicate to user that there is no game to load.
     */
    public void makeNoLoadedGameToast() {
        Toast.makeText(this, "There is no game to load!", Toast.LENGTH_SHORT).show();
    }
}
