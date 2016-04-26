package FileManagement.Decoders;

import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Life106Decoder extends Decoder {
    private String line;
    
    public Life106Decoder(BufferedReader reader) {
        super(reader);
    }

    public void decode() throws PatternFormatException, IOException {
        System.out.println("DET SKJER!!!!!");
        parseMetadata();
        parseBoard(reader);
    }

    private void parseMetadata() throws PatternFormatException, IOException {
        metadata = new MetaData();
        
        StringBuilder comment = new StringBuilder();
        comment.append("#Life 1.06").append("\n");
        
        metadata.setComment(comment.toString());
    }

    private void parseBoard(BufferedReader reader) throws PatternFormatException, IOException {
        ArrayList<ArrayList<Integer>> aliveCellPositions = new ArrayList<>();
        
        int lowestXNumber = 0; //Kan være et negativt tall. Skal omgjøres til 0
        int lowestYNumber = 0; //Kan være et negativt tall. Skal omgjøres til 0
        int highestXNumber = 0; //Skal være grensen til hvor stort Arrayet skal være
        int highestYNumber = 0; //Skal være grensen til hvor stort Arrayet skal være
        
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
            
            //Finn den laveste Y-posisjonen
            if(y < lowestYNumber) {
                lowestYNumber = y;
            }
            
            //Finn den laveste X-posisjonen
            if(x < lowestXNumber) {
                lowestXNumber = x;
            }
            
            //Finn den høyeste X-posisjonen
            if(x > highestXNumber) {
                highestXNumber = x;
            }
            
            //Finn den høyeste Y-posisjonen
            if(y > highestYNumber) {
                highestYNumber = y;
            }
            
            row++;
        }
                
        //Finn nummeret å legge til om den laveste posisjonen er negativ
        //(Slik at den laveste blir 0 igjen)
        if(lowestXNumber < 0 || lowestYNumber < 0) {
            int numberToIncreaseXBy = lowestXNumber * -1;
            int numberToIncreaseYBy = lowestYNumber * -1;
        
            for (int i = 0; i < aliveCellPositions.size(); i++) {
                int x = aliveCellPositions.get(i).get(0);
                int y = aliveCellPositions.get(i).get(1);
                aliveCellPositions.get(i).set(0, x + numberToIncreaseXBy);
                aliveCellPositions.get(i).set(1, y + numberToIncreaseYBy);
            }
        }
        
        //Finner differansen av den laveste og høyeste posisjonen
        int boardYSize = lowestYNumber*-1 + highestYNumber + 1;
        int boardXSize = lowestXNumber*-1 + highestXNumber + 1;
        
        //Husk, raddominante arrays brukes her (y går opp i sky :) )
        board = new byte[boardYSize][boardXSize];
        for (int i = 0; i < aliveCellPositions.size(); i++) {
            int x = aliveCellPositions.get(i).get(0);
            int y = aliveCellPositions.get(i).get(1);
            board[y][x] = 1;
        }
    }
}
