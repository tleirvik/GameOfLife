package model.filemanagement.otherformats;

import model.filemanagement.otherformats.data.GIFData;
import lieng.GIFWriter;

import java.io.IOException;
import model.gameoflife.GameOfLife;

/**
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
     * @param gifData The container for the image data
     */
    public GIFSaver(GIFData gifData) {
        super(gifData);
        iterations = gifData.getIterations();
        animationTimer = gifData.getAnimationTimer();
    }
    
    /**
     *  This method saves the image to file
     *
     * @return true if the image is saved, otherwise false
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
     * @param writer The {@link GIFWriter} to use
     * @param game The {@link GameOfLife}
     * @param counter The number of iterations
     * @throws IOException If an unspecified I/O error occures
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

    /**
     * This method calculates the size of an element based on the given parameters
     *
     * @param availableHeight The available height for the collection of elements
     * @param availableWidth The available width for the collection of elements
     * @param rows The given rows
     * @param columns The given columns
     * @return The minimum size for each element required to show all elements
     */
    private double calculateSize(double availableHeight, double availableWidth,
            int rows, int columns) {
        double sizeHeight = availableHeight / rows;
        double sizeWidth = availableWidth / columns;
        return Math.min(sizeWidth, sizeHeight);
    }
}
