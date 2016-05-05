package model.filemanagement.decoders;

import model.gameoflife.MetaData;
import model.gameoflife.PatternFormatException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * The {@link Decoder} class
 */
public abstract class Decoder {
    protected MetaData metadata;
    protected byte[][] board;
    protected final BufferedReader reader;

    /**
     * Creates a {@link Decoder} class with the specified parameters
     * @param reader The {@link BufferedReader} to open
     */
    public Decoder(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * The abstract decode method that every decoder must implement
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    public abstract void decode() throws PatternFormatException, IOException;
    
    /**
     * Method that returns the board contained in this class
     * @return board Returns the boolean[][] board contained in this class
     */
    public byte[][] getBoard() {
        return board;
    }
    
    /**
     * Method that returns the associated MetaData object that we read from the RLE files
     * @return metadata A MetaData object that contains relevant metadata about the board
     */
    public MetaData getMetadata() {
        return metadata;
    }
}
