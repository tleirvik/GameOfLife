package Model.FileManagement.OtherFormats;

import Model.FileManagement.OtherFormats.Data.ImageData;
import Model.GameOfLife.GameOfLife;
import java.awt.Color;
import java.io.File;

/**
 * The abstract class for all image savers
 */
public abstract class ImageSaver {
    protected final GameOfLife game;
    protected final Color aliveCellColor;
    protected final Color deadCellColor;
    protected final File file;
    protected final int height;
    protected final int width;
    protected final int cellSize;

    /**
     * Constructor for the {@link ImageSaver} class
     * @param data
     */
    public ImageSaver(ImageData data) {
        game = data.getGame();
        aliveCellColor = data.getAliveCellColor();
        deadCellColor = data.getDeadCellColor();
        file = data.getFile();
        height = data.getHeight();
        width = data.getWidth();
        cellSize = data.getCellSize();
    }

    /**
     * Method that save the image
     * @return true if the image is saved, otherwise false
     */
    public abstract boolean saveImage();
}
