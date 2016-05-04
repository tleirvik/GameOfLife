/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Robin
 */
public class HelpController {
    
    //=========================================================================
    // JavaFX Fields
    //=========================================================================
    @FXML private ListView listView;
    @FXML private WebView webView;
    
    private WebEngine webEngine;
    
    public void initializeHelp() {
        webEngine = webView.getEngine();
        initializeListView();
    }
    
    private void initializeListView() {
        /*
        About/Credits
        How to Play
        Pattern Editor
        Statistics
        ...
        */
        ObservableList<String> subjects = FXCollections.observableArrayList(
                "About / Credits","How To Play", "Pattern Editor", "Statistics"
        );
        listView.setItems(subjects);
        
        listView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                    webEngine.load(getSubjectURL(new_val));
            }
        });
        listView.getSelectionModel().select(0);
    }
    
    private String getSubjectURL(String subject) {
        String subjectFileName = "AboutCredits.html";
        switch(subject) {
            case "About / Credits":
                subjectFileName = "AboutCredits.html";
                break;
            case "How To Play":
                subjectFileName = "HowToPlay.html";
                break;
            case "Pattern Editor":
                subjectFileName = "PatternEditor.html";
                break;
            case "Statistics":
                subjectFileName = "Statistics.html";
                break;
        }
        return getClass().getResource("/Model/util/help/" + subjectFileName).toExternalForm();
    }
    
}
