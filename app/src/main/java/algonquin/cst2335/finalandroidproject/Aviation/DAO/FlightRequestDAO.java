package algonquin.cst2335.finalandroidproject.Aviation.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.finalandroidproject.Aviation.Data.FlightRequest;


@Dao
public interface FlightRequestDAO {

        @Insert
        long insertCode(FlightRequest m);
        @Query("Select * from FlightRequest")
        List<FlightRequest> getAllMessage();
        @Delete
        void deleteCode(FlightRequest m);

    }


