package model;

/**
 * Created by Andy on 5/23/2016.
 */
public class LastPosition {
    private String asset_id;
    private String asset_code;
    private String timestamp;
    private String status_code;
    private String input_mask;
    private String phone_number;
    private String distance;
    private String address;
    private String latitude;
    private String longitude;
    private String heading;
    private String speed;
    private String speed_violation;
    private String geofence_point;
    private String geofence_route;
    private String duration;

    public LastPosition() {

    }

    public LastPosition(String ai, String ac, String ts, String sc, String im, String pn, String dist, String add, String lat,
                        String lng, String head, String spd, String sv, String gp, String gr) {
        this.asset_id = ai;
        this.asset_code = ac;
        this.timestamp = ts;
        this.status_code = sc;
        this.input_mask = im;
        this.phone_number = pn;
        this.distance = dist;
        this.address = add;
        this.latitude = lat;
        this.longitude = lng;
        this.heading = head;
        this.speed = spd;
        this.speed_violation = sv;
        this.geofence_point = gp;
        this.geofence_route = gr;
    }

    public void setAssetId(String ai) {
        this.asset_id = ai;
    }

    public void setAssetCode(String ac) {
        this.asset_code = ac;
    }

    public void setTimestamp(String ts) {
        this.timestamp = ts;
    }

    public void setStatusCode(String sc) {
        this.status_code = sc;
    }

    public void setInputMask(String im) {
        this.input_mask = im;
    }

    public void setPhoneNumber(String pn) {
        this.phone_number = pn;
    }

    public void setDistance(String dist) {
        this.distance = dist;
    }

    public void setAddress(String add) {
        this.address = add;
    }

    public void setHeading(String head) {
        this.heading = head;
    }

    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    public void setLongitude(String lng) {
        this.longitude = lng;
    }

    public void setSpeed(String spd) {
        this.speed = spd;
    }

    public void setSpeedViolation(String sv) {
        this.speed_violation = sv;
    }

    public void setGeofencePoint(String gp) {
        this.geofence_point = gp;
    }

    public void setGeofenceRoute(String gr) {
        this.geofence_route = gr;
    }

    public void setDuration(String dr) {
        this.duration = dr;
    }

    public String getAssetId() {
        return this.asset_id;
    }

    public String getAssetCode() {
        return this.asset_code;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getStatusCode() {
        return this.status_code;
    }

    public String getInputMask() {
        return this.input_mask;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public String getDistance() {
        return  this.distance;
    }

    public String getAddress() {
        return this.address;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getHeading() {
        return this.heading;
    }

    public String getSpeed() {
        return this.speed;
    }

    public String getSpeedViolation() {
        return this.speed_violation;
    }

    public String getGeofencePoint() {
        return this.geofence_point;
    }

    public String getGeofenceRoute() {
        return this.geofence_route;
    }

    public String getDuration() {
        return this.duration;
    }
}

