package controller;

import helper.DAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.appointment;
import model.user;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


public class login implements Initializable {
    public Label locationText;
    public TextField usernameField;
    public TextField passwordField;
    public Button loginButton;
    public Button exitButton;
    public Label usernameText;
    public Label passwordText;
    ResourceBundle loc = ResourceBundle.getBundle("controller/loc", Locale.getDefault());
    public static user loggedInUser;

    /**
     * Runs upon opening the window. Sets location text at the bottom of the window to display current timezone.
     * Sets string to proper value based on Locale and Language settings.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationText.setText(ZoneId.systemDefault().toString());
        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                usernameText.setText(loc.getString(usernameText.getText()));
                passwordText.setText(loc.getString(passwordText.getText()));
                loginButton.setText(loc.getString("login"));
                exitButton.setText(loc.getString("exit"));
            }
        } catch (Exception e) {

        }
    }

    /**
     * Attempts to open connection with given username and password. Logs log in attempt and result in login_activity.txt file.
     * Checks for upcoming appointments and displays a message based on whether or not there are appointments within 15 minutes of logging in.
     *
     * @throws IOException
     */
    public void loginAction(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        FileWriter fw = new FileWriter("login_activity.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        if (!DAO.authenticateUser(username, password)) {
            pw.println("Failed Login at " + LocalDateTime.now().toString() + " using credentials " + username + " " + password);
            pw.close();
            Alert alert = new Alert(Alert.AlertType.ERROR, loc.getString("incorrectLoginCredentials"));
            alert.showAndWait();
            return;
        } else {
            pw.println("Successful Login at " + LocalDateTime.now().toString() + " using credentials " + username + " " + password);
            loggedInUser = DAO.returnLoggedInUser(username, password);
        }
        pw.close();
        LocalDateTime currentTime = LocalDateTime.now();
        ObservableList<appointment> allAppointments = DAO.getAllAppointments();
        ArrayList<appointment> upcomingAppointments = new ArrayList<appointment>();
        for (appointment s : allAppointments) {
            long betweenMins = ChronoUnit.MINUTES.between(currentTime, s.getStartTime());
            if ((betweenMins <= 15) && (betweenMins >= 0)) {
                upcomingAppointments.add(s);
            }
        }
        if (!upcomingAppointments.isEmpty()) {
            String displayString = "";
            for (appointment s : upcomingAppointments) {
                displayString = displayString.concat(Integer.toString(s.getAppointmentId()) + ": ");
                displayString = displayString.concat(s.getTitle() + " ");
                displayString = displayString.concat(s.getStartTime().toString());
                displayString = displayString.concat("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, loc.getString("yesUpcomingAppointments") + "\n" + displayString);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, loc.getString("noUpcomingAppointments"));
            alert.showAndWait();
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the window and exits the application.
     */
    public void exitAction(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
