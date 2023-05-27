package controller;

import helper.DAO;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.appointment;
import model.contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class modifyAppointment implements Initializable {

    public TextField appointmentIdField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField appointmentTypeField;
    public TextField customerIdField;
    public TextField userIdField;
    public Button modifyAppointmentButton;
    public Button cancelButton;
    public ComboBox<contact> contactComboBox;
    public TextField startHourField;
    public TextField endHourField;
    public TextField startMinuteField;
    public TextField endMinuteField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    private appointment caughtAppointment = null;

    /**
     * Modifies appointment using predetermined Appointment ID and values obtained from textFields in the form.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void modifyAppointmentAction(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            int appointmentId = Integer.parseInt(appointmentIdField.getText());
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

            int rowsAffected = DAO.modifyAppointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Modified Appointment");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error Modifying Appointment\n Check your inputted values and try again.");
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
     * Closes the current window and returns to the Appointment View.
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
     * Runs upon opening the window. Sets all textFields and combo boxes to the correct value according to the appointment passed from the previous window.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caughtAppointment = mainscreen.passAppointment();
        appointmentIdField.setText(Integer.toString(caughtAppointment.getAppointmentId()));
        titleField.setText(caughtAppointment.getTitle());
        descriptionField.setText(caughtAppointment.getDescription());
        locationField.setText(caughtAppointment.getLocation());
        appointmentTypeField.setText(caughtAppointment.getType());
        startHourField.setText(caughtAppointment.getStartTime().toLocalTime().toString().substring(0, 2));
        startMinuteField.setText(caughtAppointment.getStartTime().toLocalTime().toString().substring(3, 5));
        startDatePicker.setValue(caughtAppointment.getStartTime().toLocalDate());
        endHourField.setText(caughtAppointment.getEndTime().toLocalTime().toString().substring(0, 2));
        endMinuteField.setText(caughtAppointment.getEndTime().toLocalTime().toString().substring(3, 5));
        endDatePicker.setValue(caughtAppointment.getEndTime().toLocalDate());
        customerIdField.setText(Integer.toString(caughtAppointment.getCustomerId()));
        userIdField.setText(Integer.toString(caughtAppointment.getUserId()));
        try {
            contactComboBox.setItems(DAO.getAllContacts());
            for (contact c : contactComboBox.getItems()) {
                if (caughtAppointment.getContactId() == c.getContactId()) {
                    contactComboBox.setValue(c);
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
