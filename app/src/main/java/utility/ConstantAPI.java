package utility;

/**
 * Created by Andy on 5/23/2016.
 */
public class ConstantAPI {
    public final static String BASE_URL = "http://login.mtrack.co.id/_engine/AGMobileConnector.php";

    public final static String getLoginURL(String username, String password) {
        return BASE_URL + "?cmd=dmobile.mlogin&usr="+username+"&psw="+password+"";
    }

    public final static String getGroupURL(String uid, String acc_id) {
        return BASE_URL + "?cmd=dmobile.getgroup&uid="+uid+"&acc_id="+acc_id+"";
    }

    public final static String getAllVehicleURL(String uid, String acc_id, String group_id) {
        return BASE_URL + "?cmd=dmobile.vechlist&uid="+uid+"&acc_id="+acc_id+"&group_id="+group_id+"";
    }

    public final static String getLastPositionURL(String uid, String acc_id, String group_id) {
        return BASE_URL + "?cmd=dmobile.lastpost&uid="+uid+"&acc_id="+acc_id+"&group_id="+group_id+"";
    }

    public final static String getReportVehicleURL(String uid, String acc_id, String asset_id, String date1, String date2) {
        return BASE_URL + "?cmd=dmobile.parkingreport&uid="+uid+"&acc_id="+acc_id+"&asset_id="+asset_id+"&date1="+date1+"&date2="+date2+"";
    }

    public final static String getHistoryVehicleURL(String uid, String acc_id, String asset_id, String date1, String date2) {
        return BASE_URL + "?cmd=dmobile.historical&uid="+uid+"&acc_id="+acc_id+"&asset_id="+asset_id+"&date1="+date1+"&date2="+date2+"";
    }
}
