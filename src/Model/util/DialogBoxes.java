package Model.util;

import Controller.StatisticsController;
import Model.GameOfLife.Boards.Board.BoardType;
import Controller.EditorController;
import Controller.FileController;
import Controller.HelpController;
import Controller.ViewController;
import Model.GameOfLife.GameOfLife;
import Model.GameOfLife.MetaData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.StageStyle;

/**
 * This is a utility class that opens the game dialog boxes
 *
 * @see Controller.ViewController
 * @see StatisticsController
 * @see EditorController
 * @see FileController
 */
public class DialogBoxes {
    
    //=========================================================================
    //                            STATIC METHODS
    //=========================================================================
    /**
     * Returns a dialog box with the given title and sets the given gridpane as content. 
     * Padding and spacing is added before the dialog is returned
     * If optionalHeader is equal to "" or null, a header will not be created
     * 
     * The returned dialog does not contain buttons, and these have to added before 
     * showAndWait() is called. Failure to do so will result in a non-closeable, blocking dialog.
     * 
     * @param title The title of the dialog box
     * @param optionalHeader An optional header string
     * @param gp The content of the dialog box
     * @param owner The Stage that "owns" the dialog box
     * @return The dialog box
     */
    public static Dialog customUtilityDialog(String title, String optionalHeader, GridPane gp, Stage owner) {
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        
        dialog.setHeaderText(optionalHeader);
                
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(owner);
        
        gp.setHgap(10);
    	gp.setVgap(10);
    	gp.setPadding(new Insets(25, 25, 25, 25));
        
        dialog.getDialogPane().setContent(gp);
        
        return dialog;
    }

    /**
     * This static method shows a dialog box and is useful for presenting errors to the user
     *
     * @param type The {@link AlertType} of the {@link Dialog} box
     * @param title The title of the {@link Dialog} box
     * @param headerText The header text of the {@link Dialog} box
     * @param contentText The content text of the {@link Dialog} box
     */
    public static void openAlertDialog(AlertType type, String title, String headerText, String contentText) {
        Alert dialog = new Alert(type);
    	dialog.setTitle(title);
    	dialog.setHeaderText(headerText);
    	dialog.setContentText(contentText);
        dialog.showAndWait();
    }

    //=========================================================================
    //                            DIALOGS
    //=========================================================================

