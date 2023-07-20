package algonquin.cst2335.finalandroidproject.Aviation.Data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.finalandroidproject.Aviation.DAO.FlightRequestDAO;

@Database(entities = {FlightRequest.class}, version = 1)
public abstract class FlightRequestDB extends RoomDatabase {
    public abstract FlightRequestDAO frDAO();
}
