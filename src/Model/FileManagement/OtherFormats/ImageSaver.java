/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats;

import Model.FileManagement.OtherFormats.Data.ImageData;
import Model.GameOfLife.GameOfLife;
import java.awt.Color;
import java.io.File;

/**
 *
 * @author Robin
 */
public abstract class ImageSaver {
    protected final GameOfLife game;
    protected final Color aliveCellColor;
    protected final Color deadCellColor;
    protected final File file;
    protected final int height;
    protected final int width;
    protected final int cellSize;
    
    public ImageSaver(ImageData data) {
        game = data.getGame();
        aliveCellColor = data.getAliveCellColor();
        deadCellColor = data.getDeadCellColor();
        file = data.getFile();
        height = data.getHeight();
        width = data.getWidth();
        cellSize = data.getCellSize();
    }
    
    public abstract boolean saveImage();
}
