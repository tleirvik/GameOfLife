package FileManagement.Encoders;

import GameOfLife.GameOfLife;
import GameOfLife.MetaData;
import util.DialogBoxes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
    protected final StringBuffer rleString;
    
    public Encoder(GameOfLife game, File f) {
        this.game = game;
        metadata = game.getMetaData();
        filePath = f.toPath();
        rleString = new StringBuffer();
    }
    
    public abstract boolean encode();

}