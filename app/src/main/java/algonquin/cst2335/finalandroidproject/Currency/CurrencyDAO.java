package algonquin.cst2335.finalandroidproject.Currency;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
public interface CurrencyDAO {

    @Insert
    public long insertAmount(CurrencySelected amount);

    @Update
    public Integer updateAmount(CurrencySelected amount);

    @Query("Select * from CurrencySelected")
    public List<CurrencySelected> getAllAmount();

    @Delete
    public Integer deleteAmount(CurrencySelected amount);
}
