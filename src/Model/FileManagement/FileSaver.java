package FileManagement;

import FileManagement.Encoders.Encoder;
import FileManagement.Encoders.RLEEncoder;
import FileManagement.OtherFormats.GIFSaver;
import GameOfLife.GameOfLife;
import java.awt.Color;
import java.io.File;

/**
 *
 * @author Robin
 */
public class FileSaver {
    private File defaultFileDirectory = new File("Patterns");
    private File defaultImageDirectory = new File("Images");
    
    public void initialize() {
        defaultFileDirectory.mkdir();
        defaultImageDirectory.mkdir();
    }
    
    public void saveGame(EncodeType type, GameOfLife game, File f) {
        Encoder encoder = null;
        switch(type) {
            case RLE:
                encoder = new RLEEncoder(game, f);
                break;
        }
        encoder.encode();
    }
    
    public void saveImage(ImageType type, GameOfLife game, File f) {
        //ImageSaver imageSaver = null;
        switch(type) {
            case GIF:
                GIFSaver gifSaver = new GIFSaver(game);
                gifSaver.setAnimationTimer(0);//OHNO
                gifSaver.setColors(Color.yellow, Color.yellow);//NONO
                gifSaver.setFile(f);//EHH?!
                gifSaver.setHeight(0);//HOW?
                gifSaver.setWidth(0);//WHAT?!
                gifSaver.saveToGif();//Åssen skal vi nå disse greiene over fra denne klassen?
                //MÅ HA SAVEDIALOG I DENNE KLASSEN!
                break;
        }
    }
    
    public File getDefaultFileDirectory() {
        return defaultFileDirectory;
    }
    
    public File getDefaultImageDirectory() {
        return defaultImageDirectory;
    }
    
}
