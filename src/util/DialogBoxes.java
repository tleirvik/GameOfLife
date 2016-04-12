package util;

import FileManagement.GIFSaver;
import GameOfLife.MetaData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;


/**
 * Utility class for handling dialog boxes
 *
 * @see GameOfLife.ViewController
 * @see GameOfLife.EditorController
 */
public class DialogBoxes {

	/**
     * Static function with the purpose of "throwing" dialog boxes
     * @param title The title of the dialog box
     * @param headerText The header text of the dialog box
     * @param contentText The content text of the dialog box
     */

    public static void infoBox(String title, String headerText, String contentText) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);
    	alert.showAndWait();
    }

    /**
     * This dialog box is used for user input of URL string in the RLE downloading and decoding
     *
     * @return The String the user specifies in the input dialog box
     * @see FileManagement.RLEDecoder
     * @see GameOfLife.ViewController
     */
    public String urlDialogBox() {
        StringBuilder urlString = new StringBuilder();

        GridPane gp = new GridPane();
        Scene scene = new Scene(gp, 720, 300);
        Stage mainStage = new Stage();

        Label url = new Label("URL:");

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setSpacing(10);

        TextField urlTextField = new TextField();

        Button okButton = new Button("Open File");
        Button cancelButton = new Button("Cancel");

        gp.setAlignment(Pos.TOP_LEFT);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(25, 25, 25, 25));

        GridPane.setConstraints(url, 0, 0);
        gp.getChildren().add(url);
        GridPane.setConstraints(urlTextField, 1, 0);
        gp.getChildren().add(urlTextField);


        buttonBox.getChildren().addAll(okButton, cancelButton);

        GridPane.setConstraints(buttonBox, 0, 4, 4, 1);
        gp.getChildren().add(buttonBox);

        okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
            public void handle(ActionEvent event) {
                        urlString.append(urlTextField.getText());
                        mainStage.close();
            }
        });

        cancelButton.setOnAction(e -> {
                    mainStage.close();
        });

        mainStage.setTitle("Enter URL");
        mainStage.setScene(scene);
        mainStage.showAndWait();
        return urlString.toString();
    }

    /**
     * This dialog box is used for presenting statistics to the user
     *
     * @see Statistics
     */
    public void statisticsDialogBox() {
        Stage mainStage = new Stage();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * This dialog box launches a file chooser and lets the user specify which file to load
     * @return The user specified file
     */
    public File loadRLEDialogBox() {
        Stage mainStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("RLE files", "*.rle"),
                new ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(mainStage);
        return selectedFile;
    }
    /**
     * This dialog box launches a file chooser and lets the user specify which file to save
     * @return The user specified file
     */
    public File saveRLEDialogBox() {
        Stage mainStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save RLE Pattern to file");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("RLE files", "*.rle"));
        File saveRLEFile = fileChooser.showSaveDialog(mainStage);
        return saveRLEFile;
    }

    /**
     * This dialog box enables the user to edit the meta data for the game board
     *
     * @param metadata The meta data object to view or edit
     */
    public void metaDataDialogBox(MetaData metadata) {
    	GridPane gp = new GridPane();
    	Scene scene = new Scene(gp, 720, 300);
    	Stage mainStage = new Stage();

    	Label patternLabel = new Label("Pattern name:");
    	Label authorLabel = new Label("Author name:");
    	Label commentLabel = new Label("Comments:");
    	Label gameRulesLabel = new Label("Default Rules");

    	HBox buttonBox = new HBox();
    	buttonBox.setAlignment(Pos.BOTTOM_LEFT);
    	buttonBox.setSpacing(10);

    	RadioButton gameRulesRadioButton = new RadioButton();
    	gameRulesRadioButton.setTooltip(new Tooltip("Hjelp"));
    	TextField patternTextArea;
    	TextField authorTextArea;
    	TextArea commentTextArea;

    	if (metadata != null) {
            patternTextArea = new TextField(metadata.getName());
            authorTextArea = new TextField(metadata.getAuthor());
            commentTextArea = new TextArea(metadata.getComment());
    	} else {
            patternTextArea = new TextField();
            authorTextArea = new TextField();
            commentTextArea = new TextArea();
    	}

    	Button okButton = new Button("Save");
    	Button cancelButton = new Button("Cancel");

    	gp.setAlignment(Pos.TOP_LEFT);
    	gp.setHgap(10);
    	gp.setVgap(10);
    	gp.setPadding(new Insets(25, 25, 25, 25));

    	GridPane.setConstraints(patternLabel, 0, 0);
    	gp.getChildren().add(patternLabel);
    	GridPane.setConstraints(patternTextArea, 1, 0);
    	gp.getChildren().add(patternTextArea);

    	GridPane.setConstraints(authorLabel, 0, 1);
    	gp.getChildren().add(authorLabel);
    	GridPane.setConstraints(authorTextArea, 1, 1);
    	gp.getChildren().add(authorTextArea);

    	GridPane.setConstraints(commentLabel, 0, 2);
    	gp.getChildren().add(commentLabel);
    	GridPane.setConstraints(commentTextArea, 1, 2);
    	gp.getChildren().add(commentTextArea);

    	GridPane.setConstraints(gameRulesLabel, 0, 4);
    	GridPane.setConstraints(gameRulesRadioButton, 1, 4);
    	buttonBox.getChildren().addAll(gameRulesLabel, gameRulesRadioButton,
    			okButton, cancelButton);

    	GridPane.setConstraints(buttonBox, 0, 4, 4, 1);
    	gp.getChildren().add(buttonBox);

    	okButton.setOnAction(e -> {
    		System.out.println(authorTextArea.getText());
    		System.out.println(metadata);
    		metadata.setAuthor(authorTextArea.getText());
    		metadata.setName(patternTextArea.getText());
    		metadata.setComment(commentTextArea.getText());
    		mainStage.close();
    	});

    	cancelButton.setOnAction(e -> {
		    mainStage.close();
    	});

    	gameRulesRadioButton.setOnAction(e -> {
    		String[] SBrules = new String[2];
    		SBrules[0] = "3";
    		SBrules[1] = "23";
    		metadata.setRuleString(SBrules);
    	});

    	mainStage.setTitle("Enter meta data");
    	mainStage.setScene(scene);
    	mainStage.showAndWait();
    }

    /**
     * This dialog box lets the user specify the number of rows and columns of the game board
     *
     * @return An array with the rows and columns values
     *
     * @see GameOfLife.GameOfLife
     * @see GameOfLife.FixedBoard
     */
    public int[] openNewGameDialog() {
        int[] array = new int[2];
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 150, 10, 10));
        root.setHgap(10);
        root.setVgap(10);
        Stage stage = new Stage();
        
        Label label1 = new Label("Columns: ");
        Label label2 = new Label("Rows: ");
        GridPane.setConstraints(label1, 0, 0, 1, 1);
        GridPane.setConstraints(label2, 0, 1, 1, 1);
        root.getChildren().addAll(label1, label2);
        
        TextField columns = new TextField();
        columns.setPromptText("Enter a positiv integer.");
        TextField rows = new TextField();
        rows.setPromptText("Enter a positiv integer.");
        GridPane.setConstraints(columns, 1, 0, 3, 1);
        GridPane.setConstraints(rows, 1, 1, 3, 1);
        root.getChildren().addAll(columns, rows);
        
        Label errorLabel1 = new Label();
        Label errorLabel2 = new Label();
        GridPane.setConstraints(errorLabel1, 4, 0);
        GridPane.setConstraints(errorLabel2, 4, 1);
        root.getChildren().addAll(errorLabel1, errorLabel2);

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(okButton, cancelButton);
        root.getChildren().add(hbox);
        
        GridPane.setConstraints(hbox, 1, 3);
        
        okButton.setDefaultButton(true);
        okButton.setOnAction(e -> {
            boolean error = false;
            try {
                errorLabel1.setText("");
                array[0] = Integer.parseInt(columns.getText());
                if(array[0] <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfE) {
                error = true;
               errorLabel1.setText("You must enter an integer.");
               
            }
            try {
                errorLabel2.setText("");
                array[1] = Integer.parseInt(rows.getText());
                if(array[1] <= 0) {
                    throw new NumberFormatException();
                }
            } catch(NumberFormatException nfE) {
                error = true;
                errorLabel2.setText("You must enter an integer.");
            }
            if(!error) {
                stage.close();
            }
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });
        
        Scene scene = new Scene(root, 560, 150);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        
        return array;
    }
    
    
    
    
    public void saveToGIFDialog(GIFSaver saver) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 150, 10, 10));
        root.setHgap(10);
        root.setVgap(10);
        
        Stage stage = new Stage();
        
        final Label label1 = new Label("Width: ");
        final Label label2 = new Label("Height: ");
        GridPane.setConstraints(label1, 0, 0, 1, 1);
        GridPane.setConstraints(label2, 0, 1, 1, 1);
        root.getChildren().addAll(label1, label2);
        
        final String INITIAL_VALUE = "50";
        final Spinner spinnerWidth = new Spinner(10, 1000, 50, 10);
        spinnerWidth.setEditable(true);
        
        final Spinner spinnerHeight = new Spinner(10, 1000, 50, 10);
        spinnerHeight.setEditable(true);
        
        GridPane.setConstraints(spinnerWidth, 1, 0, 3, 1);
        GridPane.setConstraints(spinnerHeight, 1, 1, 3, 1);
        root.getChildren().addAll(spinnerWidth, spinnerHeight);
        
        final Label speed = new Label("Animation Speed: ");
        
        final Label speedLabel = new Label("Animation Speed in milliseconds");
        GridPane.setConstraints(speedLabel, 0, 2, 2, 1);
        root.getChildren().add(speedLabel);
        

        
        final Label errorLabel1 = new Label();
        final Label errorLabel2 = new Label();
        GridPane.setConstraints(errorLabel1, 4, 0);
        GridPane.setConstraints(errorLabel2, 4, 1);
        root.getChildren().addAll(errorLabel1, errorLabel2);
        
        final Label backgroundColorLabel = new Label("Background color");
        GridPane.setConstraints(backgroundColorLabel, 0, 4);
        root.getChildren().add(backgroundColorLabel);
        
        final ColorPicker backgroundColor = new ColorPicker(Color.BLACK);
        GridPane.setConstraints(backgroundColor, 1, 4);
        root.getChildren().add(backgroundColor);
        
        
        final Label aliveCellColorLabel = new Label("Alive Cell Color");
        GridPane.setConstraints(aliveCellColorLabel, 0, 5);
        
        final ColorPicker aliveCellColor = new ColorPicker(Color.GREEN);
        GridPane.setConstraints(aliveCellColor, 1, 5);
        root.getChildren().addAll(aliveCellColorLabel, aliveCellColor);
        
        final Label deadCellColorLabel = new Label("Dead Cell Color");
        GridPane.setConstraints(deadCellColorLabel, 0, 6);
        
        final ColorPicker deadCellColor = new ColorPicker(Color.RED);
        GridPane.setConstraints(deadCellColor, 1, 6);
        root.getChildren().addAll(deadCellColorLabel, deadCellColor);
        
        
        final Button saveButton = new Button("Save");
        saveButton.setDefaultButton(true);
        
        saveButton.setOnAction(e -> {
            int width = 0;
            int height = 0;
            
            boolean error = false;
            try {
                errorLabel1.setText("");
                width = Integer.parseInt(spinnerWidth.getEditor().textProperty().get());
            } catch (NumberFormatException nfE) {
                error = true;
               errorLabel1.setText("You must enter an integer.");
               spinnerWidth.getEditor().textProperty().set(INITIAL_VALUE);
            }
            
            try {
                errorLabel2.setText("");
                height = Integer.parseInt(spinnerHeight.getEditor().textProperty().get());
            } catch (NumberFormatException nfE) {
                error = true;
                errorLabel2.setText("You must enter an integer.");
                spinnerHeight.getEditor().textProperty().set(INITIAL_VALUE);
            }
            
            if (!error) {
                if (width > 1000) {
                    width = 50;
                } else if (height > 1000) {
                    height = 50;
                }
                saver.setWidth(width);
                saver.setHeight(height);
                saver.setColors(convertFXColorToAWTColor(backgroundColor.getValue()),
                    convertFXColorToAWTColor(aliveCellColor.getValue()), 
                    convertFXColorToAWTColor(deadCellColor.getValue()));
                stage.close();
            }
        });
        
        final Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            stage.close();
        });
        
        // An HBox for the Save and Cancel button and adding the HBox to
        // the grid.
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        hbox.getChildren().addAll(saveButton, cancelButton);
        GridPane.setConstraints(hbox, 1, 8);
        root.getChildren().add(hbox);
        
        Scene scene = new Scene(root, 630, 400);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    /**
     * This method convert JavaFX colors  to java.awt.Color to be used
     * in GIFSaver
     * @param c
     * @return 
     * @see GIFSaver
     */
    private java.awt.Color convertFXColorToAWTColor(javafx.scene.paint.Color c) {
        int r = (int) c.getRed();
        int g = (int) c.getGreen();
        int b = (int) c.getBlue();
        
        return new java.awt.Color(r, g, b);
    }
}
