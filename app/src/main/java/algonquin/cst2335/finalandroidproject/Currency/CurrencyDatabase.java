package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CurrencySelected.class}, version = 3)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CurrencyDatabase.class,
                    "currency_database"
            ).build();
        }
        return instance;
    }

    public abstract CurrencyDAO cDAO();

    // Add the migration here
    static final Migration MIGRATION_1_2 = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Write your migration SQL code here.
            // For example, if you added a new column 'newColumn' to 'CurrencySelected' table:
            // database.execSQL("ALTER TABLE CurrencySelected ADD COLUMN newColumn TEXT");
            database.execSQL("ALTER Table CurrencySelected Add COLUMN amountConverted  TEXT");
        }
    };
}
