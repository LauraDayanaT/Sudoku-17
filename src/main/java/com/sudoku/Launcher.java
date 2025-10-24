package com.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the Sudoku game application.
 */
public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sudoku/sudo.fxml")); // ✅ ruta corregida
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku 6x6");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
