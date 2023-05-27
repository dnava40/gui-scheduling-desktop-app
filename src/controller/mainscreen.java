package controller;

import helper.DAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;


public class mainscreen implements Initializable {
    public TableView appointmentsTable;
    public TableColumn appointmentIdColumn;
    public TableColumn titleColumn;
    public TableColumn descriptionColumn;
    public TableColumn locationColumn;
    public TableColumn typeColumn;
    public TableColumn startColumn;
    public TableColumn endColumn;
    public TableColumn createdColumn;
    public TableColumn createdByColumn;
    public TableColumn updatedColumn;
    public TableColumn updatedByColumn;
    public TableColumn customerIdColumn;
    public TableColumn userIdColumn;
    public TableColumn contactIdColumn;
    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public Button customerDirectoryButton;
    public ObservableList<appointment> weekView = FXCollections.observableArrayList();
    public ObservableList<appointment> monthView = FXCollections.observableArrayList();
    public static appointment passedAppointment;
    public Button logOutButton;
    public RadioButton viewAllButton;
    public ToggleGroup radioButtons;
    public RadioButton viewWeekButton;
    public RadioButton viewMonthButton;
    public Button generateReportsButton;

    /**
     * @return Appointent to be passed to the Modify Appointment form.
     */
    public static appointment passAppointment() {
        return passedAppointment;
    }

    /**
     * Opens the Add Appointment form.
     *
     * @throws IOException
     */
    public void addAppointmentAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gets appointment selected in the Appointment TableView and passes it to the Modify Appointment form.
     *
     * @throws IOException
     */
    public void modifyAppointmentAction(ActionEvent actionEvent) throws IOException {
        if (appointmentsTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No appointment is selected for modification!");
            alert.showAndWait();
            return;
        }
        passedAppointment = (appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Modify Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes appointment that is currently selected in the Appointment TableView from the database.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void deleteAppointmentAction(ActionEvent actionEvent) throws SQLException, IOException {
        if (appointmentsTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No appointment is selected for deletion!");
            alert.showAndWait();
            return;
        }
        appointment selectedAppointment = (appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        DAO.deleteAppointment(selectedAppointment.getAppointmentId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted Appointment  ID: " + selectedAppointment.getAppointmentId() + " Title: " + selectedAppointment.getTitle());
        alert.showAndWait();
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the Customer Directory Window.
     *
     * @throws IOException
     */
    public void customerActionButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Runs upon the window opening. Gets all appointments from the database and displays those appointements in the Appointment TableView.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsTable.setItems(DAO.getAllAppointments());
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        updatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        updatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        LocalDateTime weekFromNow = LocalDateTime.now().plusWeeks(1);
        LocalDateTime monthFromNow = LocalDateTime.now().plusMonths(1);
        for (appointment s : DAO.getAllAppointments()) {
            if (s.getStartTime().isAfter(LocalDateTime.now()) && s.getStartTime().isBefore(weekFromNow)) {
                weekView.add(s);
            }
            if (s.getStartTime().isAfter(LocalDateTime.now()) && s.getStartTime().isBefore(monthFromNow)) {
                monthView.add(s);
            }
        }
    }

    /**
     * Closes the connection to the database and returns to the login screen.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void logOutAction(ActionEvent actionEvent) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login Window");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays all appointments in the TableView.
     */
    public void allSelected(ActionEvent actionEvent) {
        appointmentsTable.setItems(DAO.getAllAppointments());
    }

    /**
     * Displays only appointments within the next week in the TableView.
     */
    public void weekSelected(ActionEvent actionEvent) {
        appointmentsTable.setItems(weekView);
    }

    /**
     * Displays only appointments within the next month in the TableView.
     */
    public void monthSelected(ActionEvent actionEvent) {
        appointmentsTable.setItems(monthView);
    }

    /**
     * Opens the Generate Reports Window.
     *
     * @throws IOException
     */
    public void generateReportsAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/generateReports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Generate Reports");
        stage.setScene(scene);
        stage.show();
    }
}
