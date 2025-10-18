package com.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the Sudoku game.
 */
public class Main extends Application {

    private static final String FXML_FILE = "sudoku.fxml";
    private static final String APP_TITLE = "Sudoku 6x6 Game";

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE));
            Parent root = loader.load(); // Línea que falla si el FXML no se encuentra.

            Scene scene = new Scene(root);

            // Si el archivo CSS está en la misma carpeta que Main.java
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + FXML_FILE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}