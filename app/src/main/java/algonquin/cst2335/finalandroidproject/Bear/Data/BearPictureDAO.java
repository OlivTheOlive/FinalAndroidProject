package algonquin.cst2335.finalandroidproject.Bear.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for managing BearPicture entities in the Database
 */
@Dao
public interface BearPictureDAO {


    /**
     * Inserts a new BearPicture entity into the database
     * @param bp the BearPicture object to insert
     *
     * @return the ID of the inserted BearPicture
     */
    @Insert
    public long insertPicture(BearPicture bp);

    /**
     * Retrieves all BearPictures from the Database
     *
     * @return all BearPictures objects
     */
    @Query("Select * from BearPicture")
    public List<BearPicture> getAllPictures();

    /**
     * Deletes a BearPicture from the Database
     * @param bp the BearPicture to be deleted
     */
    @Delete
    void deletePicture(BearPicture bp);
}
