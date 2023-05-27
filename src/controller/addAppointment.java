package controller;

import helper.DAO;
import helper.getNextAppointmentId;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.contact;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static helper.JDBC.getConnection;


public class addAppointment implements Initializable {


    public TextField appointmentIdField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField appointmentTypeField;
    public TextField customerIdField;
    public TextField userIdField;
    public Button createAppointmentButton;
    public Button cancelButton;
    public ComboBox contactComboBox;
    public TextField startHourField;
    public TextField endHourField;
    public TextField startMinuteField;
    public TextField endMinuteField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;

    /**
     * Creates an appointment in the database using values obtained from the text-fields in the form.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void createAppointmentAction(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = appointmentTypeField.getText();
            LocalTime sTime = LocalTime.parse(startHourField.getText() + ":" + startMinuteField.getText());
            LocalDate sDate = startDatePicker.getValue();
            LocalDateTime start = LocalDateTime.of(sDate, sTime);
            LocalTime eTime = LocalTime.parse(endHourField.getText() + ":" + endMinuteField.getText());
            LocalDate eDate = endDatePicker.getValue();
            LocalDateTime end = LocalDateTime.of(eDate, eTime);
            int customerId = Integer.parseInt(customerIdField.getText());
            int userId = Integer.parseInt(userIdField.getText());
            contact selectedContact = (contact) contactComboBox.getValue();
            int contactId = selectedContact.getContactId();

            if (DAO.validateAppointment(start, end, customerId)) {
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start or End of appointment collides with business hours or existing appointments for selected customer. Choose a new start time and try again.");
                alert.showAndWait();
                return;
            }


            int rowsAffected = DAO.addAppointment(title, description, location, type, start, end, customerId, userId, contactId);
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Created New Appointment");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Creating New Appointment\n Check your inputted values and try again.");
            alert.showAndWait();
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the window and returns to the Appointment directory.
     *
     * @throws IOException
     */
    public void cancelAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Function ran upon loading the window. Utilizes lambda function to obtain the next appointment ID that will be assigned to the appointment when created in the database.
     * Also sets the contact Combo Box to use the correct items.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getNextAppointmentId getId = () -> {
            int nextAppointmentId = 0;
            Statement stmt = getConnection().createStatement();
            String query = "SELECT `auto_increment` FROM INFORMATION_SCHEMA.TABLES WHERE table_name = 'appointments'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                nextAppointmentId = rs.getInt("AUTO_INCREMENT");
            }
            return nextAppointmentId;
        };
        try {
            appointmentIdField.setText(Integer.toString(getId.getNextAppointmentId()));
            contactComboBox.setItems(DAO.getAllContacts());
            contactComboBox.getSelectionModel().select(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
