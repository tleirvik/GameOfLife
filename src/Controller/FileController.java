package Controller;

import Model.FileManagement.EncodeType;
import Model.FileManagement.FileLoader;
import Model.FileManagement.FileSaver;
import Model.FileManagement.ImageType;
import Model.FileManagement.OtherFormats.Data.*;
import Model.GameOfLife.Boards.Board.BoardType;
import Model.GameOfLife.GameOfLife;
import Model.util.DialogBoxes;
import static Model.util.DialogBoxes.customUtilityDialog;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private boolean giveLoadedPattern;
    
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
    public void loadBoard(GameOfLife gol, Stage owner) {
        boolean loadSuccessful;
        
        Object[] args = loadBoardDialog(owner);
        if(args == null) {
            return;
        }
        
        EncodeType encodetype = (EncodeType) args[0];
        
        if(args[1].equals("DISK")) {
            String filePath = (String) args[2];
            loadSuccessful = fileLoader.loadBoard(new File(filePath), encodetype);
        } else {
            String urlString = (String) args[2];
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException muE) {
                DialogBoxes.openAlertDialog(Alert.AlertType.ERROR, "Error!", 
                    "'" + urlString + "' is not a valid URL", muE.getMessage());
                return;
            }
            loadSuccessful = fileLoader.loadBoardFromURL(url, encodetype);
        }
        
        if(!loadSuccessful) {
            return;
        }
        
        if(args[3].equals("NEW BOARD")) {
            BoardType boardType = (BoardType) args[4];
            gol.loadGame(fileLoader.getBoard(), fileLoader.getMetadata(), boardType);
        } else {
            giveLoadedPattern = true;
        }
    }
    
    private File loadFileChooser(Stage owner, List<ExtensionFilter> extFilter) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setTitle("Open Resource File");
        return fileChooser.showOpenDialog(owner);
    }
    
    private Object[] loadBoardDialog(Stage owner) {
        GridPane gp = new GridPane();
        
        gp.add(new Label("Encode Type: "), 0, 0);
        ChoiceBox<EncodeType> encodeType = new ChoiceBox<>();
        encodeType.getItems().setAll(EncodeType.values());
        encodeType.setValue(EncodeType.RLE);
        gp.add(encodeType, 1, 0);
        
        ToggleGroup diskOrURL = new ToggleGroup();
        
        Label loadBoardFrom = new Label("Load Board From: ");
        loadBoardFrom.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gp.add(loadBoardFrom,0, 1);
        //===================
        //       DISK
        //===================
        RadioButton selectDisk = new RadioButton();
        selectDisk.setUserData("DISK");
        selectDisk.setToggleGroup(diskOrURL);
        selectDisk.setText("From Disk");
        selectDisk.setSelected(true);
        gp.add(selectDisk, 0, 2);
        
        TextField filePath = new TextField();
        filePath.setEditable(false);
        filePath.setPromptText("Browse for file");
        gp.add(filePath, 1, 2, 2, 1);
        Button browse = new Button("Browse");
        gp.add(browse, 3, 2);
        
        browse.setOnAction(e -> {
            EncodeType type = encodeType.getValue();
            List<ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new ExtensionFilter(type.getName(),type.getFileExtensions()));
            File file = loadFileChooser((Stage)browse.getScene().getWindow(), extFilter);
            filePath.setText(file.getAbsolutePath());
        });
        
        //===================
        //        URL
        //===================
        RadioButton selectURL = new RadioButton();
        selectURL.setUserData("URL");
        selectURL.setToggleGroup(diskOrURL);
        selectURL.setText("From URL");
        gp.add(selectURL, 0, 4);
        
        TextField url = new TextField();
        url.setPromptText("Enter URL");
        gp.add(url, 1, 4, 2, 1);
        url.setDisable(true);
        
        diskOrURL.selectedToggleProperty().addListener(e -> {
            boolean disable = selectURL.isSelected();
            filePath.setDisable(disable);
            browse.setDisable(disable);
            url.setDisable(!disable);
        });
        
        gp.add(new Separator(), 0, 6, 4, 1);
        
        Label loadBoardInto = new Label("Load Board Into: ");
        loadBoardInto.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gp.add(loadBoardInto,0, 7);
        
        ToggleGroup loadType = new ToggleGroup();
        
        //===================
        //   TO NEW BOARD
        //===================
        RadioButton newBoard = new RadioButton();
        newBoard.setUserData("NEW BOARD");
        newBoard.setToggleGroup(loadType);
        newBoard.setText("New Board");
        newBoard.setSelected(true);
        gp.add(newBoard, 0, 8);
        
        gp.add(new Label("Board Type: "), 1, 9);
        ChoiceBox<BoardType> boardType = new ChoiceBox<>();
        boardType.getItems().setAll(BoardType.values());
        boardType.setValue(BoardType.DYNAMIC);
        gp.add(boardType, 2, 9);
        
        //===================
        // DROP FROM MOUSE POINTER
        //===================
        RadioButton droppablePattern = new RadioButton();
        droppablePattern.setUserData("MOUSE POINTER");
        droppablePattern.setToggleGroup(loadType);
        droppablePattern.setText("Droppable from Mouse Pointer");
        gp.add(droppablePattern, 0, 10, 2, 1);
        
        loadType.selectedToggleProperty().addListener(e -> {
            boardType.setDisable(droppablePattern.isSelected());
        });
        
        Dialog dialog = DialogBoxes.customUtilityDialog("Load board", null, gp, 
                owner);
        
        ButtonType open = new ButtonType("Open", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(open, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get() == open) {
            Object[] args = new Object[5];
            
            args[0] = encodeType.getValue();
            
            args[1] = (String) diskOrURL.getSelectedToggle().getUserData();
            if(args[1].equals("DISK")) {
                if(filePath.getText().equals("")) {
                    return null;
                }
                args[2] = filePath.getText();
            } else {
                if(url.getText().equals("")) {
                    return null;
                }
                args[2] = url.getText();
            }
            
            args[3] = (String) loadType.getSelectedToggle().getUserData();
            if(args[3].equals("NEW BOARD")) {
                args[4] = boardType.getValue();
            }
            
            return args;
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
        
        File f = saveFileChooser(owner, extFilter);
        
        if (f == null) {
            return;
        }
        
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
        
        final Slider animationSlider = new Slider(50,500,250);
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
        
        final Slider iterSlider = new Slider(2,100,10);
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
        if(giveLoadedPattern) {
            giveLoadedPattern = false;
            return fileLoader.getBoard();
        }
        return null;
    }
}
