/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.DialogBoxes;

/**
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik.
 */
public class FileLoader {
    private List<String> RLEdata;
    private StringBuilder sb;
    private boolean[][] board;

    public FileLoader() {
        RLEdata = new ArrayList<>();
        sb = new StringBuilder();
    }

    public boolean readGameBoardFromDisk(File file) throws IOException {
        long startTime = System.currentTimeMillis();
        board = new boolean[1715][1648];
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

            while ((line = reader.readLine()) != null) {
                Pattern RLEpattern = Pattern.compile("([0-9]+[bBoO])|([bBoO])");
                Pattern p = Pattern.compile("([0-9]+)([oObB])");
                // Matcher RLEMatcher = RLEpattern.matcher(line);

                for (int row = 0; row < line.length(); row++) {
                    Matcher RLEMatcher = RLEpattern.matcher(String.valueOf(line.charAt(row)));
                    System.out.println(String.valueOf(line.charAt(row)));
                    int column = 0;

                    while (RLEMatcher.find()) {
                        if (RLEMatcher.group(2) != null) {
                            if (RLEMatcher.group(2).matches("[oO]")) {
                                board[row][column] = true;
                            }
                            column++;
                        } else if (RLEMatcher.group(1) != null) {
                            // Flyttet ut denne så sparer man tusenvis av cpu cycles
                            Matcher m = p.matcher(RLEMatcher.group(1));

                            m.find();
                            int number = Integer.parseInt(m.group(1));

                            while (number-- != 0) {
                                if (m.group(2).matches("[oO]")) {
                                    board[row][column] = true;
                                }
                                column++;
                            }
                        }
                    } // End of first while-loop
                } // End of for-loop
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tid i ms: " + elapsedTime);
        return true;
    }


    
    public boolean readGameBoardFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line = null;
                while((line = reader.readLine()) != null) {

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
     * 
     * 
     * @return 
     */
    public List<String> getRLEdata() {
        return RLEdata;
    }
    public String getRLEString() {
        return sb.toString();
    }
    public boolean[][] getBoard() {
        return this.board;
    }
}
