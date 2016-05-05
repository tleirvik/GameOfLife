package model.filemanagement.decoders;

import model.gameoflife.MetaData;
import model.gameoflife.PatternFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link Life106Decoder} class
 */
public class Life106Decoder extends Decoder {
    private String line;

    /**
     * Constructs a {@link Life106Decoder} object with the specified parameters
     * @param reader The {@link BufferedReader} to open
     */
    public Life106Decoder(BufferedReader reader) {
        super(reader);
    }

    /**
     * Decodes the file using {@link #parseMetadata()} and {@link #parseBoard(BufferedReader)}
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    @Override
    public void decode() throws PatternFormatException, IOException {
        parseMetadata();
        parseBoard(reader);
    }

    /**
     * Parses the meta data from the file
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    private void parseMetadata() throws PatternFormatException, IOException {
        metadata = new MetaData();
        
        StringBuilder comment = new StringBuilder();
        comment.append("#Life 1.06").append("\n");
        
        metadata.setComment(comment.toString());
    }

    /**
     * This method interprets the file and translates it into a game board
     * @param reader The {@link BufferedReader} to use
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    private void parseBoard(BufferedReader reader) throws PatternFormatException, IOException {
        ArrayList<ArrayList<Integer>> aliveCellPositions = new ArrayList<>();
        
        int lowestXNumber = 0;
        int lowestYNumber = 0;
        int highestXNumber = 0;
        int highestYNumber = 0;
        
        int row = 0;
        
        Pattern positionXY = Pattern.compile("([-+]?[\\d]+) ([-+]?[\\d]+)");
        while ((line = reader.readLine()) != null) {
            aliveCellPositions.add(new ArrayList<>());
            Matcher positionXYmatcher = positionXY.matcher(line);
            positionXYmatcher.find();
            
            int x = Integer.parseInt(positionXYmatcher.group(1));
            int y = Integer.parseInt(positionXYmatcher.group(2));
            aliveCellPositions.get(row).add(x);
            aliveCellPositions.get(row).add(y);
            
            if (y < lowestYNumber) {
                lowestYNumber = y;
            }
            
            if (x < lowestXNumber) {
                lowestXNumber = x;
            }
            
            if (x > highestXNumber) {
                highestXNumber = x;
            }
            
            if (y > highestYNumber) {
                highestYNumber = y;
            }
            
            row++;
        }
                
        if (lowestXNumber < 0 || lowestYNumber < 0) {
            int numberToIncreaseXBy = lowestXNumber * -1;
            int numberToIncreaseYBy = lowestYNumber * -1;
        
            for (int i = 0; i < aliveCellPositions.size(); i++) {
                int x = aliveCellPositions.get(i).get(0);
                int y = aliveCellPositions.get(i).get(1);
                aliveCellPositions.get(i).set(0, x + numberToIncreaseXBy);
                aliveCellPositions.get(i).set(1, y + numberToIncreaseYBy);
            }
        }
        
        int boardYSize = lowestYNumber*-1 + highestYNumber + 1;
        int boardXSize = lowestXNumber*-1 + highestXNumber + 1;
        
        //Husk, raddominante arrays brukes her (y gaar opp i sky :) )
        board = new byte[boardYSize][boardXSize];
        for (int i = 0; i < aliveCellPositions.size(); i++) {
            int x = aliveCellPositions.get(i).get(0);
            int y = aliveCellPositions.get(i).get(1);
            board[y][x] = 1;
        }
    }
}
