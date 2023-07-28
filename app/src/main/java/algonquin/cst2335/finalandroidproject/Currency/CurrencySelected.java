package algonquin.cst2335.finalandroidproject.Currency;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrencySelected{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")

    public int id;

    @ColumnInfo(name="amountToCovert")
    String amountCov;

    @ColumnInfo(name="time")
    String time;

    @ColumnInfo(name="CADOrFF")
    Integer CADOrFF;

    public CurrencySelected(){}


    CurrencySelected(String a, String t, Integer cadOrff ){
        amountCov = a;
        time = t;
        CADOrFF = cadOrff;
    }


    public String getAmount(){
        return amountCov;
    }

    public String getTimeOfCurrency(){
        return time;
    }

    public Integer getCADOrFF(){
        return CADOrFF;
    }


}