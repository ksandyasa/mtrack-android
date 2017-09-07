package model;

/**
 * Created by Andy on 5/23/2016.
 */
public class Vehicle {
    private String asset_id;
    private String device_id;
    private String asset_code;
    private String group_name;

    public Vehicle() {

    }

    public Vehicle(String ai, String di, String ac, String gn) {
        this.asset_id = ai;
        this.device_id = di;
        this.asset_code = ac;
        this.group_name = gn;
    }

    public void setAssetId(String ai) {
        this.asset_id = ai;
    }

    public void setDeviceId(String di) {
        this.device_id = di;
    }

    public void setAssetCode(String ac) {
        this.asset_code = ac;
    }

    public void setGroupName(String gn) {
        this.group_name = gn;
    }

    public String getAssetId() {
        return this.asset_id;
    }

    public String getAssetCode() {
        return this.asset_code;
    }
    public String getDeviceId() {
        return this.device_id;
    }
    public String getGroupName() {
        return this.group_name;
    }

}

