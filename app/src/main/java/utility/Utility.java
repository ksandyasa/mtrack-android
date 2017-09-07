package utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import id.kreasisejahterateknologi.mtrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Group;
import model.Historical;
import model.LastPosition;
import model.Login;
import model.Parking;
import model.Vehicle;

/**
 * Created by Andy on 5/19/2016.
 */
public class Utility {

    public static class CalendarUtility {
        private final static String TAG = Utility.class.getSimpleName();

        public static Calendar getCalendarInstance() {
            return Calendar.getInstance();
        }

        public static String getDurationOfLastPosition(Date startDate, Date endDate) {
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            Log.d(TAG, "startDate : " + startDate);
            Log.d(TAG, "endDate : "+ endDate);
            Log.d(TAG, "different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            String duration = elapsedDays + " days, " + elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds";

            Log.d(TAG, elapsedDays + " days, " + elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds");

            return duration;
        }

        public static Date getDateFromString(String dateString) throws ParseException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return simpleDateFormat.parse(dateString);
        }

    }

    public static class DisplayUtility {

        public static DisplayMetrics getDisplayMetrics(Context context) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity ) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics;
        }

        public static int getScreenHeight(DisplayMetrics displayMetrics) {
            int height = (int) (displayMetrics.heightPixels * 0.5) - 44;
            return height;
        }

