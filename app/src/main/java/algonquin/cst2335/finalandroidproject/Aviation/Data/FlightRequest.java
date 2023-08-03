package algonquin.cst2335.finalandroidproject.Aviation.Data;

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
    @ColumnInfo(name= "savedID")
    protected String saveID;
    @ColumnInfo(name= "departureAP")
    protected String departureAP;
    @ColumnInfo(name= "arrivalAP")
    protected String arrivalAP;
    @ColumnInfo(name= "airlineN")
    protected String airlineN;


    @ColumnInfo(name= "date")
    protected String date;
    public FlightRequest(){}

    public FlightRequest(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
        Code=s;
        flightID=s1;
        nameID=s2;
        statusID=s3;
        saveID=s4;
        date=s5;
        departureAP=s6;
        arrivalAP=s7;
        airlineN=s8;
    }


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
    public String getSaveID() {
        return saveID;
    }
    public void setSaveID(String saveID) {
        this.saveID = saveID;
    }
    public String getDepartureAP() {
        return departureAP;
    }

    public void setDepartureAP(String departureAP) {
        this.departureAP = departureAP;
    }

    public String getArrivalAP() {
        return arrivalAP;
    }

    public void setArrivalAP(String arrivalAP) {
        this.arrivalAP = arrivalAP;
    }

    public String getAirlineN() {
        return airlineN;
    }

    public void setAirlineN(String airlineN) {
        this.airlineN = airlineN;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
