package algonquin.cst2335.finalandroidproject.Aviation.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A class representing a FlightRequest entity, which holds information about a flight request.
 */
@Entity
public class FlightRequest {


    /**
     * The primary key of the FlightRequest entity.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    /**
     * The request code associated with the flight request.
     */
    @ColumnInfo(name= "reqCode")
    protected String Code;
    /**
     * The name identifier associated with the flight request.
     */
    @ColumnInfo(name= "nameID")
    protected String nameID;
    /**
     * The status identifier associated with the flight request.
     */
    @ColumnInfo(name= "statusID")
    protected String statusID;
    /**
     * The flight ID associated with the flight request.
     */
    @ColumnInfo(name= "flightID")
    protected String flightID;
    /**
     * The save ID associated with the flight request.
     */
    @ColumnInfo(name= "savedID")
    protected String saveID;
    /**
     * The departure airport code associated with the flight request.
     */
    @ColumnInfo(name= "departureAP")
    protected String departureAP;
    /**
     * The arrival airport code associated with the flight request.
     */
    @ColumnInfo(name= "arrivalAP")
    protected String arrivalAP;
    /**
     * The airline name associated with the flight request.
     */
    @ColumnInfo(name= "airlineN")
    protected String airlineN;

    /**
     * The date of the flight request.
     */
    @ColumnInfo(name= "date")
    protected String date;

    /**
     * Default constructor for creating a FlightRequest instance.
     */
    public FlightRequest(){}

    /**
     * Constructor for creating a FlightRequest instance with provided values.
     *
     * @param s The request code.
     * @param s1 The flight ID.
     * @param s2 The name identifier.
     * @param s3 The status identifier.
     * @param s4 The save ID.
     * @param s5 The date.
     * @param s6 The departure airport code.
     * @param s7 The arrival airport code.
     * @param s8 The airline name.
     */
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

    /**
     * Get the request code associated with this FlightRequest.
     *
     * @return The request code.
     */
    public String getCode() {
        return Code;
    }

    /**
     * Set the request code for this FlightRequest.
     *
     * @param code The request code to set.
     */
    public void setCode(String code) {
        Code = code;
    }

    /**
     * Get the flight ID associated with this FlightRequest.
     *
     * @return The flight ID.
     */
    public String getFlightID() {
        return flightID;
    }

    /**
     * Set the flight ID for this FlightRequest.
     *
     * @param flightID The flight ID to set.
     */
    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    /**
     * Get the status identifier associated with this FlightRequest.
     *
     * @return The status identifier.
     */
    public String getStatusID() {
        return statusID;
    }

    /**
     * Set the status identifier for this FlightRequest.
     *
     * @param statusID The status identifier to set.
     */
    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    /**
     * Get the name identifier associated with this FlightRequest.
     *
     * @return The name identifier.
     */
    public String getNameID() {
        return nameID;
    }

    /**
     * Set the name identifier for this FlightRequest.
     *
     * @param nameID The name identifier to set.
     */
    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    /**
     * Get the save ID associated with this FlightRequest.
     *
     * @return The save ID.
     */
    public String getSaveID() {
        return saveID;
    }

    /**
     * Set the save ID for this FlightRequest.
     *
     * @param saveID The save ID to set.
     */
    public void setSaveID(String saveID) {
        this.saveID = saveID;
    }

    /**
     * Get the departure airport code associated with this FlightRequest.
     *
     * @return The departure airport code.
     */
    public String getDepartureAP() {
        return departureAP;
    }

    /**
     * Set the departure airport code for this FlightRequest.
     *
     * @param departureAP The departure airport code to set.
     */
    public void setDepartureAP(String departureAP) {
        this.departureAP = departureAP;
    }

    /**
     * Get the arrival airport code associated with this FlightRequest.
     *
     * @return The arrival airport code.
     */
    public String getArrivalAP() {
        return arrivalAP;
    }

    /**
     * Set the arrival airport code for this FlightRequest.
     *
     * @param arrivalAP The arrival airport code to set.
     */
    public void setArrivalAP(String arrivalAP) {
        this.arrivalAP = arrivalAP;
    }

    /**
     * Get the airline name associated with this FlightRequest.
     *
     * @return The airline name.
     */
    public String getAirlineN() {
        return airlineN;
    }

    /**
     * Set the airline name for this FlightRequest.
     *
     * @param airlineN The airline name to set.
     */
    public void setAirlineN(String airlineN) {
        this.airlineN = airlineN;
    }

    /**
     * Get the date associated with this FlightRequest.
     *
     * @return The date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the date for this FlightRequest.
     *
     * @param date The date to set.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the primary key ID associated with this FlightRequest.
     *
     * @return The primary key ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the primary key ID for this FlightRequest.
     *
     * @param id The primary key ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

}