    /**
     * This presents the new game dialog to the user and lets choose a game
     * @param gol The {@link GameOfLife}
     * @param owner The {@link Stage} that owns the new dialog box
     */
    public void openNewGameDialog(GameOfLife gol, Stage owner) {
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
            if (!newValue.matches("[0-9]*")) {
                rows.setText(oldValue);
            }
        });
        
        columns.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) {
                columns.setText(oldValue);
            }
        });
        
        Dialog dialog = customUtilityDialog("New Game", null, gp, owner);
        
        ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ok && !rows.getText().equals("") 
                && !columns.getText().equals("")) {
            int numRows = Integer.parseInt(rows.getText());
            int numColumns = Integer.parseInt(columns.getText());
            BoardType type = boardType.getValue();
            boolean randomPattern = random.isSelected();
            
            if (randomPattern) {
                gol.newRandomGame(numRows, numColumns, type);
            } else {
                gol.newEmptyGame(numRows, numColumns, type);
            }
        }
    }

    /**
     * This method presents a meta data dialog box to the user and lets the
     * user edit and save the meta data
     * @param metadata The {@link MetaData} object to use in the dialog box
     * @param owner The {@link Stage} that owns the new dialog box
     */
    public void metaDataDialogBox(MetaData metadata, Stage owner) {        
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
        
        Dialog dialog = customUtilityDialog("Edit Metadata", null, gp, owner);
        
        ButtonType saveChanges = new ButtonType("Save Changes", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveChanges, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == saveChanges){
            metadata.setAuthor(authorTextField.getText());
            metadata.setName(nameTextField.getText());
            metadata.setComment(commentTextArea.getText());
        }
    }
    
    public Color[] openOptionsDialog(ViewController vCtrl, Stage owner) {
        Color[] colors = vCtrl.getColors();
        GridPane gp = new GridPane();
        
        gp.add(new Label("Background Color: "), 0, 0);
        ColorPicker backgroundColor = new ColorPicker(colors[0]);
        gp.add(backgroundColor, 1, 0);
        
        gp.add(new Label("Board Background Color: "), 0, 1);
        ColorPicker boardBackgroundColor = new ColorPicker(colors[1]);
        gp.add(boardBackgroundColor, 1, 1);
        
        gp.add(new Label("Cell Color: "), 0, 2);
        ColorPicker cellColor = new ColorPicker(colors[2]);
        gp.add(cellColor, 1, 2);
        
        gp.add(new Label("Grid Color: "), 0, 3);
        ColorPicker gridColor = new ColorPicker(colors[3]);
        gp.add(gridColor, 1, 3);
        
        Dialog dialog = customUtilityDialog("Options", null, gp, owner);
        
        ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ok) {
            colors = new Color[]{
                    backgroundColor.getValue(), boardBackgroundColor.getValue(),
                    cellColor.getValue(), gridColor.getValue()
            };
        }
        return colors;
    }
    
    //=========================================================================
    //                         FXML-based windows
    //=========================================================================

    /**
     * This launches a new FXML window and starts a new {@link EditorController}
     * @param game The {@link GameOfLife} object to use in the editor
     * @param fileController The {@link FileController} to use in the editor
     * @param owner The {@link Stage} that owns the new dialog box
     */
    public void openPatternEditor(GameOfLife game, FileController fileController, 
            Stage owner) {
        Stage editor = new Stage();
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(owner);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/View/PatternEditor.fxml"));
            
            BorderPane root = loader.load();
            editor.setResizable(false);
            
            EditorController edController = loader.getController();
            edController.initializeEditor(game, fileController);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(
                    "/View/patternEditorStyleSheet.css").toExternalForm());
            
            editor.setScene(scene);
            editor.setTitle("Pattern Editor");
            editor.showAndWait();
        } catch (IOException e) {
            DialogBoxes.openAlertDialog(AlertType.ERROR, "Error", 
                    "Could not open the Pattern Editor", e.getMessage());
        } 
    }

    /**
     * This launches a new FXML window and starts a new {@link StatisticsController}
     * @param game The {@link GameOfLife} object to use in the editor
     * @param owner The {@link Stage} that owns the new dialog box
     */
    public void openStatistics(GameOfLife game, Stage owner) {
        Stage stats = new Stage();
        stats.initModality(Modality.WINDOW_MODAL);
        stats.initOwner(owner);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/View/statistics.fxml"));
            
            BorderPane root = loader.load();
            stats.setResizable(false);
            
            StatisticsController statsCtrl = loader.getController();
            statsCtrl.initializeStatistics(game);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(
                "/View/statisticsGraphics.css").toExternalForm());
            
            stats.setScene(scene);
            stats.setTitle("Game Of Life Statistics");
            stats.show();
        } catch (IOException e) {
            DialogBoxes.openAlertDialog(AlertType.ERROR, "Error", 
                    "Could not open Statistics", e.getMessage());
        } 
    }
    
    //TODO: Javadoc
    public void openHelp(Stage owner) {
        Stage help = new Stage();
        help.initModality(Modality.WINDOW_MODAL);
        help.initOwner(owner);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/View/HelpWindow.fxml"));
            
            BorderPane root = loader.load();
            help.setResizable(false);
            
            HelpController helpCtrl  = loader.getController();
            helpCtrl.initializeHelp();

            Scene scene = new Scene(root);
            
            help.setScene(scene);
            help.setTitle("Game Of Life Help");
            help.show();
        } catch (IOException e) {
            DialogBoxes.openAlertDialog(AlertType.ERROR, "Error", 
                    "Could not open Help dialog", e.toString());
        } 
    }
}