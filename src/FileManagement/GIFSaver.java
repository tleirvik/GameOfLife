/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileManagement;

import GameOfLife.GameOfLife;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import lieng.GIFWriter;

/**
 *
 * @author stianreistadrogeberg
 */
public class GIFSaver {
    private GIFWriter writer;
    private GameOfLife game;
    private Color aliveCellColor;
    private Color deadCellColor;
    private Color backgroundColor;
    private File file;
    private int iterations;
    private int animationTimer;
    private int height;
    private int width;


    public void setAnimationTimer(int animationTimer) {
        this.animationTimer = animationTimer;
    }

    
    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public GIFSaver(GameOfLife game) {
        this.game = game;
    }
    
    public void saveToGif() {
        try {
            GIFWriter writer = new GIFWriter(width, height, 
                    file.getAbsolutePath(), animationTimer);
            writeGoLSequenceToGIF(writer, this.game, iterations);
            writer.close();
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, 
            int counter) throws IOException {

        if(counter != 0) {         
            final int stripCellSize = 20; // (int) (strip.getHeight() / (pattern.length - 2));
            int x1 = 0;
            int x2 = stripCellSize;
            int y1 = 0;
            int y2 = stripCellSize;

            for(int row = 0; row < game.getRows(); row++) {
                for(int col = 0; col < game.getColumns(); col++) {
                    if (game.getCellAliveState(row, col) == 1) {
                        writer.fillRect(x1, x2, y1, y2, 
                                java.awt.Color.BLUE);
                    } else {
                        writer.fillRect(x1, x2, y1, y2, 
                                java.awt.Color.RED);
                    }
                    x1 += stripCellSize;
                    x2 += stripCellSize;
                }
                x1 = 0; // Reset X-verdien for neste rad
                x2 = stripCellSize;
                y1 += stripCellSize; // Plusser på for neste rad
                y2 += stripCellSize;
            }
            writer.insertAndProceed();
            game.update();
            writeGoLSequenceToGIF(writer, game, --counter);
        }
    }

    public void setColors(Color backgroundColor, 
            Color aliveCellColor, Color deadCellColor) {
        this.backgroundColor = backgroundColor;
        this.aliveCellColor = aliveCellColor;
        this.deadCellColor = deadCellColor;
    }
}
