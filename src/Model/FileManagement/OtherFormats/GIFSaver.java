/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats;

import Model.FileManagement.OtherFormats.Data.GIFData;
import Model.GameOfLife.GameOfLife;
import lieng.GIFWriter;
import Model.util.DialogBoxes;

import java.io.IOException;

/**
 *
 * @author Stian Reistad Røgeberg, Robin Sean Aron David Lundh, Terje Leirvik
 * 
 * This class takes a pattern from a Model.GameOfLife object and save this pattern
 * as an animated sequence in a gif file.
 * @see lieng.GIFWriter
 */
public class GIFSaver extends ImageSaver {
    private final int iterations;
    private final int animationTimer;
    private int cellSize; // shadowing ImageSaver cell size to account for
                          // the increase in board size during the nextGeneration
                          // method.

    /**
     * Sets the the current game.
     * 
     * @param gifData 
     */
    public GIFSaver(GIFData gifData) {
        super(gifData);
        iterations = gifData.getIterations();
        animationTimer = gifData.getAnimationTimer();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public boolean saveImage() {
        try {
            GIFWriter writer = new GIFWriter(width + 1, height + 1, 
                    file.getAbsolutePath(), animationTimer);
            writeGoLSequenceToGIF(writer, game, iterations);
            game.resetGame();
            writer.close();
            
        } catch (IOException e) {
            return false;
        }
        return true;
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
            cellSize = (int) calculateSize(height, width, game.getRows(), game.getColumns());
            writer.createNextImage();
            int x1 = 0;
            int x2 = cellSize;
            int y1 = 0;
            int y2 = cellSize;

            for (int row = 0; row < game.getRows() - 1; row++) {
                for (int col = 0; col < game.getColumns() - 1; col++) {
                    if (game.getCellAliveState(row, col) == 1) {
                        writer.fillRect(x1, x2, y1, y2, 
                                aliveCellColor);
                    }
                    x1 += cellSize;
                    x2 += cellSize;
                }
                x1 = 0;
                x2 = cellSize;
                y1 += cellSize;
                y2 += cellSize;
            }
            writer.insertCurrentImage();
            game.update();
            writeGoLSequenceToGIF(writer, game, --counter);
        }
    }

    private double calculateSize(double availibleHeight, double availibleWidth,
            int rows, int columns) {
        double sizeHeight = availibleHeight / rows;
        double sizeWidth = availibleWidth / columns;
        return Math.min(sizeWidth, sizeHeight);
    }
}
