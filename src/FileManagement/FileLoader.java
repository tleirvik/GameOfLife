/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GameOfLife.MetaData;
import GameOfLife.PatternFormatException;
import util.DialogBoxes;

/**
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class FileLoader {
    private List<String> RLEdata;
    private StringBuilder sb;
    private byte[][] board;
    private MetaData metadata;
    // private BufferedReader reader;

    public FileLoader() {
        RLEdata = new ArrayList<>();
        sb = new StringBuilder();
    }


    public boolean readGameBoardFromDisk(File file) throws IOException {


        // Ta inn reader i innparam
        // Lese metadata
        // LAge brett
        // Lese gamerules
        System.out.println("Creating board and setting to false");
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory());

        long startTime = System.currentTimeMillis();
        // long startTime2 = System.currentTimeMillis();

        long stopTime2 = System.currentTimeMillis();
        long elapsedTime2 = stopTime2 - startTime;

        System.out.println("Tid i ms: " + elapsedTime2);

        if (file == null) {
            DialogBoxes.infoBox("Error!", "No such file!!", "Please try again!");
            return false;
        } else if (!file.isFile()) {
            DialogBoxes.infoBox("Error!", "Invalid file!", "Please try again!");
            return false;
        } else if (!file.canRead()) {
            DialogBoxes.infoBox("Error!", "Could not read file!",
                    "Please try again!");
        }

        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            parseMetadata(reader);


            //NÅ FINNER MAN STØRRELSE OG REGLER

            // line;

            // line.matches()

            int row = 0, col = 0;
            final char lineBreak = '$';
            while ((line = reader.readLine()) != null) { //Bør være dedikert til lesing av brett


                int charNumber = 0; // Antall forekomster av en celle
                for (int i = 0; i < line.length(); i++) {
                    if (Character.isDigit(line.charAt(i))) {
                        if (charNumber == 0) {
                            charNumber = Character.getNumericValue(line.charAt(i));
                        } else {
                            charNumber *= 10;
                            charNumber += Character.getNumericValue(line.charAt(i));
                        }
                    } else if (line.charAt(i) == 'o' || line.charAt(i) == 'O') {
                        //Iterer gjennom antall forekomster og sett celle til levende

                        if (charNumber != 0) {

                            while (charNumber != 0) {
                                board[row][col] = 1;
                                charNumber--;
                                col++;
                            }
                        } else {

                            board[row][col] = 1;
                            col++;
                        }
                    } else if (line.charAt(i) == 'b' || line.charAt(i) == 'B') {

                        if (charNumber != 0) {

                            while (charNumber != 0) {
                                charNumber--;
                                col++;
                            }
                        } else {

                            col++;
                        }
                    } else if (line.charAt(i) == lineBreak) {
                        row++;
                        col = 0;
                        charNumber = 0;


                    }
                }

            }


            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            System.out.println("Tid i ms: " + elapsedTime);
            return true;
        }

    }
    public boolean readGameBoardFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");
                }
                // System.out.println(sb.toString());
            } catch (IOException ioE) {
                DialogBoxes.infoBox("Error!", "", "");
                return false;
            }
        } catch (MalformedURLException muE) {
            DialogBoxes.infoBox("Error!", "Invalid URL!", "Please check URL " +
                    "and try again.");
            return false;
        } catch (IOException ex) {
            DialogBoxes.infoBox("Error!", "", "");
            return false;
        }
        return true;
    }

    /**
     * Reads the contents of the input file and stores it in an ArrayList for future use.
     *
     *This method is not meant to be called directly, but rather through the decode() method in
     * a RLEDecoder-object.
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */


    /**
     * @return
     */
    public List<String> getRLEdata() {
        return RLEdata;
    }

    public String getRLEString() {
        return sb.toString();
    }

    public byte[][] getBoard() {
        return this.board;
    }

    public MetaData getMetadata() {
        return metadata;
    }

    private void parseMetadata(BufferedReader reader) throws IOException {
        metadata = new MetaData();
        StringBuilder name = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        Pattern RLEpatternY = Pattern.compile("([yY][\\s][=][\\s])([\\d]+)");
        Pattern RLEpatternX = Pattern.compile("([xX][\\s][=][\\s])([\\d]+)");

        String line = null;
        while ((line = reader.readLine()) != null) { // OG INNEHOLDER # ?
            if (line.charAt(0) == '#') {

                final char tempChar = line.charAt(1);
                switch (tempChar) {
                    case 'N':
                        String tempString = line.replaceAll("(#N )", "");
                        name.append(tempString);
                        metadata.setName(name.toString());
                        // System.out.println(metadata.getName());
                        System.out.println(name.toString());
                        break;
                    case 'C':
                        String tempString2 = line.replaceAll("(#C )", "");
                        comment.append(tempString2);
                        comment.append("\n");
                        metadata.setComment(comment.toString());
                        break;
                    case 'c':
                        String tempString3 = line.replaceAll("(#c )", "");
                        comment.append(tempString3);
                        comment.append("\n");
                        metadata.setComment(comment.toString());
                        break;
                    case 'O':
                        String tempString4 = line.replaceAll("(#O )", "");
                        author.append(tempString4);
                        metadata.setAuthor(author.toString());

                }

            } else {
                boolean foundRows = false;
                boolean foundColumns = false;
                Matcher RLEmatcherY = RLEpatternY.matcher(line);
                Matcher RLEmatcherX = RLEpatternX.matcher(line);
                int rows = 0;
                int columns = 0;

                if (RLEmatcherY.find()) {
                    rows = Integer.parseInt(RLEmatcherY.group(2));
                    foundRows = true;
                }

                if (RLEmatcherX.find()) {
                    columns = Integer.parseInt(RLEmatcherX.group(2));
                    foundColumns = true;
                }

                if (foundRows && foundColumns) {
                    board = new byte[rows][columns];
                    return;
                }
                if (!foundRows && !foundColumns) {
                    //throw new PatternFormatException("X and Y values could not be parsed from RLE-file");
                } else if (!foundRows) {
                    //throw new PatternFormatException("Y values could not be parsed from RLE-file");
                } else if (!foundColumns) {
                    //throw new PatternFormatException("X values could not be parsed from RLE-file");
                }
            }
        }
    }
}
// x = 1714, y = 1647, rule = s23/b3



// If line contains rule/size do that
// Mest effektive ville være å gjøre dette
// Utenfor while loop, som burde vøre dedikert
// Til lesing av brett
// IF LINE NOT CONTAINS # OR RULE/SIZE: break;