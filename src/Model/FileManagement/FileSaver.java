package Model.FileManagement;

import Model.FileManagement.Decoders.Decoder;
import Model.FileManagement.Decoders.RLEDecoder;
import Model.FileManagement.Encoders.*;
import Model.FileManagement.OtherFormats.Data.GIFData;
import Model.FileManagement.OtherFormats.Data.ImageData;
import Model.FileManagement.OtherFormats.GIFSaver;
import Model.FileManagement.OtherFormats.ImageSaver;
import Model.FileManagement.OtherFormats.JPEGSaver;
import Model.GameOfLife.GameOfLife;
import java.io.File;

/**
 * Ansvaret for lagring av filer. Skal bedømme hvilken encoder som trengs for å 
 * lagre filen
 * 
 * @author Robin
 */
public class FileSaver {
    
    public void saveGame(EncodeType type, GameOfLife game, File f) {
        Encoder encoder = null;
        switch(type) {
            case RLE:
                encoder = new RLEEncoder(game, f);
                break;
        }
        encoder.encode();
    }
    
    public void saveImage(ImageType type, ImageData data) {
        ImageSaver saver = null;
        switch(type) {
            case JPEG:
                saver = new JPEGSaver(data);
                break;
            case GIF:
                saver = new GIFSaver((GIFData) data);
                break;
        }
        saver.saveImage();
    }
    
}
