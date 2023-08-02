package algonquin.cst2335.finalandroidproject.Currency;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CurrencySelected.class}, version = 2)
public abstract class CurrencyDatabase extends RoomDatabase {

    public abstract CurrencyDAO cDAO();

    // Add the migration here
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Write your migration SQL code here.
            // For example, if you added a new column 'newColumn' to 'CurrencySelected' table:
            // database.execSQL("ALTER TABLE CurrencySelected ADD COLUMN newColumn TEXT");
            database.execSQL("ALTER Table CurrencySelected Add COLUMN amountConverted  TEXT");
        }
    };
}
