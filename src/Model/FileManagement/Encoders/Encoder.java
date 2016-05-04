package Model.FileManagement.Encoders;

import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import java.io.File;
import java.nio.file.Path;


/**
 * This class handles the interpretation of the board and translates it to Run Length Encoding format
 *
 */
public abstract class Encoder {
    protected final GameOfLife game;
    protected final MetaData metadata;
    protected final Path filePath;
    protected final StringBuilder rleString;

    /**
     * Constructs an object with the specified parameters
     * @param game The {@link GameOfLife} object to hold the game board
     * @param f The {@link File} to save to
     */
    public Encoder(GameOfLife game, File f) {
        this.game = game;
        metadata = game.getMetaData();
        filePath = f.toPath();
        rleString = new StringBuilder();
    }

    /**
     * The encode method
     * @return true if the encoding is successful
     */
    public abstract boolean encode();
}