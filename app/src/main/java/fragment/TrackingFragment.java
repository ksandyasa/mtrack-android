package fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import id.kreasisejahterateknologi.mtrack.R;
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

import callback.TrackingFragmentCallback;
import model.LastPosition;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utility.ConstantAPI;
import utility.SharedPreferencesProvider;
import utility.Utility;

/**
 * Created by apridosandyasa on 5/26/16.
 */
@SuppressLint("ValidFragment")
public class TrackingFragment extends Fragment implements OnMapReadyCallback {
    private final String TAG = TrackingFragment.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private View view;
    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private Marker marker;
    private LastPosition lastPosition;
    private CheckedTextView ctv_traffic_tracking;
    private ImageView iv_refresh_tracking;
    private TrackingFragmentCallback trackingFragmentCallback;
    private int selectedPos = -1;
    private List<LastPosition> lastPositionList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String[] responseString = {""};
    private Snackbar snackbar;

    public TrackingFragment() {

    }

    public TrackingFragment(AppCompatActivity aca, LastPosition data, int position, TrackingFragmentCallback listener) {
        this.appCompatActivity = aca;
        this.lastPosition = data;
        this.selectedPos = position;
        this.trackingFragmentCallback = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_tracking, container, false);

        this.ctv_traffic_tracking = (CheckedTextView) this.view.findViewById(R.id.ctv_traffic_tracking);
        this.iv_refresh_tracking = (ImageView) this.view.findViewById(R.id.iv_refresh_tracking);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTrackingProgressDialog();

        try {
            initializeMap();
        } catch (Exception e) {
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new ShowLastPosition());
    }

    class ShowLastPosition implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                // handle back button's click listener
                trackingFragmentCallback.ShowLastPositionView();

                return true;
            }

            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);

        LatLng pinPos = new LatLng(Double.parseDouble(lastPosition.getLatitude()), Double.parseDouble(lastPosition.getLongitude()));

        Bitmap bitmap = Utility.DisplayUtility.getRotatedBitmap(this.appCompatActivity, R.drawable.icon_north, Float.parseFloat(lastPosition.getHeading()));

        this.marker = this.googleMap.addMarker(
                new MarkerOptions().position(pinPos)
                        .title(lastPosition.getAssetCode())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.marker.getPosition(), 18f));

        this.ctv_traffic_tracking.setOnClickListener(new ShowTrafficOnTrackingMap());
    }

    private void initializeMap() {
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_gmap_tracking);
        this.supportMapFragment.getMapAsync(this);
        this.iv_refresh_tracking.setOnClickListener(new RefreshLastPositionOnTracking());
    }

    private void doObtainLastPosition() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ConstantAPI.getLastPositionURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                        SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                        "0"))
                .build();
        Log.d(TAG, ConstantAPI.getLastPositionURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                "0"));

        Call call = okHttpClient.newCall(request);
        call.enqueue(new LastPositionCallback());
    }

    private void initLastPositionDataToList(String response) {
        try {
            this.progressDialog.dismiss();
            this.lastPositionList.clear();
            this.lastPositionList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            this.lastPosition = this.lastPositionList.get(this.selectedPos);

            Log.d(TAG, "latitude " + lastPosition.getLatitude() + ", longitude " + lastPosition.getLongitude());

            LatLng pinPos = new LatLng(Double.parseDouble(lastPosition.getLatitude()), Double.parseDouble(lastPosition.getLongitude()));

            Bitmap bitmap = Utility.DisplayUtility.getRotatedBitmap(this.appCompatActivity, R.drawable.icon_north, Float.parseFloat(lastPosition.getHeading()));

            if (this.marker != null)
                this.marker.remove();

            this.marker = this.googleMap.addMarker(
                    new MarkerOptions().position(pinPos)
                            .title(lastPosition.getAssetCode())
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.marker.getPosition(), 18f));

        } catch (Exception e) {
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

    private void setTrackingProgressDialog() {
        this.progressDialog = new ProgressDialog(this.appCompatActivity);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading... please wait.");
    }

    class ShowTrafficOnTrackingMap implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (ctv_traffic_tracking.isChecked()) {
                ctv_traffic_tracking.setChecked(false);
                googleMap.setTrafficEnabled(false);
            }else {
                ctv_traffic_tracking.setChecked(true);
                Log.d(TAG, "traffic enabled " + googleMap.isTrafficEnabled());
                googleMap.setTrafficEnabled(true);
                Log.d(TAG, "traffic enabled " + googleMap.isTrafficEnabled());
            }
        }
    }

    class LastPositionCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            progressDialog.dismiss();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            responseString[0] = response.body().string();
            Log.d(TAG, responseString[0]);
            appCompatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLastPositionDataToList(responseString[0]);
                }
            });
        }
    }

    class RefreshLastPositionOnTracking implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                progressDialog.show();
                doObtainLastPosition();
            }else {
                snackbar = Snackbar.make(view, "No internet connection.", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new DismissTrackingSnackbar());
                snackbar.show();
            }
        }
    }

    class DismissTrackingSnackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }
}
