package fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.CustomSpinnerAdapter;
import callback.CustomSpinnerAdapterCallback;
import callback.MainFragmentCallback;
import callback.SearchTrackingDialogCallback;
import dialog.SearchTrackingDialog;
import id.kreasisejahterateknologi.mtrack.R;
import com.google.maps.android.ui.IconGenerator;

import model.LastPosition;
import model.Vehicle;
import network.NetworkConnection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utility.ConstantAPI;
import utility.SharedPreferencesProvider;
import utility.Utility;

/**
 * Created by Andy on 5/19/2016.
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements OnMapReadyCallback, SearchTrackingDialogCallback,
        CustomSpinnerAdapterCallback, Callback {
    private final String TAG = MainFragment.class.getSimpleName();
    private AppCompatActivity appCompatActivity;
    private View view;
    private RelativeLayout rl_container_main;
    private SupportMapFragment supportMapFragment;
    private GoogleMap gmap_tracking;
    private List<Marker> markerPinPoint = new ArrayList<>();
    private Spinner sp_search_tracking;
    private TextView tv_vehicle_tracking;
    private ImageView ib_refresh_main;
    private SearchTrackingDialog searchTrackingDialog;
    private List<Vehicle> vehicleList = new ArrayList<>();
    private List<LastPosition> lastPositionList = new ArrayList<>();
    private String[] responseString = {""};
    private int posSpinItemSelected = -1;
    private int networkMode = -1;
    private int indeksLastPositon = 0;
    private int midPos;
    private float zoomAngle;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;
    private MainFragmentCallback mainFragmentCallback;
    private Handler mainFragmentHandler;
    private Messenger mainFragmentMessenger;
    private Message mainFragmentMessage;

    public MainFragment() {

    }

    public MainFragment(AppCompatActivity aca, MainFragmentCallback listener) {
        this.appCompatActivity = aca;
        this.mainFragmentCallback = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_main, container, false);

        this.rl_container_main = (RelativeLayout) this.view.findViewById(R.id.rl_container_main);
        this.sp_search_tracking = (Spinner) this.view.findViewById(R.id.sp_search_tracking);
        this.tv_vehicle_tracking = (TextView) this.view.findViewById(R.id.tv_vehicle_tracking);
        this.ib_refresh_main = (ImageView) this.view.findViewById(R.id.ib_refresh_main);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SetMainProgressDialog();

        if (Utility.ConnectionUtility.isNetworkConnected(this.appCompatActivity)) {
            this.progressDialog.show();
            this.networkMode = 0;
            doObtainAllVehicleData();
        }else {
            snackbar = Snackbar.make(rl_container_main, "No internet connection.", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Dismiss", new DismissMainSnackbar());
            snackbar.show();
        }

        this.ib_refresh_main.setOnClickListener(new RefreshDashboard());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmap_tracking = googleMap;

        this.gmap_tracking.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.gmap_tracking.getUiSettings().setCompassEnabled(true);
        this.gmap_tracking.getUiSettings().setZoomControlsEnabled(true);
        this.gmap_tracking.getUiSettings().setRotateGesturesEnabled(false);

        this.midPos = (this.lastPositionList.size() <= 25) ? (this.lastPositionList.size() / 2) : (this.lastPositionList.size() / 2) + 1;
        this.zoomAngle = (this.lastPositionList.size() <= 25) ? 9.0f : 4.75f;

        for (LastPosition data : lastPositionList) {
            LatLng pinPos = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));

            Log.d(TAG, data.getDuration());
            int durationDays = Integer.parseInt(data.getDuration().substring(0, data.getDuration().indexOf(" days")));
            Log.d(TAG, "durationDays" + durationDays);

            IconGenerator iconFactory = new IconGenerator(this.appCompatActivity);
            if (data.getStatusCode().equals("Engine Off")) {
                if (durationDays > 1) {
                    iconFactory.setContentView(SetCustomIconGeneratorView(data.getAssetCode(), this.appCompatActivity.getResources().getDrawable(R.drawable.custom_info_window_black_bg), Color.WHITE, this.indeksLastPositon, Float.parseFloat(data.getHeading()), data.getSpeedViolation(), data.getGeofenceRoute()));
                    iconFactory.setBackground(this.appCompatActivity.getResources().getDrawable(R.drawable.transparent_bg));
                }else {
                    iconFactory.setContentView(SetCustomIconGeneratorView(data.getAssetCode(), this.appCompatActivity.getResources().getDrawable(R.drawable.custom_info_window_red_bg), Color.WHITE, this.indeksLastPositon, Float.parseFloat(data.getHeading()), data.getSpeedViolation(), data.getGeofenceRoute()));
                    iconFactory.setBackground(this.appCompatActivity.getResources().getDrawable(R.drawable.transparent_bg));
                }
            }else if (data.getStatusCode().equals("Engine On")) {
                if (durationDays > 1) {
                    iconFactory.setContentView(SetCustomIconGeneratorView(data.getAssetCode(), this.appCompatActivity.getResources().getDrawable(R.drawable.custom_info_window_black_bg), Color.WHITE, this.indeksLastPositon, Float.parseFloat(data.getHeading()), data.getSpeedViolation(), data.getGeofenceRoute()));
                    iconFactory.setBackground(this.appCompatActivity.getResources().getDrawable(R.drawable.transparent_bg));
                }else
                    iconFactory.setContentView(SetCustomIconGeneratorView(data.getAssetCode(), this.appCompatActivity.getResources().getDrawable(R.drawable.custom_info_window_white_bg), Color.BLACK, this.indeksLastPositon, Float.parseFloat(data.getHeading()), data.getSpeedViolation(), data.getGeofenceRoute()));
                iconFactory.setBackground(this.appCompatActivity.getResources().getDrawable(R.drawable.transparent_bg));
            }

            Log.d(TAG, "anchorU " + iconFactory.getAnchorU() + ", anchorV " + iconFactory.getAnchorV());
            Marker marker = this.gmap_tracking.addMarker(
//                    new MarkerOptions().position(pinPos)
//                            .title(data.getAssetCode())
//                            .infoWindowAnchor(0, 0)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_start)));
                    new MarkerOptions().position(pinPos)
                            .title(data.getAssetCode())
                            .snippet(""+this.indeksLastPositon)
                            .anchor(iconFactory.getAnchorU(), 0.9f)
                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(data.getAssetCode()))));

            this.markerPinPoint.add(marker);
            this.indeksLastPositon++;
        }

        this.gmap_tracking.moveCamera(CameraUpdateFactory.newLatLngZoom(this.markerPinPoint.get(midPos).getPosition(), this.zoomAngle));
        this.gmap_tracking.setOnMarkerClickListener(new ShowLastPositionDetailFromDashboard());
    }



    private void initializeMap() {
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_google_maps);
        this.supportMapFragment.getMapAsync(this);
    }

    private void doObtainAllVehicleData() {
        this.mainFragmentHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                doMainFragmentParseMessage(msg);
            }
        };
        doMainFragmentNetworkService(ConstantAPI.getAllVehicleURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                "0"));
    }

    private void doObtainLastPositionData() {
        this.mainFragmentHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                doMainFragmentParseMessage(msg);
            }
        };
        doMainFragmentNetworkService(ConstantAPI.getLastPositionURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                "0"));
    }

    private void InitAllVehicleDataToSpinner(String response) {
        try {
            this.vehicleList = Utility.JSONUtility.getListVehiclesFromJSON(response);
            this.sp_search_tracking.setAdapter(new CustomSpinnerAdapter(this.appCompatActivity,
                    this.vehicleList, this));
            this.sp_search_tracking.setOnItemSelectedListener(new ShowSearchTrackingDialog());
            this.networkMode = 1;
            doObtainLastPositionData();
        } catch (Exception e) {
            Log.d(TAG, "Failed loading map " + e.getMessage());
        }
    }

    private void InitLastPositionDataToList(String response) {
        try {
            if (this.progressDialog != null)
                this.progressDialog.dismiss();
            this.lastPositionList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            initializeMap();
        } catch (Exception e) {
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

    private void doMainFragmentNetworkService(String url) {
        Intent networkIntent = new Intent(this.appCompatActivity, NetworkConnection.class);
        this.mainFragmentMessenger = new Messenger(this.mainFragmentHandler);
        networkIntent.putExtra("messenger", this.mainFragmentMessenger);
        networkIntent.putExtra("url", url);
        this.appCompatActivity.startService(networkIntent);
    }

    private void doMainFragmentParseMessage(Message message) {
        this.mainFragmentMessage = message;
        Bundle b = this.mainFragmentMessage.getData();
        responseString[0] = b.getString("network_response");
        Log.d(TAG, "responseString[0] " + responseString[0]);
        parseResponseJSONFromServer(responseString[0]);
    }

    private void parseResponseJSONFromServer(String response) {
        if (this.networkMode == 0)
            InitAllVehicleDataToSpinner(response);
        else
            InitLastPositionDataToList(response);
    }

    private View SetCustomIconGeneratorView(String title, Drawable drawable, int textColor, int pos, float heading, String speed, String geofence) {
        View view = LayoutInflater.from(this.appCompatActivity).inflate(R.layout.custom_info_window, null);
        ImageView iv_alert_info_window = (ImageView) view.findViewById(R.id.iv_alert_info_window);
        TextView tv_title_info_window = (TextView) view.findViewById(R.id.tv_title_info_window);
        TextView tv_pos_info_window = (TextView) view.findViewById(R.id.tv_pos_info_window);
        ImageView iv_marker_info_window = (ImageView) view.findViewById(R.id.iv_marker_info_window);
        iv_marker_info_window.setImageResource(R.drawable.icon_north);
        Bitmap bitmap = Utility.DisplayUtility.getRotatedBitmap(this.appCompatActivity, R.drawable.icon_north, heading);
        tv_title_info_window.setText(title);
        tv_pos_info_window.setText(""+pos);
        tv_title_info_window.setBackgroundDrawable(drawable);
        tv_title_info_window.setTextColor(textColor);
        if (speed.equals("1") || geofence.equals("1"))
            iv_alert_info_window.setVisibility(View.VISIBLE);
        else
            iv_alert_info_window.setVisibility(View.INVISIBLE);
        iv_marker_info_window.setImageBitmap(bitmap);
        return view;
    }

    private void SetMainProgressDialog() {
        this.progressDialog = new ProgressDialog(this.appCompatActivity);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading... please wait.");
    }

    @Override
    public void setNoVehicle(String noVehicle) {
        this.tv_vehicle_tracking.setText(noVehicle);
    }

    @Override
    public void reshowSearchDialog() {
        if (this.posSpinItemSelected == 1)
            showSearchDialog();
        else
            this.tv_vehicle_tracking.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        responseString[0] = response.body().string();
        Log.d(TAG, responseString[0]);
        this.appCompatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseResponseJSONFromServer(responseString[0]);
            }
        });
    }

    class ShowSearchTrackingDialog implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            posSpinItemSelected = position;
            Log.d(TAG, "positionSpinItemSelected = " + posSpinItemSelected);
            if (posSpinItemSelected == 1) {
                //showSearchDialog();
            }else {
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    class DismissMainSnackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }

    class RefreshDashboard implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                progressDialog.show();
                networkMode = 0;
                doObtainAllVehicleData();
            }else {
                snackbar = Snackbar.make(rl_container_main, "No internet connection.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissMainSnackbar());
                snackbar.show();
            }
        }
    }

    class ShowLastPositionDetailFromDashboard implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d(TAG, "position " + marker.getSnippet());
            try {
                Intent intentData = new Intent();
                intentData.putExtra("asset_pos", Integer.parseInt(marker.getSnippet()));
                intentData.putExtra("asset_code", marker.getTitle());
                mainFragmentCallback.SelectLastPositionFromDashboard(intentData);
                gmap_tracking.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomAngle));
            } catch (Exception e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
            return true;
        }
    }

    private void showSearchDialog() {
        this.searchTrackingDialog = new SearchTrackingDialog(this.appCompatActivity, this);
        this.searchTrackingDialog.setCancelable(false);
        this.searchTrackingDialog.show(this.appCompatActivity.getSupportFragmentManager(), "searchTrackingDialog");
        this.tv_vehicle_tracking.setVisibility(View.VISIBLE);
    }
}
