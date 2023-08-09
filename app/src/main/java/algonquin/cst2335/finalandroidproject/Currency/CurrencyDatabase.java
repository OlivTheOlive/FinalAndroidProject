package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Room Database class for managing currency conversion history data.
 */
@Database(entities = {CurrencySelected.class}, version = 6, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    /**
     * Returns an instance of the CurrencyDAO interface to interact with the database.
     *
     * @return An instance of CurrencyDAO.
     */
    public abstract CurrencyDAO cDAO();

    /**
     * Migration from version 2 to version 6 of the database schema.
     */
    static final Migration MIGRATION_2_6 = new Migration(2, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `CurrencySelected` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`conversionResult` TEXT, `time` TEXT )");
        }
    };

    /**
     * Migration from version 2 to version 7 of the database schema.
     */
    static final Migration MIGRATION_2_7 = new Migration(2, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //database.execSQL("DROP TABLE `CurrencySelected`");

            database.execSQL("CREATE TABLE IF NOT EXISTS `CurrencySelected` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`conversionResult` TEXT, `time` TEXT )");
        }
    };


    /**
     * Returns a synchronized instance of the CurrencyDatabase.
     *
     * @param context The application context.
     * @return A synchronized instance of the CurrencyDatabase.
     */
    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "currencyDatabase")
                    .addMigrations(MIGRATION_2_6, MIGRATION_2_7) // Add the migration here
                    .build();
        }
        return instance;
    }
}

