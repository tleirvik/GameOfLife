package Model.util;

import Controller.StatisticsController;
import Model.FileManagement.GIFSaver;
import Model.GameOfLife.Boards.Board.BoardType;
import Controller.EditorController;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import Controller.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DialogBoxes {
    
    private final ViewController viewControllerReference;
    private Stage mainStage;
    
    public DialogBoxes(ViewController viewController) {
        this.viewControllerReference = viewController;
    }
    
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    
    public static void setDialogBox(Dialog dialog, String title, String headerText, String contentText) {
        dialog.initStyle(StageStyle.UTILITY);
    	dialog.setTitle(title);
    	dialog.setHeaderText(headerText);
    	dialog.setContentText(contentText);
    }
    
    /**
    * Static function with the purpose of "throwing" dialog boxes
    * @param title The title of the dialog box
    * @param headerText The header text of the dialog box
    * @param contentText The content text of the dialog box
    */
    public static void infoBox(String title, String headerText, String contentText) {
    	Alert alertDialog = new Alert(AlertType.WARNING);
        setDialogBox(alertDialog, title, headerText, contentText);
    	alertDialog.showAndWait();
    }

    public String urlDialogBox() {
        TextInputDialog urlDialog = new TextInputDialog();
        setDialogBox(urlDialog, "Open File from URL", "Please enter the URL to the file", "");
        Optional<String> result = urlDialog.showAndWait();
        
        if (result.isPresent()){
            return result.get();
        } else {
            return null;
        }        
    }
    
    public boolean customConfirmationDialog(GridPane gp, String title) {
        Alert metadatabox = new Alert(AlertType.CONFIRMATION);
        setDialogBox(metadatabox, title, null, null);
        metadatabox.setGraphic(null);
        
        gp.setHgap(10);
    	gp.setVgap(10);
    	gp.setPadding(new Insets(25, 25, 25, 25));
        metadatabox.getDialogPane().setContent(gp);

        Optional<ButtonType> result = metadatabox.showAndWait();
        
        if(result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public void metaDataDialogBox(MetaData metadata) {        
        GridPane gp = new GridPane();
        
        TextField nameTextField = new TextField();
        TextField authorTextField = new TextField();
        TextArea commentTextArea = new TextArea();
        RadioButton gameRulesRadioButton = new RadioButton();
        
    	if (metadata != null) {
            nameTextField.setText(metadata.getName());
            authorTextField.setText(metadata.getAuthor());
            commentTextArea.setText(metadata.getComment());
    	}

   	gp.add(new Label("Pattern name: "), 0, 0);
    	gp.add(nameTextField, 1, 0);
        
        gp.add(new Label("Author name: "), 0, 1);
    	gp.add(authorTextField, 1, 1);
        
        gp.add(new Label("Comments: "), 0, 2);
    	gp.add(commentTextArea, 1, 2);

        gp.add(new Label("Default Rules"), 0, 3);
    	gp.add(gameRulesRadioButton, 1, 3);

    	gameRulesRadioButton.setOnAction(e -> {
    		String[] SBrules = new String[2];
    		SBrules[0] = "3";
    		SBrules[1] = "23";
    		metadata.setRuleString(SBrules);
    	});
        
        if(customConfirmationDialog(gp, "Edit Metadata")) {
            metadata.setAuthor(authorTextField.getText());
            metadata.setName(nameTextField.getText());
            metadata.setComment(commentTextArea.getText());
        }
    }

    public void openNewGameDialog(GameOfLife gol) {
        GridPane gp = new GridPane();
        
        
        gp.add(new Label("Rows: "), 0, 0);
        gp.add(new Label("Columns: "), 0, 1);
        TextField rows = new TextField();
        TextField columns = new TextField();
        gp.add(rows, 1, 0);
        gp.add(columns, 1, 1);
        
        gp.add(new Label("Board Type: "), 0, 2);
        ChoiceBox<BoardType> boardType = new ChoiceBox<>();
        boardType.getItems().setAll(BoardType.values());
        boardType.setValue(BoardType.FIXED);
        gp.add(boardType, 1, 2);
        
        gp.add(new Label("Random Pattern: "), 0, 3);
        CheckBox random = new CheckBox();
        gp.add(random, 1, 3);
        
        rows.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                rows.setText(oldValue);
            }
        });
        
        columns.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                columns.setText(oldValue);
            }
        });
                    
        if(customConfirmationDialog(gp, "New Game") &&
           rows.getText() != "" && columns.getText() != "") {
            int numRows = Integer.parseInt(rows.getText());
            int numColumns = Integer.parseInt(columns.getText());
            BoardType type = boardType.getValue();
            boolean randomPattern = random.isSelected();
            
            if(randomPattern) {
                gol.newRandomGame(numRows, numColumns, type);
            } else {
                gol.newEmptyGame(numRows, numColumns, type);
            }
        }
    }

    public void openPatternEditor(GameOfLife game) {
        Stage editor = new Stage();
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(mainStage);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../View/PatternEditor.fxml"));
            
            BorderPane root = loader.load();
            editor.setResizable(false);
            
            EditorController edController = loader.getController();
            edController.setPattern(game);
            edController.setDialogBoxes(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(
                    "patternEditor.css").toExternalForm());
            
            editor.setScene(scene);
            editor.setTitle("Pattern Editor");
            editor.show();
        } catch (IOException e) {
            DialogBoxes.infoBox("Error", "Could not open the Pattern Editor", 
                    e.getMessage());
        } 
    }
    
     public void saveToGIFDialog(GIFSaver saver) {
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
            if(!newValue.matches("[0-9]*")) {
                widthField.setText(oldValue);
            }
        });
        gp.add(spinnerWidth, 1, 0, 3, 1);
        
        final Spinner spinnerHeight = new Spinner(10, 10000, 200, 10);
        spinnerHeight.setEditable(true);
        TextField heightField = spinnerHeight.getEditor();
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
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
        
        final Label iterations = new Label("10");
        gp.add(iterations, 1, 4);
        
        iterSlider.valueProperty().addListener(e -> {
            iterations.setText(String.format("%d", 
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
        
        if (customConfirmationDialog(gp, "Save To GIF")) {            
            List<ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new ExtensionFilter("GIF files", "*.gif"));
            extFilter.add(new ExtensionFilter("All Files", "*.*"));
            File file = fileChooser(extFilter, false);
            
            if (file == null) {
                return;
            }
            
            saver.setIterations(3);
            saver.setAnimationTimer(animationSlider.valueProperty().intValue());
            saver.setWidth((int) spinnerWidth.getValue());
            saver.setHeight((int) spinnerHeight.getValue());
            saver.setIterations(iterSlider.valueProperty().intValue());
            saver.setColors(convertFXColorToAWTColor(aliveCellColor.getValue()), 
                    convertFXColorToAWTColor(deadCellColor.getValue()));
            saver.setFile(file);
            saver.saveToGif();
        }
    }
    
    private java.awt.Color convertFXColorToAWTColor(javafx.scene.paint.Color c) {
        
        float r = (float) c.getRed();
        float g = (float) c.getGreen();
        float b = (float) c.getBlue();
        float o = (float) c.getOpacity();
        return new java.awt.Color(r, g, b);
    }
    
    public File fileChooser(List<ExtensionFilter> extFilter, boolean load) {
        Stage mainStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        
        fileChooser.getExtensionFilters().addAll(extFilter);
        
        if(load) {
            fileChooser.setTitle("Open Resource File");
            return fileChooser.showOpenDialog(mainStage);
        } else {
            fileChooser.setTitle("Save File");
            return fileChooser.showSaveDialog(mainStage);
        }
    }
    
    public void statistics(GameOfLife game) {
        Stage editor = new Stage();
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(mainStage);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../View/statistics.fxml"));
            
            BorderPane root = loader.load();
            editor.setResizable(false);
            
            StatisticsController stats = loader.getController();
            stats.initializeStatistics(game);

            Scene scene = new Scene(root);
            
            editor.setScene(scene);
            editor.setTitle("Game Of Life Statistics");
            editor.show();
        } catch (IOException e) {
            DialogBoxes.infoBox("Error", "Could not open Statistics", 
                    e.getMessage());
        } 
    }
}