package fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import adapter.ListLastPositionAdapter;
import callback.LastPositionCallback;
import callback.LastPositonAdapterCallback;
import id.kreasisejahterateknologi.mtrack.R;
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
 * Created by Andy on 5/23/2016.
 */
@SuppressLint("ValidFragment")
public class LastPositionFragment extends Fragment implements Callback, LastPositonAdapterCallback {
    private final String TAG = LastPositionFragment.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private View view;
    private LinearLayout ll_container_lastposition;
    private RelativeLayout rl_refresh_last_position, rl_list_last_position, rl_content_last_position;
    private ListView lv_last_position;
    private ListLastPositionAdapter lv_last_position_adapter;
    private ImageView iv_refresh_last_position;
    private TextView tv_assetcode_lastposition, tv_value_timestamp_lastposition,
                    tv_value_address_lastposition, tv_value_status_lastposition,
                    tv_value_engine_lastposition, tv_value_gsm_lastposition,
                    tv_value_distance_lastposition, tv_value_speed_lastposition,
                    tv_value_duration_lastposition, tv_value_alert_lastposition,
                    tv_refresh_last_position;
    private Button btn_viewmap_lastposition;

    private String[] responseString = {""};
    private List<LastPosition> lastPositionList = new ArrayList<>();
    private int selectedPos = 0;
    private LastPositionCallback lastPositionCallback;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;

    public LastPositionFragment() {

    }

    public LastPositionFragment(AppCompatActivity aca, LastPositionCallback listener) {
        this.appCompatActivity = aca;
        this.lastPositionCallback = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_lastposition, container, false);

        this.ll_container_lastposition = (LinearLayout) this.view.findViewById(R.id.ll_container_lastposition);
        this.rl_refresh_last_position = (RelativeLayout) this.view.findViewById(R.id.rl_refresh_last_position);
        this.rl_list_last_position = (RelativeLayout) this.view.findViewById(R.id.rl_list_last_position);
        this.rl_content_last_position = (RelativeLayout) this.view.findViewById(R.id.rl_content_last_position);
        this.iv_refresh_last_position = (ImageView) this.view.findViewById(R.id.iv_refresh_last_position);
        this.tv_refresh_last_position = (TextView) this.view.findViewById(R.id.tv_refresh_last_positon);
        this.lv_last_position = (ListView) this.view.findViewById(R.id.lv_last_position);
        this.tv_assetcode_lastposition = (TextView) this.view.findViewById(R.id.tv_assetcode_lastposition);
        this.tv_value_timestamp_lastposition = (TextView) this.view.findViewById(R.id.tv_value_timestamp_lastposition);
        this.tv_value_address_lastposition = (TextView) this.view.findViewById(R.id.tv_value_address_lastposition);
        this.tv_value_status_lastposition = (TextView) this.view.findViewById(R.id.tv_value_status_lastposition);
        this.tv_value_engine_lastposition = (TextView) this.view.findViewById(R.id.tv_value_engine_lastposition);
        this.tv_value_gsm_lastposition = (TextView) this.view.findViewById(R.id.tv_value_gsm_lastposition);
        this.tv_value_distance_lastposition = (TextView) this.view.findViewById(R.id.tv_value_distance_lastposition);
        this.tv_value_speed_lastposition = (TextView) this.view.findViewById(R.id.tv_value_speed_lastposition);
        this.tv_value_duration_lastposition = (TextView) this.view.findViewById(R.id.tv_value_duration_lastposition);
        this.tv_value_alert_lastposition = (TextView) this.view.findViewById(R.id.tv_value_alert_lastposition);
        this.btn_viewmap_lastposition = (Button) this.view.findViewById(R.id.btn_viewmap_last_position);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SetLastPositionProgressDialog();

        this.iv_refresh_last_position.setOnClickListener(new RefreshLastPositionFromImage());

