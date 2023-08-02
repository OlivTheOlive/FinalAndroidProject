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

    @ColumnInfo(name= "nameID")
    protected String nameID;


    @ColumnInfo(name= "statusID")
    protected String statusID;


    @ColumnInfo(name= "flightID")
    protected String flightID;

    public FlightRequest(){};
    public FlightRequest(String m, String n, String b, String k){
        nameID=b;
        Code = m;
        flightID=n;
        statusID=k;
    };

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }
    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }
}
