package controller;

import helper.DAO;
import helper.divisionIdToCountryId;
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
import model.customer;
import model.firstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static helper.JDBC.getConnection;


public class modifyCustomer implements Initializable {

    public TextField customerIdField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox<country> countryComboBox;
    public ComboBox<firstLevelDivision> firstLevelComboBox;
    public Button cancelButton;
    public Button modifyCustomerButton;

    /**
     * When country is selected, sets first level divisions combo Box to the correct values.
     *
     * @throws SQLException
     */
    public void countrySelected(ActionEvent actionEvent) throws SQLException {
        firstLevelComboBox.setItems(DAO.getCertainFirstLevelDivisions(countryComboBox.getValue().getCountryId()));
    }

    /**
     * Closes the current window and returns to the Customer Directory.
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
     * Runs upon loading the window. Sets all textFields and combo Boxes according to the customer that was selected in the previous screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer caughtCustomer = customerDirectory.passCustomer();
        customerIdField.setText(Integer.toString(caughtCustomer.getCustomerId()));
        nameField.setText(caughtCustomer.getName());
        addressField.setText(caughtCustomer.getAddress());
        postalCodeField.setText(caughtCustomer.getPostalCode());
        phoneNumberField.setText(caughtCustomer.getPhoneNumber());
        divisionIdToCountryId converter = (s) -> {
            int countryId = 0;
            Statement stmt = getConnection().createStatement();
            String query = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + s;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                countryId = rs.getInt("Country_ID");
            }
            return countryId;
        };
        try {
            countryComboBox.setItems(DAO.getAllCountries());
            firstLevelComboBox.setItems(DAO.getCertainFirstLevelDivisions(converter.convert(caughtCustomer.getDivisionId())));
            for (country c : countryComboBox.getItems()) {
                if (converter.convert(caughtCustomer.getDivisionId()) == c.getCountryId()) {
                    countryComboBox.setValue(c);
                    break;
                }
            }
            for (firstLevelDivision d : firstLevelComboBox.getItems()) {
                if (caughtCustomer.getDivisionId() == d.getDivisionId()) {
                    firstLevelComboBox.setValue(d);
                    break;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modifies the customer in the databse according to the predetermined Customer ID and the values found in the textFields and Combo Boxes in the form.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void modifyCustomerAction(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            int customerId = Integer.parseInt(customerIdField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            int divisionId = firstLevelComboBox.getValue().getDivisionId();
            int rowsAffected = DAO.modifyCustomer(customerId, name, address, postalCode, phoneNumber, divisionId);
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Modified Customer");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error Modifying Customer\n Check your inputted values and try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Modifying Customer \n Check your inputted values and try again.");
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
}
