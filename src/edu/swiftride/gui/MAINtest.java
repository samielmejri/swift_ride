/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.swiftride.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author user
 */
public class MAINtest extends Application {
    
    @Override
    public void start(Stage primaryStage) {
         
        
        
        Parent root = null;
        
            try {
               Stage st = new Stage();
            FXMLLoader load = new FXMLLoader(getClass()
                    .getResource("Accident.fxml"));
             root = load.load();
            Scene sc = new Scene(root);
            
            st.setScene(sc);
            st.show();
    
    } catch (IOException ex) {
                Logger.getLogger(MAINtest.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
