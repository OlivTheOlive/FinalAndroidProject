package algonquin.cst2335.finalandroidproject.Aviation.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequest;

/**
 * Data Access Object (DAO) interface for interacting with FlightRequest entities in the database.
 */
@Dao
public interface FlightRequestDAO {

        /**
         * Insert a FlightRequest object into the database.
         *
         * @param m The FlightRequest object to insert.
         * @return The ID of the inserted row.
         */
        @Insert
        long insertCode(FlightRequest m);

        /**
         * Retrieve all FlightRequest objects from the database.
         *
         * @return A list of FlightRequest objects.
         */
        @Query("Select * from FlightRequest")
        List<FlightRequest> getAllMessage();

        /**
         * Delete a FlightRequest object from the database.
         *
         * @param m The FlightRequest object to delete.
         */
        @Delete
        void deleteCode(FlightRequest m);

        /**
         * Check if a flight ID exists in the database.
         *
         * @param flightId The flight ID to check.
         * @return True if the flight ID exists, otherwise false.
         */
        @Query("SELECT EXISTS(SELECT 1 FROM FlightRequest WHERE flightId = :flightId LIMIT 1)")
        boolean isFlightIdExists(String flightId);
}