        public static Animation setAnimationToView(Context context) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim);
            return animation;
        }

        public static Animation setZeroAlphaAnimationToView(Context context) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_zero_anim);
            return animation;
        }

        public static Animation setRotateAnimationToView(float heading) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, heading);
            rotateAnimation.setDuration(500);
            return rotateAnimation;
        }

        public static Bitmap getRotatedBitmap(Context context, int res, float heading) {
            Bitmap myImg = BitmapFactory.decodeResource(context.getResources(), res);

            Matrix matrix = new Matrix();
            matrix.postRotate(heading);

            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                    matrix, true);

            return rotated;
        }
    }

    public static class ConnectionUtility {

        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

    }

    public  static class OtherUtlity {

        public static String translateHeading(float h) {
            String heading = "";
            heading = (h >= 0 && h < 22.5) ? "NNE" : (h >= 22.5 && h < 45) ? "NE" : (h >= 45 && h < 67.5) ?
                    "ENE" : (h >= 67.5 && h < 90) ? "E" : (h >= 90 && h < 112.5) ? "ESE" : (h >= 112.5 && h < 135) ?
                    "SE" : (h >= 135 && h < 157.5) ? "SSE" : (h >= 157.5 && h < 180) ? "S" : (h >= 180 && h < 202.5) ?
                    "SSW" : (h >= 202.5 && h < 225) ? "SW" : (h >= 225 && h < 247.5) ? "WSW" : (h >= 247.5 && h < 270) ?
                    "W" : (h >= 270 && h < 292.5) ? "WNW" : (h >= 292.5 && h < 315) ? "NW" : (h >= 315 && h < 337.5) ?
                    "NNW" : (h >= 337.5 && h < 360) ? "N" : "N";

            return heading;
        }

    }

    public static class JSONUtility {

        public static Login getLoginDataFromJSON(String json) throws JSONException {
            JSONObject jsonObject = new JSONObject(json);
            Login login = new Login();
            if (jsonObject.getInt("success") == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("product");
                login.setUid(jsonArray.getJSONObject(0).getString("uid"));
                login.setId(jsonArray.getJSONObject(0).getString("acc_id"));
                login.setCompanyName(jsonArray.getJSONObject(0).getString("acc_companyname"));
                login.setSuccess(jsonObject.getInt("success"));
            }
            return login;
        }

        public static List<Group> getListGroupFromJSON(String json) throws JSONException{
            JSONObject jsonObject = new JSONObject(json);
            List<Group> groupList = new ArrayList<>();
            if (jsonObject.getInt("success") == 1) {
                JSONArray jsonArray = jsonObject.getJSONArray("group_list");
                for (int i = 0;i < jsonArray.length(); i++) {
                    groupList.add(new Group(jsonArray.getJSONObject(i).getString("group_ID"), jsonArray.getJSONObject(i).getString("group_name")));
                }
            }
            return groupList;
        }

        public static List<Vehicle> getListVehiclesFromJSON(String json) throws JSONException {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("vechlist");
            List<Vehicle> vehicleList = new ArrayList<>();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length() ; i++) {
                    vehicleList.add(new Vehicle(jsonArray.getJSONObject(i).getString("asset_ID"),
                            jsonArray.getJSONObject(i).getString("deviceID"),
                            jsonArray.getJSONObject(i).getString("asset_code"),
                            jsonArray.getJSONObject(i).getString("group_name")));
                }
            }
            return vehicleList;
        }

        public static List<LastPosition> getListLastPositionFromJSON(String json) throws JSONException, ParseException {
            List<LastPosition> lastPositionList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("lastpost");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date endDate = CalendarUtility.getCalendarInstance().getTime();
                    Date startDate = simpleDateFormat.parse(jsonArray.getJSONObject(i).getString("timestamp"));
                    String duration = CalendarUtility.getDurationOfLastPosition(startDate, endDate);

                    LastPosition lastPosition = new LastPosition();
                    lastPosition.setAssetId(jsonArray.getJSONObject(i).getString("asset_ID"));
                    lastPosition.setAssetCode(jsonArray.getJSONObject(i).getString("asset_code"));
                    lastPosition.setTimestamp(jsonArray.getJSONObject(i).getString("timestamp"));
                    lastPosition.setStatusCode((!jsonArray.getJSONObject(i).optString("statuscode").equals("null")) ? jsonArray.getJSONObject(i).getString("statuscode") : "No data");
                    lastPosition.setInputMask((!jsonArray.getJSONObject(i).optString("inputMask").equals("null")) ? jsonArray.getJSONObject(i).getString("inputMask") : "No data");
                    lastPosition.setPhoneNumber(jsonArray.getJSONObject(i).getString("simPhoneNumber"));
                    lastPosition.setDistance(jsonArray.getJSONObject(i).getString("distanceKM"));
                    lastPosition.setAddress(jsonArray.getJSONObject(i).getString("address"));
                    lastPosition.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                    lastPosition.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                    lastPosition.setHeading(jsonArray.getJSONObject(i).getString("heading"));
                    lastPosition.setSpeed(jsonArray.getJSONObject(i).getString("speedKPH"));
                    lastPosition.setSpeedViolation(jsonArray.getJSONObject(i).getString("speedviolation"));
                    lastPosition.setGeofencePoint(jsonArray.getJSONObject(i).getString("geofencepoi"));
                    lastPosition.setGeofenceRoute(jsonArray.getJSONObject(i).getString("geofenceroute"));
                    lastPosition.setDuration(duration);

                    lastPositionList.add(lastPosition);
                }
            }
            return lastPositionList;
        }

        public static List<Parking> getListParkingFromJSON(String json) throws JSONException {
            List<Parking> parkingList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("parking");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    parkingList.add(new Parking(jsonArray.getJSONObject(i).getString("asset_code"),
                            jsonArray.getJSONObject(i).getString("off"),
                            jsonArray.getJSONObject(i).getString("on"),
                            jsonArray.getJSONObject(i).getString("duration"),
                            jsonArray.getJSONObject(i).getString("address"),
                            jsonArray.getJSONObject(i).getString("distanceKM")));
                }
            }
            return parkingList;
        }

        public static List<Historical> getListHistoricalFromJSON(String json) throws JSONException {
            List<Historical> historicalList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("historical");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    historicalList.add(new Historical(jsonArray.getJSONObject(i).getString("asset_ID"),
                            jsonArray.getJSONObject(i).getString("asset_code"),
                            jsonArray.getJSONObject(i).getString("timestamp"),
                            (!jsonArray.getJSONObject(i).optString("statuscode").equals("null")) ? jsonArray.getJSONObject(i).getString("statuscode") : "No data",
                            (!jsonArray.getJSONObject(i).optString("inputMask").equals("null")) ? jsonArray.getJSONObject(i).getString("inputMask") : "No data",
                            jsonArray.getJSONObject(i).getString("address"),
                            jsonArray.getJSONObject(i).getString("distanceKM"),
                            jsonArray.getJSONObject(i).getString("speedKPH")));
                }
            }
            return historicalList;
        }

    }

}
