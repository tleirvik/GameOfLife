/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.filemanagement.otherformats;

import model.filemanagement.otherformats.data.ImageData;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Save PNG files
 */
public class PNGSaver extends ImageSaver {

    public PNGSaver(ImageData data) {
        super(data);
    }

    @Override
    public boolean saveImage() {
        BufferedImage boardImage = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = boardImage.createGraphics();
        
        int x = 0;
        int y = 0;

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                if (game.getCellAliveState(row, col) == 1) {
                    graphics2D.setPaint(aliveCellColor);
                } else {
                    graphics2D.setPaint(deadCellColor);
                }
                graphics2D.fillRect(x, y, cellSize, cellSize);
                x += cellSize;
            }
            x = 0;
            y += cellSize;
        }
        
        try {
            ImageIO.write(boardImage, "png", this.file);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
