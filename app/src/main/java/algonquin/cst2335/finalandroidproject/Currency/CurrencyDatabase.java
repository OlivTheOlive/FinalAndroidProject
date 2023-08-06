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

    static final Migration MIGRATION_2_6 = new Migration(2, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE `CurrencySelected`");

            database.execSQL("CREATE TABLE IF NOT EXISTS `CurrencySelected` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`conversionResult` TEXT, `time` TEXT )");
        }
    };

    static final Migration MIGRATION_2_7 = new Migration(2, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //database.execSQL("DROP TABLE `CurrencySelected`");

            database.execSQL("CREATE TABLE IF NOT EXISTS `CurrencySelected` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`conversionResult` TEXT, `time` TEXT )");
        }
    };
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

