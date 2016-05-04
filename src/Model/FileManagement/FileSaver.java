package Model.FileManagement;

import Model.FileManagement.Encoders.*;
import Model.FileManagement.OtherFormats.Data.GIFData;
import Model.FileManagement.OtherFormats.Data.ImageData;
import Model.FileManagement.OtherFormats.Data.WavData;
import Model.FileManagement.OtherFormats.GIFSaver;
import Model.FileManagement.OtherFormats.ImageSaver;
import Model.FileManagement.OtherFormats.JPEGSaver;
import Model.FileManagement.OtherFormats.WavSaver;
import Model.GameOfLife.GameOfLife;
import java.io.File;

/**
 * This class enables the user to save files of different types
 */
public class FileSaver {

    /**
     * This method saves a game to a file using a specified encoder
     *
     * @param type The {@link EncodeType} used for saving the game
     * @param game The {@link GameOfLife} to save
     * @param f The {@link File} to save
     * @return
     */
    public boolean saveGame(EncodeType type, GameOfLife game, File f) {
        Encoder encoder = null;
        switch(type) {
            case RLE:
                encoder = new RLEEncoder(game, f);
                break;
        }
        return encoder.encode();
    }

    /**
     * This method saves a board to a sound
     *
     * @param wav The {@link WavData} to save
     * @return The wave file
     */
    public boolean saveSound(WavData wav) {
        WavSaver wavSaver = new WavSaver(wav);
        return wavSaver.saveSound();
    }

    /**
     * This method saves the board as an image
     * @param type The {@link ImageType} to save
     * @param data The {@link ImageData} to save
     * @return The image file
     */
    public boolean saveImage(ImageType type, ImageData data) {
        ImageSaver saver = null;
        switch (type) {
            case JPEG:
                saver = new JPEGSaver(data);
                break;
            case GIF:
                saver = new GIFSaver((GIFData) data);
                break;
        }
        return saver.saveImage();
    }
    
}
