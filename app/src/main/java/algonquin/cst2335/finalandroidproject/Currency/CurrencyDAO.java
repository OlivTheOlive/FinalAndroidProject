package algonquin.cst2335.finalandroidproject.Currency;

import android.icu.util.CurrencyAmount;

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
    public List<CurrencyAmount> getAllAmount();

    @Delete
    public Integer deleteAmount(CurrencySelected amount);
}
