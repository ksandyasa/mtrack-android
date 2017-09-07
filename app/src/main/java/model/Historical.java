package model;

/**
 * Created by apridosandyasa on 6/8/16.
 */
public class Historical {
    private String asset_id;
    private String asset_code;
    private String timestamp;
    private String status_code;
    private String input_mask;
    private String address;
    private String distance;
    private String speed;

    public Historical() {

    }

    public Historical(String ai, String ac, String ts, String sc, String im, String add, String dist, String spd) {
        this.asset_id = ai;
        this.asset_code = ac;
        this.timestamp = ts;
        this.status_code = sc;
        this.input_mask = im;
        this.address = add;
        this.distance = dist;
        this.speed = spd;
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

    public String getAddress() {
        return this.address;
    }

    public String getDistance() {
        return this.distance;
    }

    public String getSpeed() {
        return this.speed;
    }

}
