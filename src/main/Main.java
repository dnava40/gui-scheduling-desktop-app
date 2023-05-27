package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;


public class Main extends Application {
    /**
     * Starts the JavaFX Application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml/"));
        stage.setTitle("Login Window");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * The main method of the program.
     */
    public static void main(String[] args) throws IOException {
        JDBC.getConnection();
        launch(args);
        JDBC.closeConnection();
    }
}