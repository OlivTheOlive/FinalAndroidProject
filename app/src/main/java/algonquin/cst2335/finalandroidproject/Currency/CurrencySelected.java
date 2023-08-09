package algonquin.cst2335.finalandroidproject.Currency;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a currency conversion result stored in the database.
 */
@Entity
public class CurrencySelected {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "conversionResult")
    String conversionResult;

    @ColumnInfo(name = "time")
    String time;
    /**
     * Default constructor for CurrencySelected.
     */
    public CurrencySelected() {}

    /**
     * Constructor to create a new CurrencySelected instance with conversion result and time.
     *
     * @param a The conversion result.
     * @param b The timestamp when the conversion occurred.
     */
    CurrencySelected(String a, String b ){
        conversionResult = a;
        time = b;
    }

    /**
     * Get the conversion result.
     *
     * @return The conversion result.
     */
    public String getConversionResult(){
        return conversionResult;
    }
    /**
     * Get the timestamp when the conversion occurred.
     *
     * @return The timestamp.
     */
    public String getTime(){
        return time;
    }

}