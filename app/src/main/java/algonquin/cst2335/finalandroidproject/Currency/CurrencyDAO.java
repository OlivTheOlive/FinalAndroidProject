package algonquin.cst2335.finalandroidproject.Currency;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


/**
 * Data Access Object (DAO) interface for interacting with the currency conversion history database.
 */
@Dao
public interface CurrencyDAO {


    /**
     * Insert a new currency conversion amount into the database.
     *
     * @param amount The CurrencySelected object representing the conversion amount.
     * @return The row ID of the newly inserted currency conversion amount.
     */
    @Insert
    public long insertAmount(CurrencySelected amount);

    /**
     * Update an existing currency conversion amount in the database.
     *
     * @param amount The CurrencySelected object representing the updated conversion amount.
     * @return The number of rows affected by the update operation.
     */
    @Update
    public int updateAmount(CurrencySelected amount);

    /**
     * Retrieve all currency conversion amounts from the database.
     *
     * @return A List containing all CurrencySelected objects representing the conversion amounts.
     */
    @Query("Select * from CurrencySelected")
       public List<CurrencySelected> getAllAmount();

    /**
     * Delete a currency conversion amount from the database.
     *
     * @param amount The CurrencySelected object representing the conversion amount to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    @Delete
    public int deleteAmount(CurrencySelected amount);


}
