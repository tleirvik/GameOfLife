package util;

import java.io.File;
import java.util.Optional;
import GameOfLife.MetaData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

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
                        System.out.println(urlString.toString());
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
    
    public File saveRLEDialogBox() {
        Stage mainStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save RLE Pattern to file");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("RLE files", "*.rle"));
        File saveRLEFile = fileChooser.showSaveDialog(mainStage);
        return saveRLEFile;
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
        Stage stage = new Stage();
        
        Label label1 = new Label("Columns: ");
        Label label2 = new Label("Rows: ");
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(label2, 0, 1);
        root.getChildren().addAll(label1, label2);
        
        Label errorLabel1 = new Label();
        Label errorLabel2 = new Label();
        GridPane.setConstraints(errorLabel1, 2, 0);
        GridPane.setConstraints(errorLabel2, 2, 1);
        root.getChildren().addAll(errorLabel1, errorLabel2);
        
        TextField inputField1 = new TextField();
        TextField inputField2 = new TextField();
        
        GridPane.setConstraints(inputField1, 1, 0);
        GridPane.setConstraints(inputField2, 1, 1);
        root.getChildren().addAll(inputField1, inputField2);
        
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(okButton, cancelButton);
        root.getChildren().add(hbox);
        
        GridPane.setConstraints(hbox, 1, 3);
        
        okButton.setOnAction(e -> {
            boolean error = false;
            try {
                errorLabel1.setText("");
                array[0] = Integer.parseInt(inputField1.getText());
                
            } catch (NumberFormatException nfE) {
                error = true;
               errorLabel1.setText("You must enter an integer.");
               
            }
            try {
                errorLabel2.setText("");
                array[1] = Integer.parseInt(inputField2.getText());
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
        
        Scene scene = new Scene(root, 400, 150);
        stage.setScene(scene);
        stage.showAndWait();
        
        
        return array;
    }
}
