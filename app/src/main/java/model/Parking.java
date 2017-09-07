package model;

/**
 * Created by apridosandyasa on 5/30/16.
 */
public class Parking {
    private String asset_code;
    private String timestamp_off;
    private String timestamp_on;
    private String duration;
    private String address;
    private String distance;

    public Parking() {

    }

    public Parking(String ac, String toff, String ton, String dr, String add, String d) {
        this.asset_code = ac;
        this.timestamp_off = toff;
        this.timestamp_on = ton;
        this.duration = dr;
        this.address = add;
        this.distance = d;
    }

    public void setAssetCode(String ac) {
        this.asset_code = ac;
    }

    public void setTimestampOff(String toff) {
        this.timestamp_off = toff;
    }

    public void setTimestampOn(String ton) {
        this.timestamp_on = ton;
    }

    public void setDuration(String dr) {
        this.duration = dr;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public void setDistance(String d) {
        this.distance = d;
    }

    public String getAssetCode() {
        return this.asset_code;
    }

    public String getTimestampOff() {
        return this.timestamp_off;
    }

    public String getTimestampOn() {
        return this.timestamp_on;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDistance() {
        return this.distance;
    }
}
