/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import GameOfLife.GameOfLife;
import lieng.GIFWriter;
import util.DialogBoxes;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik
 * 
 * This class takes a pattern from a GameOfLife object and save this pattern
 * as an animated sequence in a gif file.
 * @see lieng.GIFWriter
 */
public class GIFSaver {
    private final GameOfLife game;
    private Color aliveCellColor;
    private Color deadCellColor;
    private File file;
    private int iterations;
    private int animationTimer;
    private int height;
    private int width;
    private int cellSize;


    
    /**
     * 
     */
    public void saveToGif() {
        calculateCellSize();
        try {
            GIFWriter writer = new GIFWriter(width + 1, height + 1, 
                    file.getAbsolutePath(), animationTimer);
            writeGoLSequenceToGIF(writer, game, iterations);
            game.resetGame();
            writer.close();
            
        } catch (IOException e) {
            DialogBoxes.infoBox("Error", "Could not save as .gif", 
                    "Please try again");
        }
    }
    
    /**
     * This method calculate the size of a cell.
     */
    private void calculateCellSize() {
        int cellWidth = width / game.getColumns();
        int cellHeight = height / game.getRows();

        if(cellWidth < cellHeight) {
            cellSize = cellWidth;
        } else {
            cellSize = cellHeight;
        }
    }
    
    /**
     * This method writes the current sequence to an gif image.
     * 
     * @param writer
     * @param game
     * @param counter
     * @throws IOException 
     */
    private void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, 
        int counter) throws IOException {
        
        writer.setBackgroundColor(deadCellColor);
        
        if (counter != 0) {         
            writer.createNextImage();
            int x1 = 0;
            int x2 = cellSize;
            int y1 = 0;
            int y2 = cellSize;

            for (int row = 0; row < game.getRows() -1; row++) {
                for (int col = 0; col < game.getColumns() -1; col++) {
                    if (game.getCellAliveState(row, col) == 1) {
                        writer.fillRect(x1, x2, y1, y2, 
                                aliveCellColor);
                    }
                    x1 += cellSize;
                    x2 += cellSize;
                }
                x1 = 0; // Reset X-verdien for neste rad
                x2 = cellSize;
                y1 += cellSize; // Plusser på for neste rad
                y2 += cellSize;
            }
            writer.insertCurrentImage();
            game.update();
            writeGoLSequenceToGIF(writer, game, --counter);
        }
    }
    
    /**
     * This method set the speed of the animation.
     * 
     * @param animationTimer 
     */
    public void setAnimationTimer(int animationTimer) {
        this.animationTimer = animationTimer;
    }

    /**
     * Sets the height of the gif image.
     * 
     * @param height 
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the width of th gif image.
     * 
     * @param width 
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Sets the file to wich the pattern sequence will be written.
     * 
     * @param file 
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * Sets the the current game.
     * 
     * @param game 
     */
    public GIFSaver(GameOfLife game) {
        this.game = game;
    }

    /**
     * Sets the colors alive cells and dead cells to be used in the gif image.
     * 
     * @param aliveCellColor
     * @param deadCellColor 
     */
    public void setColors(Color aliveCellColor, Color deadCellColor) {
        this.aliveCellColor = aliveCellColor;
        this.deadCellColor = deadCellColor;
        System.out.println("aliveCellColot -> " + deadCellColor);
    }

    /**
     * Sets the number of iterations.
     * 
     * @param i 
     */
    public void setIterations(int i) {
        iterations = i;
    }
}
