package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class for the Pattern Editor
 */
public class HelpController {
    
    //=========================================================================
    // JavaFX Fields
    //=========================================================================
    @FXML private ListView listView;
    @FXML private WebView webView;
    
    private WebEngine webEngine;

    /**
     * Initializes the help Web View
     *
     * @see ListView
     * @see WebView
     */
    public void initializeHelp() {
        webEngine = webView.getEngine();
        initializeListView();
    }

    /**
     * Initializes the {@link ListView}
     */
    private void initializeListView() {
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

    /**
     * Returns the path to the web files
     * @param subject The subject to load
     * @return the path to the web files
     */
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
        return getClass().getResource("/model/util/help/" + subjectFileName).toExternalForm();
    }
}
