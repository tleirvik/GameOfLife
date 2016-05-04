/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

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
    
    public void initializeHelp() {
        
        listView.get
        
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://google.com");
        
        
    }
}
