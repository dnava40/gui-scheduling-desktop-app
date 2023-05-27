package controller;

import helper.DAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static helper.JDBC.getConnection;


public class customerDirectory implements Initializable {
    public TableView customerTable;
    public TableColumn customerIdColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn postalCodeColumn;
    public TableColumn phoneNumberColumn;
    public TableColumn createTimeColumn;
    public TableColumn createdByColumn;
    public TableColumn lastUpdatedColumn;
    public TableColumn lastUpdatedByColumn;
    public TableColumn divisionIdColumn;
    public Button modifyCustomerButton;
    public Button addCustomerButton;
    public Button deleteCustomerButton;
    public static customer passedCustomer;
    public Button backToAppointmentButton;

    /**
     * @return The customer that was selected in the Customer Directory when the modify Customer button was pressed.
     */
    public static customer passCustomer() {
        return passedCustomer;
    }

    /**
     * Runs upon opening the window. Gets all customers from the database and populates the Customer TableView.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(DAO.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }

    /**
     * Passes selected customer to the modify Customer form, and opens the modify Customer form.
     *
     * @throws IOException
     */
    public void modifyCustomerAction(ActionEvent actionEvent) throws IOException {
        if (customerTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No customer is selected for modification!");
            alert.showAndWait();
            return;
        }
        passedCustomer = (customer) customerTable.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(getClass().getResource("/view/modifyCustomer.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Modify Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the add Customer form.
     *
     * @throws IOException
     */
    public void addCustomerAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes customer from the databse that is currently selected in the customer TableView.
     *
     * @throws SQLException
     * @throws IOException
     */
    public void deleteCustomerAction(ActionEvent actionEvent) throws SQLException, IOException {
        if (customerTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No customer is selected for deletion!");
            alert.showAndWait();
            return;
        }
        customer selectedCustomer = (customer) customerTable.getSelectionModel().getSelectedItem();
        Statement stmt = getConnection().createStatement();
        String query = "SELECT * FROM appointments WHERE Customer_ID = " + selectedCustomer.getCustomerId();
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Deleting Customer, All Appointments with Customer Must Be Deleted First.");
            alert.showAndWait();
            return;
        }
        int rowsAffected = DAO.deleteCustomer(selectedCustomer.getCustomerId());
        if (rowsAffected > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted " + selectedCustomer.getName());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Deleting Customer, All Appointments with Customer Must Be Deleted First.");
            alert.showAndWait();
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Directory");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the current window and returns to the Appointment Directory.
     *
     * @throws IOException
     */
    public void backToAppointmentAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Directory");
        stage.setScene(scene);
        stage.show();
    }
}
