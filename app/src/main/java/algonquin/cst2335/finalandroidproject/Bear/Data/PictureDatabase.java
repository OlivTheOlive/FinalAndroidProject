package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BearPicture.class}, version = 1)
public abstract class PictureDatabase extends RoomDatabase {
    public abstract BearPictureDAO bpDAO();

}
