package controller;

import helper.DAO;
import helper.getNextAppointmentId;
import helper.getNextCustomerId;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.country;
import model.firstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static helper.JDBC.getConnection;


public class addCustomer implements Initializable {
    public TextField customerIdField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox<country> countryComboBox;
    public ComboBox<firstLevelDivision> firstLevelComboBox;
    public Button createCustomerButton;
    public Button cancelButton;

    /**
     * When country is selected in the combo Box, sets the first level divisions combo box to the correct values.
     *
     * @throws SQLException
     */
    public void countrySelected(ActionEvent actionEvent) throws SQLException {
        firstLevelComboBox.setItems(DAO.getCertainFirstLevelDivisions(countryComboBox.getValue().getCountryId()));
    }

    /**
     * Creates a new customer in the database using values obtained from the textFields and combo Boxes in the form.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void createCustomerAction(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            int divisionId = firstLevelComboBox.getValue().getDivisionId();
            int rowsAffected = DAO.addCustomer(name, address, postalCode, phoneNumber, divisionId);
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Created New Customer");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error Creating New Customer\n Check your inputted values and try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Creating New Customer \n Check your inputted values and try again.");
            alert.showAndWait();
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the window and returns to the Customer Directory.
     *
     * @throws IOException
     */
    public void cancelAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Runs upon loading the window. Utilizes lambda expression to get next customer ID from the database. Sets items in country combo box to the correct items.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getNextCustomerId getId = () -> {
            int nextCustomerId = 0;
            Statement stmt = getConnection().createStatement();
            String query = "SELECT `auto_increment` FROM INFORMATION_SCHEMA.TABLES WHERE table_name = 'customers'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                nextCustomerId = rs.getInt("AUTO_INCREMENT");
            }
            return nextCustomerId;
        };
        try {
            customerIdField.setText(Integer.toString(getId.getNextCustomerId()));
            countryComboBox.setItems(DAO.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
