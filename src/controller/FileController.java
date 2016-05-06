package controller;

import model.filemanagement.EncodeType;
import model.filemanagement.FileLoader;
import model.filemanagement.FileSaver;
import model.filemanagement.ImageType;
import model.filemanagement.otherformats.data.GIFData;
import model.filemanagement.otherformats.data.WavData;
import model.gameoflife.boards.Board.BoardType;
import model.util.DialogBoxes;
import static model.util.DialogBoxes.customUtilityDialog;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.filemanagement.otherformats.data.ImageData;
import model.gameoflife.GameOfLife;

/**
 * The FileController class that handles the interaction with files
 */
public class FileController {
    private final FileChooser fileChooser;
    private final FileSaver fileSaver;
    private final FileLoader fileLoader;
    private boolean giveLoadedPattern;

    /**
     * Constructs a FileController object with the specified parameters
     */
    public FileController() {
        fileChooser = new FileChooser();
        fileSaver = new FileSaver();
        fileLoader = new FileLoader();
    }
    
    //=========================================================================
    //                              LOAD
    //=========================================================================

    /**
     *
     * @param gol The {@link GameOfLife} object to hold the game board
     * @param owner The Stage that "owns" the dialog box
     */
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

    /**
     *  The Load {@link FileChooser} dialog
     * @param owner The Stage that "owns" the dialog box
     * @param extFilter {@link List} of {@link ExtensionFilter} to use in the {@link FileChooser}
     * @return The selected {@link File}
     */
    private File loadFileChooser(Stage owner, List<ExtensionFilter> extFilter) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setTitle("Open Resource File");
        return fileChooser.showOpenDialog(owner);
    }

    /**
     * The generic Load Board dialog that handles loading of files
     * @param owner The Stage that "owns" the dialog box
     * @return An array of arguments that the user chose in the dialog
     */
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
            
            if (file != null) {
                filePath.setText(file.getAbsolutePath());
            }
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

    /**
     *   The Save {@link FileChooser} dialog
     *
     * @param game The {@link GameOfLife} game to be save
     * @param type The {@link EncodeType} of the board to save
     * @param owner The Stage that "owns" the dialog box
     */
    public void saveBoard(GameOfLife game, EncodeType type, Stage owner) {
        List<FileChooser.ExtensionFilter> extFilter = new ArrayList<>();
        extFilter.add(new ExtensionFilter("RLE files", "*.rle"));
        extFilter.add(new ExtensionFilter("All Files", "*.*"));
        
        File f = saveFileChooser(owner, extFilter);
        
        if (f == null) {
            return;
        }
        
        fileSaver.saveGame(type, game, f);
    }

    /**
     * The Save as Sound dialog
     * @param game The {@link GameOfLife} game to be saved to sound
     * @param owner The Stage that "owns" the dialog box
     * @see WavData
     */
    public void saveSound(GameOfLife game, Stage owner) {
        WavData wavData = saveToWavDialog(game, owner);
        
        if (wavData == null) {
            return;
        }
        fileSaver.saveSound(wavData);
    }

    /**
     * The Save as Image dialog
     * @param game The {@link GameOfLife} game to be saved to image
     * @param owner The Stage that "owns" the dialog box
     */
    public void saveImage(GameOfLife game, Stage owner) {
        ImageData data = data = saveToImageDialog(game, owner);//Open dialog and wait for it to close
        if (data == null) { //If data equals null, user exited the dialog
            return;
        }

        fileSaver.saveImage(ImageType.PNG, data);
    }

    /**
     * The Save as animation dialog
     * @param game The {@link GameOfLife} game to be saved to image
     * @param owner The Stage that "owns" the dialog box
     * @see GIFData
     */
    public void saveAnimation(GameOfLife game, Stage owner) {
        //Can be updated to support other formats
        GIFData data = data = saveToGIFDialog(game, owner);//Open dialog and wait for it to close
        if (data == null) { //If data equals null, user exited the dialog
            return;
        }
        fileSaver.saveImage(ImageType.GIF, data);
    }

    /**
     *  The generic Save {@link FileChooser} dialog
     * @param owner The Stage that "owns" the dialog box
     * @param extFilter {@link List} of {@link ExtensionFilter} to use in the {@link FileChooser}
     * @return The selected {@link File}
     */
    private File saveFileChooser(Stage owner, List<ExtensionFilter> extFilter) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setTitle("Save File");
        return fileChooser.showSaveDialog(owner);
    }

    /**
     * The Save as Wav dialog
     * @param game The {@link GameOfLife} game to be saved to Wav format
     * @param owner The Stage that "owns" the dialog box
     * @see WavData
     * @see model.filemanagement.otherformats.WavSaver
     * @see model.filemanagement.otherformats.wav.WavFile
     * @return A {@link WavData} object
     */
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
            extFilter.add(new FileChooser.ExtensionFilter("WAV files", "*.wav"));
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
            //TODO: Do mindre hardkodet

            return new WavData(game, iterations, sampleRate, channels, 
                    durationInSeconds, bits, file);
        }
        
        return null;
    }
    
    /**
     *  The Save to Image dialog box
     * @param game The {@link GameOfLife} game to be saved as a PNG image
     * @param owner The Stage that "owns" the dialog box
     * @return A {@link ImageData} object
     * @see ImageData
     */
    private ImageData saveToImageDialog(GameOfLife game, Stage owner) {
        GridPane gp = new GridPane();

        //=========================================
        //  Image size
        //=========================================
        gp.add(new Label("Width: "), 0, 0, 1, 1);
        gp.add(new Label("Height: "), 0, 1, 1, 1);

        int minimumWidth = 1 * game.getColumns();
        int minimumHeight = 1 * game.getRows();

        int maximumWidth = minimumWidth * 4;
        int maximumHeight = minimumHeight * 4;

        final Spinner spinnerWidth = new Spinner(minimumWidth, maximumWidth, minimumWidth, minimumWidth);
        spinnerWidth.setEditable(true);
        TextField widthField = spinnerWidth.getEditor();
        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                widthField.setText(oldValue);
            }
        });
        gp.add(spinnerWidth, 1, 0, 3, 1);
        
        final Spinner spinnerHeight = new Spinner(minimumHeight, maximumHeight, minimumHeight, minimumHeight);
        spinnerHeight.setEditable(true);
        TextField heightField = spinnerHeight.getEditor();
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                heightField.setText(oldValue);
            }
        });
        gp.add(spinnerHeight, 1, 1, 3, 1);
        
        //=========================================
        //  Image colors
        //=========================================
        gp.add(new Label("Alive Cell Color"), 0, 6);
        final ColorPicker aliveCellColor = new ColorPicker(Color.web("#ccffff"));
        gp.add(aliveCellColor, 1, 6);
        
        gp.add(new Label("Dead Cell Color"), 0, 7);
        final ColorPicker deadCellColor = new ColorPicker(Color.web("#003333"));
        gp.add(deadCellColor, 1, 7);
        
        Dialog dialog = customUtilityDialog("Save to PNG", null, gp, owner);
        
        ButtonType saveChanges = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveChanges, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == saveChanges){
            List<FileChooser.ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
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
            
            return new ImageData(game, aliveColor, deadColor, file, height, width, cellSize);
        }
        return null;
    }
    
    /**
     *  The Save to Gif dialog box
     * @param game The {@link GameOfLife} game to be saved as a GIF animation
     * @param owner The Stage that "owns" the dialog box
     * @return A {@link GIFData} object
     * @see GIFData
     */
    private GIFData saveToGIFDialog(GameOfLife game, Stage owner) {
        GridPane gp = new GridPane();

        //=========================================
        //  GIF-size
        //=========================================
        gp.add(new Label("Width: "), 0, 0, 1, 1);
        gp.add(new Label("Height: "), 0, 1, 1, 1);

        int minimumWidth = 1 * game.getColumns();
        int minimumHeight = 1 * game.getRows();

        int maximumWidth = minimumWidth * 4;
        int maximumHeight = minimumHeight * 4;

        final Spinner spinnerWidth = new Spinner(minimumWidth, maximumWidth, minimumWidth, minimumWidth);
        spinnerWidth.setEditable(true);
        TextField widthField = spinnerWidth.getEditor();
        widthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                widthField.setText(oldValue);
            }
        });
        gp.add(spinnerWidth, 1, 0, 3, 1);
        
        final Spinner spinnerHeight = new Spinner(minimumHeight, maximumHeight, minimumHeight, minimumHeight);
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

    /**
     * Method to convert between {@link Color} and {@link java.awt.Color}
     * @param c The {@link Color} to convert
     * @return The converted {@link java.awt.Color}
     */
    private java.awt.Color convertFXColorToAWTColor(javafx.scene.paint.Color c) {
        float r = (float) c.getRed();
        float g = (float) c.getGreen();
        float b = (float) c.getBlue();
        float o = (float) c.getOpacity();
        return new java.awt.Color(r, g, b);
    }

    /**
     * This method calculates the size of an element based on the given parameters
     *
     * @param availableHeight The available height for the collection of elements
     * @param availableWidth The available width for the collection of elements
     * @param rows The given rows
     * @param columns The given columns
     * @return The minimum size for each element required to show all elements
     */
    private double calculateSize(double availableHeight, double availableWidth,
            int rows, int columns) {
        double sizeHeight = availableHeight / rows;
        double sizeWidth = availableWidth / columns;
        return Math.min(sizeWidth, sizeHeight);
    }

    /**
     * Returns the <code>byte[][]</code> board
     * @return the <code>byte[][]</code> board
     */
    public byte[][] getBoard() {
        if(giveLoadedPattern) {
            giveLoadedPattern = false;
            return fileLoader.getBoard();
        }
        return null;
    }
}
