package controller;

import helper.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.appointment;
import model.contact;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class generateReports {
    public Button monthTypeButton;
    public Button contactButton;
    public Button appointmentLength;
    public TextArea reportWindow;
    public Button exitButton;
    public ComboBox<String> monthComboBox;
    public ComboBox<String> typeComboBox;
    ObservableList<appointment> appointmentList = DAO.getAllAppointments();
    ObservableList<contact> contactsList = DAO.getAllContacts();
    ObservableList<String> monthList = FXCollections.observableArrayList();
    ObservableList<String> typeList = FXCollections.observableArrayList();
    ArrayList<appointment> currentMonth = new ArrayList<appointment>();


    /**
     * When a report is generated by Month and Type of Appointment, the report window is cleared and the combo Boxes for selecting month and appointment type are made visible. The month Combo Box is populated with Strings representing the months of the year.
     */
    public void monthTypeAction(ActionEvent actionEvent) {
        reportWindow.clear();
        monthComboBox.setVisible(true);
        typeComboBox.setVisible(true);
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        monthComboBox.setItems(monthList);
    }

    /**
     * When a report is generated for every contact in the organization, the month and type combo boxes are made invisible. A new string is generated and displayed in the report window.
     */
    public void contactAction(ActionEvent actionEvent) {
        monthComboBox.setVisible(false);
        typeComboBox.setVisible(false);
        reportWindow.clear();
        String displayString = "";
        for (contact c : contactsList) {
            displayString = displayString.concat(c.getContactId() + ": " + c.getContactName() + "\n");
            for (appointment a : appointmentList) {
                if (c.getContactId() == a.getContactId()) {
                    displayString = displayString.concat(a.getAppointmentId() + " " + a.getTitle() + " " + a.getType() + " " + a.getDescription() + " " + a.getStartTime() + " " + a.getEndTime() + " " + a.getCustomerId() + "\n");
                }
            }
        }
        reportWindow.setText(displayString);
    }

    /**
     * Generates a report based on how long each appointment in the database is. Sets Month and Type combo boxes to be invisible. Generates a string to be displayed in the report window.
     */
    public void appointmentLengthAction(ActionEvent actionEvent) {
        monthComboBox.setVisible(false);
        typeComboBox.setVisible(false);
        reportWindow.clear();
        String displayString = "";
        ArrayList<appointment> fifteenMinutesOrLess = new ArrayList<appointment>();
        ArrayList<appointment> oneHourOrLess = new ArrayList<appointment>();
        ArrayList<appointment> twoHoursOrLess = new ArrayList<appointment>();
        ArrayList<appointment> moreThanTwoHours = new ArrayList<appointment>();
        for (appointment a : appointmentList) {
            if (ChronoUnit.MINUTES.between(a.getStartTime(), a.getEndTime()) <= 15) {
                fifteenMinutesOrLess.add(a);
            } else if (ChronoUnit.MINUTES.between(a.getStartTime(), a.getEndTime()) <= 60) {
                oneHourOrLess.add(a);
            } else if (ChronoUnit.MINUTES.between(a.getStartTime(), a.getEndTime()) <= 120) {
                twoHoursOrLess.add(a);
            } else {
                moreThanTwoHours.add(a);
            }
        }
        displayString = displayString.concat("Appointments fifteen minutes long or less:\n");
        for (appointment a : fifteenMinutesOrLess) {
            displayString = displayString.concat(a.getAppointmentId() + " " + a.getTitle() + " " + a.getType() + " " + a.getDescription() + " " + a.getStartTime() + " " + a.getEndTime() + " " + a.getCustomerId() + "\n");
        }
        displayString = displayString.concat("Appointments one hour long or less:\n");
        for (appointment a : oneHourOrLess) {
            displayString = displayString.concat(a.getAppointmentId() + " " + a.getTitle() + " " + a.getType() + " " + a.getDescription() + " " + a.getStartTime() + " " + a.getEndTime() + " " + a.getCustomerId() + "\n");
        }
        displayString = displayString.concat("Appointments two hours long or less:\n");
        for (appointment a : twoHoursOrLess) {
            displayString = displayString.concat(a.getAppointmentId() + " " + a.getTitle() + " " + a.getType() + " " + a.getDescription() + " " + a.getStartTime() + " " + a.getEndTime() + " " + a.getCustomerId() + "\n");
        }
        displayString = displayString.concat("Appointments more than two hours long:\n");
        for (appointment a : moreThanTwoHours) {
            displayString = displayString.concat(a.getAppointmentId() + " " + a.getTitle() + " " + a.getType() + " " + a.getDescription() + " " + a.getStartTime() + " " + a.getEndTime() + " " + a.getCustomerId() + "\n");
        }
        reportWindow.setText(displayString);
    }

    /**
     * Closes the current window and returns to the Appointment Directory.
     *
     * @throws IOException
     */
    public void exitAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * When month is selected, populates TypeList with correct list of appointment types for the given month.
     */
    public void monthAction(ActionEvent actionEvent) {
        typeList.clear();
        currentMonth.clear();
        reportWindow.clear();
        for (appointment a : appointmentList) {
            if ((a.getStartTime().getMonth().toString()).equals(monthComboBox.getValue().toUpperCase())) {
                currentMonth.add(a);
            }
        }
        for (appointment a : currentMonth) {
            if (!typeList.contains(a.getType())) {
                typeList.add(a.getType());
            }
        }
        typeComboBox.setItems(typeList);
    }

    /**
     * When appointment type is selected, creates a new string based on month and appointment type selected, and displays it in the report window.
     */
    public void typeAction(ActionEvent actionEvent) {
        String displayString = "";
        int typeCount = 0;
        for (appointment a : currentMonth) {
            if (a.getType().equals(typeComboBox.getValue())) {
                typeCount++;
            }
        }
        displayString = monthComboBox.getValue() + " - " + typeComboBox.getValue() + " - " + typeCount;
        reportWindow.setText(displayString);
    }
}
