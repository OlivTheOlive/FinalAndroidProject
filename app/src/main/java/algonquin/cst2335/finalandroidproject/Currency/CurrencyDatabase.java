package algonquin.cst2335.finalandroidproject.Currency;

import androidx.room.Database;

@Database(entities = {CurrencySelected.class}, version = 1)
public abstract class CurrencyDatabase extends androidx.room.RoomDatabase{

    public abstract CurrencyDAO cDAO();

}
