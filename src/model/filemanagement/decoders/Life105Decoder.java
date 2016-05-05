package model.filemanagement.decoders;

import model.gameoflife.MetaData;
import model.gameoflife.PatternFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@link Life105Decoder} class
 */
public class Life105Decoder extends Decoder {
    private String line;
    /**
     * Constructs a {@link Life105Decoder} object with the specified parameters
     * @param reader The {@link BufferedReader} to use
     */
    public Life105Decoder(BufferedReader reader) {
        super(reader);
    }

    /**
     * Decodes the file using {@link #parseMetadata(BufferedReader)} and {@link #parseBoard(BufferedReader)}
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    @Override
    public void decode() throws PatternFormatException, IOException {
        parseMetadata(reader);
        parseBoard(reader);
    }
    /**
     * Parses the meta data from the file
     * @param reader The {@link BufferedReader} to open
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    private void parseMetadata(BufferedReader reader) 
            throws PatternFormatException, IOException {
        metadata = new MetaData();
        
        StringBuilder comment = new StringBuilder();
        comment.append("#Life 1.05").append("\n");

        while ((line = reader.readLine()) != null) {
            if (line.charAt(0) == '#') {
                final char tempChar = line.charAt(1);
                switch (tempChar) {
                    case 'D':
                        String tempString = line.replaceAll("(#D )", "");
                        comment.append(tempString).append("\n");
                        break;
                    case 'N':
                        String[] ruleString = {"23","3"};
                        metadata.setRuleString(ruleString);
                        break;
                    case 'P':
                        String tempString2 = line.replaceAll("(#)", "");
                        comment.append(tempString2).append("\n");
                        break;
                }
            } else {
                break;
            }
        }
        metadata.setComment(comment.toString());
    }
    /**
     * This method interprets the file and translates it into a game board
     * @param reader The {@link BufferedReader} to use
     * @throws PatternFormatException If the pattern is not recognized
     * @throws IOException If an unspecified I/O error occurs
     */
    private void parseBoard(BufferedReader reader) throws PatternFormatException, IOException {
        ArrayList<ArrayList<Byte>> tempBoard = new ArrayList<>();
                
        int row = 0;
        int colSize = 0;
        
        do {
            if (line.equals("")) {
                continue;
            }
            tempBoard.add(new ArrayList<>()); //legg til en rad
            for (int i = 0; i < line.length(); i++) {
                switch (line.charAt(i)) {
                    case '*':
                        tempBoard.get(row).add((byte) 1);
                        break;
                    case '.':
                        tempBoard.get(row).add((byte) 0);
                        break;
                    default:
                        throw new PatternFormatException("Line '" + line +
                                "' on position '" + i + "' did not match character '*' or '.'!");
                }
            }
            if (tempBoard.get(row).size() > colSize) {
                colSize = tempBoard.get(row).size();
            }
            row++;
        }
        while ((line = reader.readLine()) != null);
        
        board = new byte[tempBoard.size()][colSize];
        for (row = 0; row < tempBoard.size(); row++) {
            for (int col = 0; col < tempBoard.get(row).size(); col++) {
                board[row][col] = tempBoard.get(row).get(col);
            }
        }
    }
}