        if (Utility.ConnectionUtility.isNetworkConnected(this.appCompatActivity)) {
            this.progressDialog.show();
            doObtainLastPosition();
        }else {
            this.rl_refresh_last_position.startAnimation(Utility.DisplayUtility.setAnimationToView(this.appCompatActivity));
            this.rl_list_last_position.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(this.appCompatActivity));
            this.rl_content_last_position.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(this.appCompatActivity));
            this.rl_refresh_last_position.setVisibility(View.VISIBLE);
            this.rl_list_last_position.setVisibility(View.GONE);
            this.rl_content_last_position.setVisibility(View.GONE);
            snackbar = Snackbar.make(this.ll_container_lastposition, "No internet connection.", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Dismiss", new DismissLastPositionSnackbar());
            snackbar.show();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        this.progressDialog.dismiss();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        responseString[0] = response.body().string();
        Log.d(TAG, responseString[0]);
        this.appCompatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initLastPositionDataToList(responseString[0]);
            }
        });
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
        call.enqueue(this);
    }

    private void doRefreshLastPosition() {
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
        call.enqueue(new RefreshCallback());
    }

    private void initLastPositionDataToList(String response) {
        try {
            if (this.progressDialog != null)
                this.progressDialog.dismiss();
            this.lastPositionList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            this.lv_last_position_adapter = new ListLastPositionAdapter(this.appCompatActivity, this.lastPositionList);
            this.lv_last_position.setAdapter(this.lv_last_position_adapter);
            this.lv_last_position.setOnItemClickListener(new LastPositionItemSelect());
            showFirstRowListData();
            this.btn_viewmap_lastposition.setOnClickListener(new ShowTrackingMap());
        } catch (JSONException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        } catch (ParseException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        }
    }

    private void refreshLastPositionDataToList(String response) {
        try {
            if (this.progressDialog != null)
                this.progressDialog.dismiss();
            this.lastPositionList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            this.lv_last_position_adapter = new ListLastPositionAdapter(this.appCompatActivity, this.lastPositionList);
            this.lv_last_position.setAdapter(this.lv_last_position_adapter);
            this.lv_last_position.setOnItemClickListener(new LastPositionItemSelect());
            this.btn_viewmap_lastposition.setOnClickListener(new ShowTrackingMap());
            setDataLastPositionFromRow(this.selectedPos);
        } catch (JSONException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        } catch (ParseException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        }
    }

    private void showFirstRowListData() {
        this.selectedPos = 0;
        this.tv_assetcode_lastposition.setText(this.lastPositionList.get(0).getAssetCode());
        this.tv_value_timestamp_lastposition.setText(this.lastPositionList.get(0).getTimestamp());
        this.tv_value_address_lastposition.setText(this.lastPositionList.get(0).getAddress());
        this.tv_value_status_lastposition.setText(this.lastPositionList.get(0).getInputMask());
        this.tv_value_engine_lastposition.setText(this.lastPositionList.get(0).getStatusCode());
        this.tv_value_gsm_lastposition.setText("+" + this.lastPositionList.get(0).getPhoneNumber());
        this.tv_value_distance_lastposition.setText(this.lastPositionList.get(0).getDistance());
        this.tv_value_speed_lastposition.setText(this.lastPositionList.get(0).getSpeed());
        this.tv_value_duration_lastposition.setText(this.lastPositionList.get(0).getDuration());
        if (lastPositionList.get(0).getSpeedViolation().equals("1") && lastPositionList.get(0).getGeofenceRoute().equals("1"))
            tv_value_alert_lastposition.setText("overspeed and exit geofence");
        else if (lastPositionList.get(0).getSpeedViolation().equals("1") && lastPositionList.get(0).getGeofenceRoute().equals("0"))
            tv_value_alert_lastposition.setText("overspeed");
        else if (lastPositionList.get(0).getSpeedViolation().equals("0") && lastPositionList.get(0).getGeofenceRoute().equals("1"))
            tv_value_alert_lastposition.setText("exit geofence");
        else
            tv_value_alert_lastposition.setText("-");
        this.tv_value_gsm_lastposition.setTextColor(this.appCompatActivity.getResources().getColor(R.color.colorPrimary));
        this.tv_value_gsm_lastposition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tv_value_gsm_lastposition.setOnClickListener(new CallPhoneNumber(0));
        lv_last_position.setItemChecked(0, true);
        this.lv_last_position.setSelection(0);
    }

    public void SelectPostLastPosition(int p) {
        Log.d(TAG, "pos " + p);
        this.lv_last_position.performItemClick(this.lv_last_position.getChildAt(p),
                p,
                this.lv_last_position_adapter.getItemId(p));
        this.lv_last_position.setItemChecked(p, true);
        this.lv_last_position.setSelection(p);
    }

    private void SetLastPositionProgressDialog() {
        this.progressDialog = new ProgressDialog(this.appCompatActivity);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading... please wait.");
    }

    class RefreshCallback implements Callback {

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
                    refreshLastPositionDataToList(responseString[0]);
                }
            });
        }
    }

    class ShowTrackingMap implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            lastPositionCallback.SendLastPositionDataToTracking(lastPositionList.get(selectedPos), selectedPos);
        }
    }

    class DismissLastPositionSnackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }

    class CallPhoneNumber implements View.OnClickListener {
        private int vPosition;

        public CallPhoneNumber(int p) {
            this.vPosition = p;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "+" + lastPositionList.get(vPosition).getPhoneNumber());
            String phone_no = "+" + lastPositionList.get(vPosition).getPhoneNumber();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone_no));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }
    }

    class LastPositionItemSelect implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedPos = position;
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                lv_last_position.setAdapter(null);
                lv_last_position_adapter = null;
                progressDialog.show();
                doRefreshLastPosition();
            }else {
                snackbar = Snackbar.make(ll_container_lastposition, "No internet connection.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissLastPositionSnackbar());
                snackbar.show();
            }
        }
    }

    class RefreshLastPositionFromImage implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                rl_refresh_last_position.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(appCompatActivity));
                rl_list_last_position.startAnimation(Utility.DisplayUtility.setAnimationToView(appCompatActivity));
                rl_content_last_position.startAnimation(Utility.DisplayUtility.setAnimationToView(appCompatActivity));
                rl_refresh_last_position.setVisibility(View.GONE);
                rl_list_last_position.setVisibility(View.VISIBLE);
                rl_content_last_position.setVisibility(View.VISIBLE);
                progressDialog.show();
                doObtainLastPosition();
            }else {
                snackbar = Snackbar.make(ll_container_lastposition, "No internet connection.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissLastPositionSnackbar());
                snackbar.show();
            }

        }
    }

    @Override
    public void setDataLastPositionFromRow(int pos) {
        tv_assetcode_lastposition.setText(lastPositionList.get(pos).getAssetCode());
        tv_value_timestamp_lastposition.setText(lastPositionList.get(pos).getTimestamp());
        tv_value_address_lastposition.setText(lastPositionList.get(pos).getAddress());
        tv_value_status_lastposition.setText(lastPositionList.get(pos).getInputMask());
        tv_value_engine_lastposition.setText(lastPositionList.get(pos).getStatusCode());
        tv_value_gsm_lastposition.setText("+" + lastPositionList.get(pos).getPhoneNumber());
        tv_value_distance_lastposition.setText(lastPositionList.get(pos).getDistance());
        tv_value_speed_lastposition.setText(lastPositionList.get(pos).getSpeed());
        tv_value_duration_lastposition.setText(lastPositionList.get(pos).getDuration());
        if (lastPositionList.get(pos).getSpeedViolation().equals("1") && lastPositionList.get(pos).getGeofenceRoute().equals("1"))
            tv_value_alert_lastposition.setText("overspeed and exit geofence");
        else if (lastPositionList.get(pos).getSpeedViolation().equals("1") && lastPositionList.get(pos).getGeofenceRoute().equals("0"))
            tv_value_alert_lastposition.setText("overspeed");
        else if (lastPositionList.get(pos).getSpeedViolation().equals("0") && lastPositionList.get(pos).getGeofenceRoute().equals("1"))
            tv_value_alert_lastposition.setText("exit geofence");
        else
            tv_value_alert_lastposition.setText("-");
        this.tv_value_gsm_lastposition.setTextColor(this.appCompatActivity.getResources().getColor(R.color.colorPrimary));
        this.tv_value_gsm_lastposition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tv_value_gsm_lastposition.setOnClickListener(new CallPhoneNumber(pos));
        lv_last_position_adapter.setSelectedPosition(pos);
        lv_last_position_adapter.notifyDataSetChanged();
        lv_last_position.setItemChecked(pos, true);
        this.lv_last_position.setSelection(pos);
    }
}
