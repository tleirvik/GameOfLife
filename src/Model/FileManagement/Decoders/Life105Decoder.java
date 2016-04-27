package Model.FileManagement.Decoders;

import Model.GameOfLife.MetaData;
import Model.GameOfLife.PatternFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Life105Decoder extends Decoder {
    private String line;
    
    public Life105Decoder(BufferedReader reader) {
        super(reader);
    }

    public void decode() throws PatternFormatException, IOException {
        parseMetadata(reader);
        parseBoard(reader);
    }

    private void parseMetadata(BufferedReader reader) throws PatternFormatException, IOException {
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

    private void parseBoard(BufferedReader reader) throws PatternFormatException, IOException {
        ArrayList<ArrayList<Byte>> tempBoard = new ArrayList<>();
                
        int row = 0;
        int colSize = 0;
        
        do {
            if(line.equals("")) {
                continue; //IGNORER TOMME LINJER TOTALT
            }
            tempBoard.add(new ArrayList<>()); //legg til en rad
            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) == '*') {
                    tempBoard.get(row).add((byte) 1);
                } else if(line.charAt(i) == '.') {
                    tempBoard.get(row).add((byte) 0);
                } else {
                    throw new PatternFormatException("Line '" + line + "' on position '" + i + "' did not match character '*' or '.'!");
                }
            }
            //Finner den største kolonnerekken og setter det til en variabel som kan brukes under
            if(tempBoard.get(row).size() > colSize) {
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
