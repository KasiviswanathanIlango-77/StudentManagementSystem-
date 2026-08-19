/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package smsjavafxapplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author kamui
 */
public class SMSJavaFxApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LoginPage/Login.fxml"));
        // Get screen bounds for responsive sizing
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        Scene scene = new Scene(root);
        
        // Set minimum size to prevent crushing
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(600);
        
        // Start maximized (fills 1920x1080 or any screen)
        primaryStage.setMaximized(true);
        
        primaryStage.setTitle("Student Management System Project");
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
} 
