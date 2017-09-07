package fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;

import id.kreasisejahterateknologi.mtrack.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import adapter.LastPosition1Adapter;
import callback.LastPositionAdapterCallback1;
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
public class LastPosition1Fragment extends Fragment implements Callback, LastPositionAdapterCallback1 {
    private final String TAG = LastPosition1Fragment.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private View view;
    private ProgressDialog progressDialog;
    private LinearLayout ll_container_lastposition1;
    private PullToRefreshListView lv_lastposition1;
    private LastPosition1Adapter rv_lastposition1_adapter;
    private List<LastPosition> lastPositionList = new ArrayList<>();
    private String[] responseString = {""};
    private Snackbar snackbar;
    private int selectedPosition = 0;

    public LastPosition1Fragment() {

    }

    public LastPosition1Fragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_lastposition1, container, false);

        this.ll_container_lastposition1 = (LinearLayout) this.view.findViewById(R.id.ll_container_lastposition1);
        this.lv_lastposition1 = (PullToRefreshListView) this.view.findViewById(R.id.lv_lastposition1);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLastPosition1ProgressDialog();

        if (Utility.ConnectionUtility.isNetworkConnected(this.appCompatActivity)) {
            this.progressDialog.show();
            doObtainLastPosition();
        }else {
            this.snackbar = Snackbar.make(this.ll_container_lastposition1, "No internet connection.", Snackbar.LENGTH_SHORT);
            this.snackbar.setAction("Dismiss", new DismissLastPosition1Snackbar());
            this.snackbar.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        this.progressDialog.dismiss();
        snackbar = Snackbar.make(ll_container_lastposition1, "Failed obtain data from.", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Dismiss", new DismissLastPosition1Snackbar());
        snackbar.show();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        responseString[0] = response.body().string();
        Log.d(TAG, responseString[0]);
        this.appCompatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
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

    private void initLastPositionDataToList(String response) {
        try {
            this.lastPositionList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            this.lv_lastposition1.setOnRefreshListener(new RefreshLastPosition1Data());
            registerForContextMenu(this.lv_lastposition1);
            this.rv_lastposition1_adapter = new LastPosition1Adapter(this.appCompatActivity, this.lastPositionList, this);
            this.lv_lastposition1.setAdapter(this.rv_lastposition1_adapter);
            this.lv_lastposition1.setOnItemClickListener(new HighlightLastPosition1Data());
            this.lv_lastposition1.setOnItemLongClickListener(new GoToTrackingOnMapView());
        } catch (JSONException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        } catch (ParseException e) {
            Log.d(TAG, "Exception " +e.getMessage());
        }
    }

    private void setLastPosition1ProgressDialog() {
        this.progressDialog = new ProgressDialog(this.appCompatActivity);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading... please wait.");
    }

    @Override
    public void SelectLastPositionDataToTracking(int p) {
        Intent intentData = new Intent();
        intentData.putExtra("asset_pos", p);
        intentData.putExtra("asset_code", this.lastPositionList.get(p).getAssetCode());
        this.appCompatActivity.setResult(Activity.RESULT_OK, intentData);
        this.appCompatActivity.finish();
    }

    class DismissLastPosition1Snackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }

    class HighlightLastPosition1Data implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            //lv_actual_lastposition1.setItemChecked(position, true);
            Log.d(TAG, "pos " + position);
            rv_lastposition1_adapter.setSelectedPosition(position - 1);
            rv_lastposition1_adapter.notifyDataSetChanged();
        }
    }

    class GoToTrackingOnMapView implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intentData = new Intent();
            intentData.putExtra("asset_pos", position-1);
            intentData.putExtra("asset_code", lastPositionList.get(position-1).getAssetCode());
            appCompatActivity.setResult(Activity.RESULT_OK, intentData);
            appCompatActivity.finish();

            return false;
        }
    }

    class RefreshLastPosition1Data implements PullToRefreshBase.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                progressDialog.show();
                doObtainLastPosition();
                lv_lastposition1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_lastposition1.onRefreshComplete();
                    }
                }, 1000);
            }else {
                snackbar = Snackbar.make(ll_container_lastposition1, "No internet connection.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissLastPosition1Snackbar());
                snackbar.show();
            }
        }
    }

}
