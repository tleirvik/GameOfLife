/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement.Decoders;

import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Robin
 */
public abstract class Decoder {
    protected MetaData metadata;
    protected byte[][] board;
    protected final BufferedReader reader;
    
    public Decoder(BufferedReader reader) {
        this.reader = reader;
    }
    
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
     *
     * @return metadata A MetaData object that contains relevant metadata about the board
     */
    public MetaData getMetadata() {
        return metadata;
    }
}
