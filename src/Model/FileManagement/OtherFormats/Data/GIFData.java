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
public class GIFData extends ImageData {
    private final int iterations;
    private final int animationTimer;
    
    public GIFData(GameOfLife game, Color aliveCellColor, Color deadCellColor, 
            File file, int height, int width, int cellSize, int iterations, int animationTimer) {
        super(game, aliveCellColor, deadCellColor, file, height, width, cellSize);
        this.iterations = iterations;
        this.animationTimer = animationTimer;
    }
    
    /**
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @return the animation timer
     */
    public int getAnimationTimer() {
        return animationTimer;
    }
}
