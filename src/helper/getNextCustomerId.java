package helper;

import java.sql.SQLException;

public interface getNextCustomerId {
    /**
     * Functional Interface for Lambda expression that returns next customer ID in the database.
     *
     * @return Int representing the next customer ID.
     * @throws SQLException
     */
    int getNextCustomerId() throws SQLException;
}
