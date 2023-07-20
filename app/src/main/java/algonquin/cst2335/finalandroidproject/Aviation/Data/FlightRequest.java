package algonquin.cst2335.finalandroidproject.Aviation.Data;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FlightRequest {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name= "reqCode")
    protected String Code;

    public FlightRequest(){};
    public FlightRequest(String m){

        Code = m;
    };

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }


}
