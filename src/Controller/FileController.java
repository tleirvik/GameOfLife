package Controller;

import Model.FileManagement.EncodeType;
import Model.FileManagement.FileLoader;
import Model.FileManagement.FileSaver;
import Model.FileManagement.ImageType;
import Model.FileManagement.OtherFormats.Data.*;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import Model.GameOfLife.PatternFormatException;
import Model.util.DialogBoxes;
import static Model.util.DialogBoxes.customUtilityDialog;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Ansvaret for å åpne dialogbokser og flytte data rundt for å lagre/laste filer.
 * Skal ikke lagre eller laste filene selv
 * 
 * 
 * @author Robin
 */
public class FileController {
    private final FileChooser fileChooser;
    private final FileSaver fileSaver;
    private final FileLoader fileLoader;
    
    private final File defaultPatternDirectory = new File("Patterns");
    private final File defaultImageDirectory = new File("Images");
    
    public FileController() {
        fileChooser = new FileChooser();
        fileSaver = new FileSaver();
        fileLoader = new FileLoader();
        
        defaultPatternDirectory.mkdir(); //Returns true false if folder is created/exists
        defaultImageDirectory.mkdir();
    }
    
    public File[] getPatternDirectoryFiles() {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String fileExtension = getFileExtension(name);
                //True om den er RLE, false hvis den er noe annet
                return fileExtension.equals(".rle");
            }
        };
        
        return defaultPatternDirectory.listFiles(filter);
    }
    
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    //=========================================================================
    //                              LOAD
    //=========================================================================
    public boolean loadBoard(Stage owner, boolean online) {
        if(online) {
            String urlString = urlDialogBox(owner);
            if(urlString == null) { //User closed dialog without entering a URL
                return false;
            }
            
            URL url;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException ex) {
                DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Invalid URL", 
                        null, "The submitted text is not a valid URL");
                return false;
            }
            
            //Finn ut filtypen
            EncodeType type = null;
            String fileExtension = getFileExtension(urlString);
            System.out.println(fileExtension);
            for (EncodeType e : EncodeType.values()) {
                if(Arrays.asList(e.getFileExtensions()).contains(fileExtension)) {
                    type = e;
                    break;
                }
             }
           
           if(type == null) {
               DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Unknown encoding",
                       "The file's encoding could not be determined", 
                       "This program only supports files ending with .rle, .lif and .life");
               return false;
           }
           
           fileLoader.loadBoardFromURL(url, type);
        } else {
            List<ExtensionFilter> extFilter = new ArrayList<>();
            
            //Itererer igjennom Encode-enumene og legger til alle
            for (EncodeType e : EncodeType.values()) {
                extFilter.add(new ExtensionFilter(e.getName(), e.getFileExtensions()));
            }
            
            File f = loadFileChooser(owner, extFilter);
            
            if(f == null) {
                return false;
            }
            
            //Finn ut hva brukeren valgte som encoding
            EncodeType type = null;
            ExtensionFilter extensionFilter = fileChooser.getSelectedExtensionFilter();
            for (EncodeType e : EncodeType.values()) {
                if(extensionFilter.getDescription().matches(e.getName())) {
                    type = e;
                    break;
                }
            }
                        
            try {
                fileLoader.loadBoard(f, type);
            } catch (IOException ex) {
                //TODO: 01.05.2016 Endre alle exceptions til å enten bruke logger eller noe annet
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PatternFormatException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
    
    private File loadFileChooser(Stage owner, List<ExtensionFilter> extFilter) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setTitle("Open Resource File");
        return fileChooser.showOpenDialog(owner);
    }
    
    private String urlDialogBox(Stage owner) {
        GridPane gp = new GridPane();
        
        gp.add(new Label("URL: "), 0, 0);
        TextField url = new TextField();
        url.setPromptText("Enter URL");
        gp.add(url, 1, 0);
        
        Dialog dialog = DialogBoxes.customUtilityDialog("Load board from URL", 
                "Please enter the URL", gp, owner);
        
        ButtonType open = new ButtonType("Open", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(open, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get() == open && !url.getText().equals("")) {
            return url.getText();
        } else {
            return null;
        }
    }
    
    
    
    
    //=========================================================================
    //                              SAVE
    //=========================================================================
    public void saveBoard(GameOfLife game, EncodeType type, Stage owner) {
        List<FileChooser.ExtensionFilter> extFilter = new ArrayList<>();
        //TODO: Lag en switch på type for extensionfilter
        extFilter.add(new ExtensionFilter("RLE files", "*.rle"));
        extFilter.add(new ExtensionFilter("All Files", "*.*"));
        
        File f = loadFileChooser(owner, extFilter);
        
        fileSaver.saveGame(type, game, f);
    }
    
    public void saveSound(GameOfLife game, Stage owner) {
        WavData wavData = saveToWavDialog(game, owner);
        
        if (wavData == null) {
            return;
        }
        fileSaver.saveSound(wavData);
    }
    
    public void saveImage(GameOfLife game, Stage owner) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO: FIX HER For JPEG, PNG
    }
    
    
    public void saveAnimation(GameOfLife game, Stage owner) {
        //Can be updated to support other formats
        GIFData gifData = saveToGIFDialog(game, owner);
        if (gifData == null) {//Bruker avbrøt dialogboksen
            return;
        }
        fileSaver.saveImage(ImageType.GIF, gifData);
    }
    
    private File saveFileChooser(Stage owner, List<ExtensionFilter> extFilter) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setTitle("Save File");
        return fileChooser.showSaveDialog(owner);
    }
    
    private WavData saveToWavDialog(GameOfLife game, Stage owner) {
        GridPane gp = new GridPane();

        //=========================================
        //  Duration in seconds
        //=========================================
        gp.add(new Label("Duration (in seconds): "), 0, 0);
        
        final Spinner spinnerDuration = new Spinner(1, 10000, 30, 10);
        spinnerDuration.setEditable(true);
        TextField durationField = spinnerDuration.getEditor();
        durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                durationField.setText(oldValue);
            }
        });
        gp.add(spinnerDuration, 1, 0, 3, 1);
        
        //=========================================
        //  Iterations
        //=========================================
        gp.add(new Label("Number of Iterations:"), 0, 1);
        
        final Slider iterSlider = new Slider(2,20,10);
        iterSlider.setShowTickMarks(true);
        iterSlider.setShowTickLabels(true);
        iterSlider.setMajorTickUnit(2);
        iterSlider.setMinorTickCount(0);
        gp.add(iterSlider,0, 5, 2, 1);
        
        final Label iterationsLabel = new Label("10");
        gp.add(iterationsLabel, 1, 4);
        
        iterSlider.valueProperty().addListener(e -> {
            iterationsLabel.setText(String.format("%d", 
                    iterSlider.valueProperty().intValue()));
        });
        
        Dialog dialog = customUtilityDialog("Save to WAV", null, gp, owner);
        
        ButtonType saveChanges = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveChanges, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == saveChanges){
            List<FileChooser.ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new FileChooser.ExtensionFilter("GIF files", "*.gif"));
            extFilter.add(new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = saveFileChooser(owner, extFilter);
            
            if (file == null) {
                return null;
            }
            
            int iterations = (int) iterSlider.getValue();
            int sampleRate = 44100;
            int channels = 2;
            int durationInSeconds = (int) spinnerDuration.getValue();
            int bits = 16;
            //TODO: Gjør mindre hardkodet
            
            return new WavData(game, iterations, sampleRate, channels, 
                    durationInSeconds, bits, file);
        }
        
        return null;
    }
    
    private GIFData saveToGIFDialog(GameOfLife game, Stage owner) {
        GridPane gp = new GridPane();

        //=========================================
        //  GIF-size
        //=========================================
        gp.add(new Label("Width: "), 0, 0, 1, 1);
        gp.add(new Label("Height: "), 0, 1, 1, 1);
        
        final Spinner spinnerWidth = new Spinner(10, 10000, 200, 10);
        spinnerWidth.setEditable(true);
        TextField widthField = spinnerWidth.getEditor();
        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                widthField.setText(oldValue);
            }
        });
        gp.add(spinnerWidth, 1, 0, 3, 1);
        
        final Spinner spinnerHeight = new Spinner(10, 10000, 200, 10);
        spinnerHeight.setEditable(true);
        TextField heightField = spinnerHeight.getEditor();
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                heightField.setText(oldValue);
            }
        });
        gp.add(spinnerHeight, 1, 1, 3, 1);
        
        //=========================================
        //  Animation-speed
        //=========================================
        gp.add(new Label("Animation Speed:"), 0, 2);
        
        final Label sliderLabel = new Label("500 ms");
        gp.add(sliderLabel,1 ,2);
        
        final Slider animationSlider = new Slider(125,1000,500);
        animationSlider.setShowTickMarks(true);
        animationSlider.setShowTickLabels(true);
        animationSlider.setMajorTickUnit(100);
        animationSlider.setMinorTickCount(0);
        gp.add(animationSlider, 0, 3, 2, 1);
        
        animationSlider.valueProperty().addListener(e-> {
            sliderLabel.setText(String.format("%d ms", 
                    animationSlider.valueProperty().intValue()));
        });
        
        //=========================================
        //  Iterations
        //=========================================
        gp.add(new Label("Number of Iterations:"), 0, 4);
        
        final Slider iterSlider = new Slider(2,20,10);
        iterSlider.setShowTickMarks(true);
        iterSlider.setShowTickLabels(true);
        iterSlider.setMajorTickUnit(2);
        iterSlider.setMinorTickCount(0);
        gp.add(iterSlider,0, 5, 2, 1);
        
        final Label iterationsLabel = new Label("10");
        gp.add(iterationsLabel, 1, 4);
        
        iterSlider.valueProperty().addListener(e -> {
            iterationsLabel.setText(String.format("%d", 
                    iterSlider.valueProperty().intValue()));
        });
        
        //=========================================
        //  GIF-colors
        //=========================================
        gp.add(new Label("Alive Cell Color"), 0, 6);
        final ColorPicker aliveCellColor = new ColorPicker(Color.web("#ccffff"));
        gp.add(aliveCellColor, 1, 6);
        
        gp.add(new Label("Dead Cell Color"), 0, 7);
        final ColorPicker deadCellColor = new ColorPicker(Color.web("#003333"));
        gp.add(deadCellColor, 1, 7);
        
        Dialog dialog = customUtilityDialog("Save to GIF", null, gp, owner);
        
        ButtonType saveChanges = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveChanges, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == saveChanges){
            List<FileChooser.ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new FileChooser.ExtensionFilter("GIF files", "*.gif"));
            extFilter.add(new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = saveFileChooser(owner, extFilter);
            
            if (file == null) {
                return null;
            }
            
            java.awt.Color aliveColor = convertFXColorToAWTColor(aliveCellColor.getValue()), 
                            deadColor = convertFXColorToAWTColor(deadCellColor.getValue());
            int height = (int) spinnerHeight.getValue(),
                 width = (int) spinnerWidth.getValue();
            int cellSize = (int) calculateSize(height, width, game.getRows(), 
                    game.getColumns());
            int iterations = (int) iterSlider.getValue();
            int animationTimer = (int) animationSlider.getValue();
            
            return new GIFData(game, aliveColor, deadColor, file, height, width, 
                    cellSize, iterations, animationTimer);
        }
        return null;
    }
    
    
    private java.awt.Color convertFXColorToAWTColor(javafx.scene.paint.Color c) {
        float r = (float) c.getRed();
        float g = (float) c.getGreen();
        float b = (float) c.getBlue();
        float o = (float) c.getOpacity();
        return new java.awt.Color(r, g, b);
    }

    private double calculateSize(double availibleHeight, double availibleWidth,
            int rows, int columns) {
        double sizeHeight = availibleHeight / rows;
        double sizeWidth = availibleWidth / columns;
        return Math.min(sizeWidth, sizeHeight);
    }
    
    public byte[][] getBoard() {
        return fileLoader.getBoard();
    }
    
    public MetaData getMetadata() {
       return fileLoader.getMetadata();
    }
}
