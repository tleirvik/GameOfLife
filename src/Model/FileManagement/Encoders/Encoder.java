package Model.FileManagement.Encoders;

import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import java.io.File;
import java.nio.file.Path;


/**
 * This class handles the interpretation of the board and translates it to Run Length Encoding format
 * Three methods that interprets the meta data and one method that parses the byte[][] board
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public abstract class Encoder {
    protected final GameOfLife game;
    protected final MetaData metadata;
    protected final Path filePath;
    protected final StringBuilder rleString;
    
    public Encoder(GameOfLife game, File f) {
        this.game = game;
        metadata = game.getMetaData();
        filePath = f.toPath();
        rleString = new StringBuilder();
    }
    
    public abstract boolean encode();

}