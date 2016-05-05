package model.filemanagement.otherformats.data;

import java.awt.Color;
import java.io.File;
import model.gameoflife.GameOfLife;

/**
 * This is the class for handling GIF data
 */
public class GIFData extends ImageData {
    private final int iterations;
    private final int animationTimer;

    /**
     * Consutructs a {@link GIFData} object with the specified parameters
     *
     * @param game The {@link GameOfLife} to use
     * @param aliveCellColor The {@link javafx.scene.paint.Color} of the alive cells
     * @param deadCellColor The {@link javafx.scene.paint.Color} of the dead cells
     * @param file The {@link File}to save to
     * @param height The height of the image
     * @param width The width of the image
     * @param cellSize The size of the cell
     * @param iterations The number of iterations
     * @param animationTimer The time between the iterations
     */
    public GIFData(GameOfLife game, Color aliveCellColor, Color deadCellColor, 
            File file, int height, int width, int cellSize, int iterations, int animationTimer) {
        super(game, aliveCellColor, deadCellColor, file, height, width, cellSize);
        this.iterations = iterations;
        this.animationTimer = animationTimer;
    }
    
    /**
     * Returns the number of iterations
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Returns the animtation timer
     * @return the animation timer
     */
    public int getAnimationTimer() {
        return animationTimer;
    }
}
