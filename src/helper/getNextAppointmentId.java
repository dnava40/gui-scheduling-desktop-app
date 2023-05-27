package helper;

import java.sql.SQLException;

public interface getNextAppointmentId {
    /**
     * Functional Interface for Lambda Expression that returns next appointment ID in database.
     *
     * @return Int representing the next Appointment ID in the database.
     * @throws SQLException
     */
    int getNextAppointmentId() throws SQLException;
}
