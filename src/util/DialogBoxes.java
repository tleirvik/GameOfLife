package util;

import FileManagement.GIFSaver;
import GameOfLife.EditorController;
import GameOfLife.GameOfLife;
import java.io.File;
import GameOfLife.MetaData;
import GameOfLife.ViewController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;

public class DialogBoxes {
    
    private final ViewController viewControllerReference;
    private Stage mainStage;
    
    public DialogBoxes(ViewController viewController) {
        this.viewControllerReference = viewController;
    }
    
	/**
     * Static function with the purpose of "throwing" dialog boxes
     * @param title The title of the dialog box
     * @param headerText The header text of the dialog box
     * @param contentText The content text of the dialog box
     */
    
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public static void infoBox(String title, String headerText, String contentText) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setHeaderText(headerText);
    	alert.setContentText(contentText);
    	alert.showAndWait();
    }

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
    
    public File fileChooser(List<ExtensionFilter> extFilter, boolean load) {
        Stage mainStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(extFilter);
        
        if(load) {
            return fileChooser.showOpenDialog(mainStage);
        } else {
            return fileChooser.showSaveDialog(mainStage);
        }
    }

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

    public int[] openNewGameDialog() {
        int[] array = new int[2];
        
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 150, 10, 10));
        root.setHgap(10);
        root.setVgap(10);
        Stage stage = new Stage();
        
        Label rowLabel = new Label("Rows: ");
        Label columnLabel = new Label("Columns: ");
        GridPane.setConstraints(rowLabel, 0, 0, 1, 1);
        GridPane.setConstraints(columnLabel, 0, 1, 1, 1);
        root.getChildren().addAll(rowLabel, columnLabel);
        
        Spinner rows = new Spinner(1, 100000, 1, 1);
        Spinner columns = new Spinner(1, 100000, 1, 1);
        GridPane.setConstraints(rows, 1, 0, 3, 1);
        GridPane.setConstraints(columns, 1, 1, 3, 1);
        root.getChildren().addAll(rows, columns);
        
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
           array[0] = (int)rows.getValue();
           array[1] = (int)columns.getValue();
           stage.close();
        });
        
        
        cancelButton.setOnAction(e -> {
            array[0] = 0;
            array[1] = 0;
            stage.close();
        });
        
        Scene scene = new Scene(root, 560, 150);

        stage.setScene(scene);
        stage.showAndWait();
        
        return array;
    }

    public void openPatternEditor(GameOfLife game) {
        Stage editor = new Stage();
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(mainStage);
        
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("PatternEditor.fxml"));
            
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
        final Spinner spinnerWidth = new Spinner(10, 10000, 50, 10);
        spinnerWidth.setEditable(true);
        
        final Spinner spinnerHeight = new Spinner(10, 10000, 50, 10);
        spinnerHeight.setEditable(true);
        
        GridPane.setConstraints(spinnerWidth, 1, 0, 3, 1);
        GridPane.setConstraints(spinnerHeight, 1, 1, 3, 1);
        root.getChildren().addAll(spinnerWidth, spinnerHeight);
        
        final Label saveToLabel = new Label("Save To: ");
        final TextField saveToField = new TextField();
        saveToField.setEditable(false);
        final Button saveToButton = new Button("Browse");
                
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
        final Button cancelButton = new Button("Cancel");
        
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        hbox.getChildren().addAll(saveButton, cancelButton);
        root.getChildren().add(hbox);
        
        GridPane.setConstraints(hbox, 1, 8);
        
        saveToButton.setOnAction(e -> {
            List<ExtensionFilter> extFilter = new ArrayList<>();
            extFilter.add(new ExtensionFilter("GIF files", "*.gif"));
            extFilter.add(new ExtensionFilter("All Files", "*.*"));
            saveToField.setText(fileChooser(extFilter, false).getAbsolutePath());
        });
        
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(e -> {
            boolean error = false;
            try {
                errorLabel1.setText("");
                Integer.parseInt(spinnerWidth.getEditor().textProperty().get());
            } catch (NumberFormatException nfE) {
                error = true;
               errorLabel1.setText("You must enter an integer.");
               spinnerWidth.getEditor().textProperty().set(INITIAL_VALUE);
            }
            
            try {
                errorLabel2.setText("");
                Integer.parseInt(spinnerHeight.getEditor().textProperty().get());
            } catch (NumberFormatException nfE) {
                error = true;
                errorLabel2.setText("You must enter an integer.");
                spinnerHeight.getEditor().textProperty().set(INITIAL_VALUE);
            }
            
            File file = new File(saveToField.getText());
            if (!file.isFile() && !file.canRead()) {
                error = true;
            }
            
            if (!error) {
                saver.setWidth((int) spinnerWidth.getValue());
                saver.setHeight((int) spinnerHeight.getValue());
                saver.setColors(convertFXColorToAWTColor(backgroundColor.getValue()),
                    convertFXColorToAWTColor(aliveCellColor.getValue()), 
                    convertFXColorToAWTColor(deadCellColor.getValue()));
                saver.setFile(file);
                saver.saveToGif();
                stage.close();
            }
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });
        
        Scene scene = new Scene(root, 630, 400);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    private java.awt.Color convertFXColorToAWTColor(javafx.scene.paint.Color c) {
        int r = (int) c.getRed();
        int g = (int) c.getGreen();
        int b = (int) c.getBlue();
        
        return new java.awt.Color(r, g, b);
    }
}