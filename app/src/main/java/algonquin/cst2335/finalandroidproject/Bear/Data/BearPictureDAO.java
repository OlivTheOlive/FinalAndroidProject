package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BearPictureDAO {

    @Insert
    public long insertPicture(BearPicture bp);

    @Query("Select * from BearPicture")
    public List<BearPicture> getAllPictures();

    @Delete
    void deletePicture(BearPicture bp);
}
