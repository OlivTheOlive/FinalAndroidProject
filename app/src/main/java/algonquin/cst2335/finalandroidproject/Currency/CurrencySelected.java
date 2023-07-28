package algonquin.cst2335.finalandroidproject.Currency;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencySelected{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")

    public int id;

    @ColumnInfo(name="Amount")
    Integer amount;

    @ColumnInfo(name="timeOfCurrency")
    String timeOfCurrency;

    @ColumnInfo(name="CADOrFF")
    String CADOrFF;


    public CurrencySelected(String s, String timeOfCurrency, int type) {}

    CurrencySelected(int a, String t, String cadOrff ){
        amount = a;
        timeOfCurrency = t;
        CADOrFF = cadOrff;
    }

    public CurrencySelected() {

    }

    public Integer getAmount(){
        return amount;
    }

    public Integer getTimeOfCurrency(){
        return timeOfCurrency;
    }

    public Integer getCADOrFF(){
        return CADOrFF;
    }


}