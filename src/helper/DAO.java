package helper;

import controller.login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static controller.login.loggedInUser;
import static helper.JDBC.getConnection;

public abstract class DAO {
    /**
     * @return An ObservableList of all appointments in the database.
     */
    public static ObservableList<appointment> getAllAppointments() {
        ObservableList<appointment> appointmentList = FXCollections.observableArrayList();
        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM appointments";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String Title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createTime = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdated = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                appointment newAppointment = new appointment(appointmentId, Title, description, location, type, start, end, createTime, createdBy, lastUpdated, lastUpdatedBy, customerId, userId, contactId);
                appointmentList.add(newAppointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentList;
    }

    /**
     * @return An ObservableList of all customers in the database.
     */
    public static ObservableList<customer> getAllCustomers() {
        ObservableList<customer> customerList = FXCollections.observableArrayList();
        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM customers";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                LocalDateTime createTime = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divisionId = rs.getInt("Division_ID");
                customer newCustomer = new customer(customerId, customerName, address, postalCode, phoneNumber, createTime, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                customerList.add(newCustomer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    /**
     * @return An ObservableList of all contacts in the database.
     */
    public static ObservableList<contact> getAllContacts() {
        ObservableList<contact> contactsList = FXCollections.observableArrayList();
        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM contacts";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contact newContact = new contact(contactId, contactName, email);
                contactsList.add(newContact);
            }
        } catch (SQLException e) {

        }
        return contactsList;
    }

    /**
     * @return An ObservableList of all countries in the database.
     * @throws SQLException
     */
    public static ObservableList<country> getAllCountries() throws SQLException {
        ObservableList<country> countriesList = FXCollections.observableArrayList();
        Statement stmt = getConnection().createStatement();
        String query = "SELECT * FROM countries";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String name = rs.getString("Country");
            country newCountry = new country(countryId, name);
            countriesList.add(newCountry);
        }
        return countriesList;
    }

    /**
     * @param countryId The country ID to be used to select the first level divisions.
     * @return An ObservableList of the first level divisions pertaining to the country ID that is passed as a parameter.
     * @throws SQLException
     */
    public static ObservableList<firstLevelDivision> getCertainFirstLevelDivisions(int countryId) throws SQLException {
        ObservableList<firstLevelDivision> firstLevelDivisionsList = FXCollections.observableArrayList();
        Statement stmt = getConnection().createStatement();
        String query = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryId;
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String name = rs.getString("Division");
            int cId = rs.getInt("Country_ID");
            firstLevelDivision newFirstLevelDivision = new firstLevelDivision(divisionId, name, cId);
            firstLevelDivisionsList.add(newFirstLevelDivision);
        }
        return firstLevelDivisionsList;
    }

    /**
     * @param title       The title of the appointment.
     * @param description The description of the appointment
     * @param location    The location of the appointment.
     * @param type        The type of the appointment.
     * @param start       The start time of the appointment.
     * @param end         The end time of the appointment.
     * @param customerId  The ID of the customer in the appointment.
     * @param userId      The ID of the user who set up the appointment.
     * @param contactId   The ID of the contact in the appointment.
     * @return An integer representing the amount of rows that were affected by the appointment creation.
     * @throws SQLException
     */
    public static int addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
        String statement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, loggedInUser.getUsername());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(10, loggedInUser.getUsername());
        ps.setInt(11, customerId);
        ps.setInt(12, userId);
        ps.setInt(13, contactId);
        try {
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating new appointment. Check your inputted values and try again.");
            alert.showAndWait();
            return 0;
        }
    }

    /**
     * @param name        The name of the customer.
     * @param address     The address of the customer.
     * @param postalCode  The postal code of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param divisionId  The division ID in which the customer resides.
     * @return An integer representing the amount of rows affected by the customer creation.
     * @throws SQLException
     */
    public static int addCustomer(String name, String address, String postalCode, String phoneNumber, int divisionId) throws SQLException {
        String statement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, loggedInUser.getUsername());
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, loggedInUser.getUsername());
        ps.setInt(9, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param appointmentId The ID of the appointment.
     * @param title         The title of the appointment.
     * @param description   The description of the appointment
     * @param location      The location of the appointment.
     * @param type          The type of the appointment.
     * @param start         The start time of the appointment.
     * @param end           The end time of the appointment.
     * @param customerId    The ID of the customer in the appointment.
     * @param userId        The ID of the user who set up the appointment.
     * @param contactId     The ID of the contact in the appointment.
     * @return An integer representing the amount of rows that were affected by the appointment modification.
     * @throws SQLException
     */
    public static int modifyAppointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
        String statement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(start));
        ps.setTimestamp(6, Timestamp.valueOf(end));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, loggedInUser.getUsername());
        ps.setInt(9, customerId);
        ps.setInt(10, userId);
        ps.setInt(11, contactId);
        ps.setInt(12, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param name        The name of the customer.
     * @param address     The address of the customer.
     * @param postalCode  The postal code of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param divisionId  The division ID in which the customer resides.
     * @return An integer representing the amount of rows affected by the customer modification.
     * @throws SQLException
     */
    public static int modifyCustomer(int customerId, String name, String address, String postalCode, String phoneNumber, int divisionId) throws SQLException {
        String statement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, loggedInUser.getUsername());
        ps.setInt(7, divisionId);
        ps.setInt(8, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param appointmentId The ID of the appointment that is to be deleted.
     * @return An integer representing the amount of rows affected by the appointment deletion.
     * @throws SQLException
     */
    public static int deleteAppointment(int appointmentId) throws SQLException {
        String statement = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param customerId The ID of the customer to be deleted.
     * @return An integer representing the amount of rows affected by the customer deletion.
     * @throws SQLException
     */
    public static int deleteCustomer(int customerId) throws SQLException {
        String statement = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = getConnection().prepareStatement(statement);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * @param startTime  The start time of the appointment to be validated.
     * @param endTime    The end time of the appointment to be validated.
     * @param customerId The customer ID the appointments are to be validated against.
     * @return A boolean value, true if the appointment was successfully validated or false if the appointment was not validated.
     * @throws SQLException
     */
    public static boolean validateAppointment(LocalDateTime startTime, LocalDateTime endTime, int customerId) throws SQLException {
        ZonedDateTime zonedStart = ZonedDateTime.of(startTime, ZoneId.systemDefault());

        if ((zonedStart.withZoneSameInstant(ZoneId.of("America/New_York")).getHour() < 8) || (zonedStart.withZoneSameInstant(ZoneId.of("America/New_York")).getHour() >= 22)) {
            return false;
        }
        Statement stmt = getConnection().createStatement();
        String query = "SELECT Start, End FROM appointments WHERE Customer_ID = " + customerId;
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            return true;
        }
        while (rs.next()) {
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            if ((startTime.isAfter(start) && startTime.isBefore(end)) || (endTime.isAfter(start) && endTime.isBefore(end)) || (startTime.isEqual(start)) || (endTime.isEqual(end))) {
                return false;
            }
        }
        return true;
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        Statement stmt = getConnection().createStatement();
        String query = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            return false;
        } else {
            return true;
        }
    }

    public static user returnLoggedInUser(String username, String password) throws SQLException {
        Statement stmt = getConnection().createStatement();
        String query = "SELECT * FROM users WHERE User_Name = '" + username + "' AND Password = '" + password + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String passWord = rs.getString("Password");
            user newUser = new user(userId, userName, passWord);
            return newUser;
        }
        return null;
    }
}
