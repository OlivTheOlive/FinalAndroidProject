package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CurrencySelected.class}, version = 6, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    public abstract CurrencyDAO cDAO();

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Add the new column 'conversionResult' to 'CurrencySelected' table
            database.execSQL("ALTER TABLE CurrencySelected ADD COLUMN conversionResult TEXT");
        }
    };

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "currencyDatabase")
                    .addMigrations(MIGRATION_5_6) // Add the migration here
                    .build();
        }
        return instance;
    }
}

