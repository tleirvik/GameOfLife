package model.filemanagement.otherformats.data;

import java.awt.Color;
import java.io.File;
import model.gameoflife.GameOfLife;

/**
 * The image data class
 */
public class ImageData {
    private final GameOfLife game;
    private final Color aliveCellColor;
    private final Color deadCellColor;
    private final File file;
    private final int height;
    private final int width;
    private final int cellSize;

    /**
     * Constructs an {@link ImageData} object with the specified parameters
     * @param game The {@link GameOfLife} to use
     * @param aliveCellColor The {@link javafx.scene.paint.Color} of the alive cells
     * @param deadCellColor The {@link javafx.scene.paint.Color} of the dead cells
     * @param file The {@link File}to save to
     * @param height The height of the image
     * @param width The width of the image
     * @param cellSize The size of the cell
     */
    public ImageData(GameOfLife game, Color aliveCellColor, Color deadCellColor, 
            File file, int height, int width, int cellSize) {
        this.game = game;
        this.aliveCellColor = aliveCellColor;
        this.deadCellColor = deadCellColor;
        this.file = file;
        this.height = height;
        this.width = width;
        this.cellSize = cellSize;
    }

    /**
     * Returns the {@link GameOfLife} object
     * @return the game
     */
    public GameOfLife getGame() {
        return game;
    }

    /**
     * Return the alive cell color
     * @return the aliveCellColor
     */
    public Color getAliveCellColor() {
        return aliveCellColor;
    }

    /**
     * Returns the dead cell color
     * @return the deadCellColor
     */
    public Color getDeadCellColor() {
        return deadCellColor;
    }

    /**
     * Returns the file
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the height
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the cell size
     * @return the cellSize
     */
    public int getCellSize() {
        return cellSize;
    }
}
