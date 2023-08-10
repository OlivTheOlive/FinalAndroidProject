package algonquin.cst2335.finalandroidproject.Aviation.Data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.finalandroidproject.Aviation.DAO.FlightRequestDAO;

/**
 * A Room database class that defines the database structure and provides access to DAOs.
 *
 * @version 1
 */
@Database(entities = {FlightRequest.class}, version = 1)
public abstract class FlightRequestDB extends RoomDatabase {
    /**
     * Provides access to the FlightRequestDAO for database operations.
     *
     * @return The FlightRequestDAO instance.
     */
    public abstract FlightRequestDAO frDAO();
}
