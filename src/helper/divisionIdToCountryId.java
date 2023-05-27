package helper;

import java.sql.SQLException;

public interface divisionIdToCountryId {
    /**
     * Functional Interface for Lambda expression that returns country ID given a First Level Division ID.
     *
     * @param divisionId The Division ID to be converted into a Country ID.
     * @return An int representing the Country ID returned.
     * @throws SQLException
     */
    int convert(int divisionId) throws SQLException;
}
