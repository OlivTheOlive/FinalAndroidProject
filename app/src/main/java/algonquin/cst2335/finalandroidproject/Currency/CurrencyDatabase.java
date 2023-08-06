package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CurrencySelected.class}, version = 4, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {

    private static CurrencyDatabase instance;

    public abstract CurrencyDAO cDAO();

    // Add the migration here
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Write your migration SQL code here.
            // For example, if you added a new column 'newColumn' to 'CurrencySelected' table:
            // database.execSQL("ALTER TABLE CurrencySelected ADD COLUMN newColumn TEXT");
            database.execSQL("CREATE TABLE IF NOT EXISTS `CurrencySelected_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`conversionResult` TEXT, `time` TEXT)");

            // Copy the data from the old table to the new table
            database.execSQL("INSERT INTO `CurrencySelected_new` (`id`, `conversionResult`, `time`) " +
                    "SELECT `id`, `conversionResult`, `time` FROM `CurrencySelected`");

            // Drop the old table
            database.execSQL("DROP TABLE `CurrencySelected`");

            // Rename the new table to the original name
            database.execSQL("ALTER TABLE `CurrencySelected_new` RENAME TO `CurrencySelected`");

        }
    };

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "currencyDatabase")
                    .addMigrations(MIGRATION_3_4) // Add the migration here
                    .build();
        }
        return instance;
    }
}
