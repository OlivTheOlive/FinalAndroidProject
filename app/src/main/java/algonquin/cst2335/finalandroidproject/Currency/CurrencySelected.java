package algonquin.cst2335.finalandroidproject.Currency;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencySelected{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")

    public int id;

    @ColumnInfo(name="conversionResult")
    String conversionResult;


    @ColumnInfo(name="time")
    String time;

    public CurrencySelected(){}


    CurrencySelected(String a, String b ){
        conversionResult = a;
        time = b;
    }


    public String conversionResult(){
        return conversionResult;
    }

    public String getTime(){
        return time;
    }

}