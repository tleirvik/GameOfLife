package model.filemanagement;

import model.filemanagement.otherformats.data.GIFData;
import model.filemanagement.otherformats.data.ImageData;
import model.filemanagement.otherformats.data.WavData;
import model.filemanagement.otherformats.GIFSaver;
import model.filemanagement.otherformats.ImageSaver;
import model.filemanagement.otherformats.PNGSaver;
import model.filemanagement.otherformats.WavSaver;
import java.io.File;
import model.filemanagement.encoders.Encoder;
import model.filemanagement.encoders.RLEEncoder;
import model.gameoflife.GameOfLife;

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
     * @return True if the file has been saved
     */
    public boolean saveGame(EncodeType type, GameOfLife game, File f) {
        Encoder encoder = new RLEEncoder(game, f);
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
            case PNG:
                saver = new PNGSaver(data);
                break;
            case GIF:
                saver = new GIFSaver((GIFData) data);
                break;
        }
        return saver.saveImage();
    }
    
}
