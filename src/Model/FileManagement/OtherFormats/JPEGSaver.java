/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.FileManagement.OtherFormats;

import Model.FileManagement.OtherFormats.Data.ImageData;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robin
 */
public class JPEGSaver extends ImageSaver {

    public JPEGSaver(ImageData data) {
        super(data);
    }

    public void saveImage() {
        BufferedImage boardImage = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = boardImage.createGraphics();
        
        int x1 = 0;
        int x2 = cellSize;
        int y1 = 0;
        int y2 = cellSize;

        for (int row = 0; row < game.getRows() -1; row++) {
            for (int col = 0; col < game.getColumns() -1; col++) {
                if (game.getCellAliveState(row, col) == 1) {
                    graphics2D.setPaint(aliveCellColor);
                } else {
                    graphics2D.setPaint(deadCellColor);
                }
                graphics2D.fillRect(x1, x2, y1, y2);
                x1 += cellSize;
                x2 += cellSize;
            }
            x1 = 0; // Reset X-verdien for neste rad
            x2 = cellSize;
            y1 += cellSize; // Plusser på for neste rad
            y2 += cellSize;
        }
        
    }
}
