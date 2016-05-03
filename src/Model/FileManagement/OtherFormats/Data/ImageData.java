/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats.Data;

import Model.GameOfLife.GameOfLife;
import java.awt.Color;
import java.io.File;

/**
 *
 * @author Robin
 */
public class ImageData {
    private final GameOfLife game;
    private final Color aliveCellColor;
    private final Color deadCellColor;
    private final File file;
    private final int height;
    private final int width;
    private final int cellSize;
    
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
     * @return the game
     */
    public GameOfLife getGame() {
        return game;
    }

    /**
     * @return the aliveCellColor
     */
    public Color getAliveCellColor() {
        return aliveCellColor;
    }

    /**
     * @return the deadCellColor
     */
    public Color getDeadCellColor() {
        return deadCellColor;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the cellSize
     */
    public int getCellSize() {
        return cellSize;
    }
    
    
}
