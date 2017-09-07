package fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import id.kreasisejahterateknologi.mtrack.R;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.ParkingAssetAdapter;
import adapter.ReportHistoricalAdapter;
import callback.ParkingAssetAdapterCallback;
import model.Historical;
import model.LastPosition;
import model.Vehicle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utility.ConstantAPI;
import utility.SharedPreferencesProvider;
import utility.Utility;
import widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 6/8/16.
 */
@SuppressLint("ValidFragment")
public class HistoricalFragment extends Fragment {
    private final String TAG = HistoricalFragment.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private View view;
    private LinearLayout ll_container_historical;
    private EditText ed_from_historical, ed_to_historical;
    private ImageButton btn_from_date_historical;
    private Button btn_viewreport_historical;
    private RecyclerView rv_asset_historical;
    private LinearLayoutManager rv_asset_historical_llm;
    private ParkingAssetAdapter rv_asset_historical_adapter;
    private DatePickerDialog fromDatePicker;
    private TimePickerDialog fromTimePicker;
    private LinearLayout ll_content1_historical;
    private RelativeLayout rl_content2_historical;
    private RelativeLayout rl_refresh_asset_historical;
    private ImageView iv_refresh_asset_historical;
    private LinearLayout ll_report_historical;
    private HorizontalScrollView hsv_report_historical;
    private RecyclerView rv_report_historical;
    private LinearLayoutManager rv_report_historical_llm;
    private TextView tv_assetreport_historical, tv_datereport_historical;
    private ImageView iv_scrollreport_historical;
    private ReportHistoricalAdapter rv_report_historical_adapter;
    private List<LastPosition> vehicleList = new ArrayList<>();
    private List<Historical> historicalList = new ArrayList<>();
    private String fromDateString, toDateString, date1String, date2String, startDateString, endDateString;
    private String[] responseString = {""};
    private int selectedParkingAssetPosition = 0;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;

    public HistoricalFragment() {

    }

    public HistoricalFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_historical, container, false);

        this.ll_container_historical = (LinearLayout) this.view.findViewById(R.id.ll_container_historical);
        this.ll_content1_historical = (LinearLayout) this.view.findViewById(R.id.ll_content1_historical);
        this.ed_from_historical = (EditText) this.view.findViewById(R.id.ed_from_historical);
        this.ed_to_historical = (EditText) this.view.findViewById(R.id.ed_to_historical);
        this.btn_from_date_historical = (ImageButton) this.view.findViewById(R.id.btn_from_date_historical);
        this.rv_asset_historical = (RecyclerView) this.view.findViewById(R.id.rv_asset_historical);
        this.rl_content2_historical = (RelativeLayout) this.view.findViewById(R.id.rl_content2_historical);
        this.rl_refresh_asset_historical = (RelativeLayout) this.view.findViewById(R.id.rl_refresh_asset_historical);
        this.iv_refresh_asset_historical = (ImageView) this.view.findViewById(R.id.iv_refresh_asset_historical);
        this.btn_viewreport_historical = (Button) this.view.findViewById(R.id.btn_viewreport_historical);
        this.ll_report_historical = (LinearLayout) this.view.findViewById(R.id.ll_report_historical);
        this.tv_assetreport_historical = (TextView) this.view.findViewById(R.id.tv_assetreport_historical);
        this.tv_datereport_historical = (TextView) this.view.findViewById(R.id.tv_datereport_historical);
        this.iv_scrollreport_historical = (ImageView) this.view.findViewById(R.id.iv_scrollreport_historical);
        this.hsv_report_historical = (HorizontalScrollView) this.view.findViewById(R.id.hsv_report_historical);
        this.rv_report_historical = (RecyclerView) this.view.findViewById(R.id.rv_report_historical);

        this.rv_asset_historical_llm = new LinearLayoutManager(this.appCompatActivity);
        this.rv_asset_historical.setHasFixedSize(true);
        this.rv_asset_historical.setLayoutManager(this.rv_asset_historical_llm);
        this.rv_asset_historical.addItemDecoration(new RecycleDividerItemDecoration(this.appCompatActivity));

        this.rv_report_historical_llm = new LinearLayoutManager(this.appCompatActivity);
        this.rv_report_historical.setHasFixedSize(true);
        this.rv_report_historical.setLayoutManager(this.rv_report_historical_llm);
        this.rv_report_historical.addItemDecoration(new RecycleDividerItemDecoration(this.appCompatActivity));

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDateAndTimePickerDialog();
        setParkingProgressDialog();

        this.btn_from_date_historical.setOnClickListener(new ShowFromDate());
        this.iv_refresh_asset_historical.setOnClickListener(new ReloadVehicleData());

        if (Utility.ConnectionUtility.isNetworkConnected(this.appCompatActivity))
            doObtainAllVehicleData();
        else {
            this.rl_refresh_asset_historical.startAnimation(Utility.DisplayUtility.setAnimationToView(this.appCompatActivity));
            this.rv_asset_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(this.appCompatActivity));
            this.rl_refresh_asset_historical.setVisibility(View.VISIBLE);
            this.rv_asset_historical.setVisibility(View.GONE);
            this.snackbar = Snackbar.make(this.rl_content2_historical, "No internet connection", Snackbar.LENGTH_SHORT);
            this.snackbar.setAction("Dismiss", new DismissLastPosition1Snackbar());
            this.snackbar.show();
        }

        this.iv_scrollreport_historical.setOnClickListener(new ScrollReportView());
        this.btn_viewreport_historical.setOnClickListener(new ViewReportVehicle());
    }

    private void setDateAndTimePickerDialog() {
        this.fromDatePicker = DatePickerDialog.newInstance(
                new FromDateTimePicker(),
                Utility.CalendarUtility.getCalendarInstance().get(Calendar.YEAR),
                Utility.CalendarUtility.getCalendarInstance().get(Calendar.MONTH),
                Utility.CalendarUtility.getCalendarInstance().get(Calendar.DAY_OF_MONTH)
        );

        this.fromTimePicker = TimePickerDialog.newInstance(
                new FromDateTimePicker(),
                Utility.CalendarUtility.getCalendarInstance().get(Calendar.HOUR_OF_DAY),
                Utility.CalendarUtility.getCalendarInstance().get(Calendar.MINUTE),
                false
        );
    }

    private void doObtainAllVehicleData() {
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
        call.enqueue(new VehicleCallback());
    }

    private void doObtainReportVehicleData() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ConstantAPI.getHistoryVehicleURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                        SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                        vehicleList.get(selectedParkingAssetPosition).getAssetId(),
                        date1String, date2String))
                .build();
        Log.d(TAG, ConstantAPI.getHistoryVehicleURL(SharedPreferencesProvider.getInstance().getUid(this.appCompatActivity),
                SharedPreferencesProvider.getInstance().getAccID(this.appCompatActivity),
                vehicleList.get(selectedParkingAssetPosition).getAssetId(),
                date1String, date2String));

        Call call = okHttpClient.newCall(request);
        call.enqueue(new ReportVehicleCallback());
    }

    private void InitAllVehicleDataToList(String response) {
        try {
            this.vehicleList = Utility.JSONUtility.getListLastPositionFromJSON(response);
            this.rv_asset_historical_adapter = new ParkingAssetAdapter(this.appCompatActivity, this.vehicleList, new VehicleDataCallback());
            this.rv_asset_historical.setAdapter(this.rv_asset_historical_adapter);
        } catch (Exception e) {
            Log.d(TAG, "Exception " + e.getMessage());
        }
    }

    private void InitAllParkingDataToList(String response) {
        try {
            this.progressDialog.dismiss();
            this.historicalList = Utility.JSONUtility.getListHistoricalFromJSON(response);
            this.rv_report_historical_adapter = new ReportHistoricalAdapter(this.appCompatActivity, this.historicalList);
            this.rv_report_historical.setAdapter(this.rv_report_historical_adapter);
            this.tv_assetreport_historical.setText(this.vehicleList.get(selectedParkingAssetPosition).getAssetCode());
            this.tv_datereport_historical.setText("From " + fromDateString + " To " + toDateString);
            if (!this.ll_report_historical.isShown()) {
                this.hsv_report_historical.requestLayout();
                this.rv_report_historical.requestLayout();
                this.ll_report_historical.setVisibility(View.VISIBLE);
                this.ll_content1_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(this.appCompatActivity));
                this.rl_content2_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(this.appCompatActivity));
                this.ll_content1_historical.setVisibility(View.GONE);
                this.rl_content2_historical.setVisibility(View.GONE);
                this.iv_scrollreport_historical.setImageResource(R.drawable.icon_double_down);
            }
            this.hsv_report_historical.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hsv_report_historical.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    onResume();
                }
            }, 300L);
        } catch (JSONException e) {
            Log.d(TAG, "Exception " + e.getMessage());

            Toast.makeText(this.appCompatActivity, "No data for this asset " + this.vehicleList.get(this.selectedParkingAssetPosition).getAssetCode(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setParkingProgressDialog() {
        this.progressDialog = new ProgressDialog(this.appCompatActivity);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading... please wait.");
    }

    class DismissLastPosition1Snackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }

    class ScrollReportView implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if (ll_content1_historical.isShown()) {
                hsv_report_historical.requestLayout();
                rv_report_historical.requestLayout();
                ll_content1_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(appCompatActivity));
                rl_content2_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(appCompatActivity));
                ll_content1_historical.setVisibility(View.GONE);
                rl_content2_historical.setVisibility(View.GONE);
                iv_scrollreport_historical.setImageResource(R.drawable.icon_double_down);
                hsv_report_historical.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hsv_report_historical.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 200L);
            }else{
                hsv_report_historical.requestLayout();
                ll_content1_historical.startAnimation(Utility.DisplayUtility.setAnimationToView(appCompatActivity));
                rl_content2_historical.startAnimation(Utility.DisplayUtility.setAnimationToView(appCompatActivity));
                ll_content1_historical.setVisibility(View.VISIBLE);
                rl_content2_historical.setVisibility(View.VISIBLE);
                iv_scrollreport_historical.setImageResource(R.drawable.icon_double_up);
                hsv_report_historical.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hsv_report_historical.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 200L);
            }
        }
    }

    class ViewReportVehicle implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (ed_from_historical.getText().toString().equals("") && ed_to_historical.getText().toString().equals("")) {
                Snackbar.make(ll_container_historical, "Please fill From and To field", Snackbar.LENGTH_SHORT).show();
            }else if (ed_from_historical.getText().toString().equals("") && !ed_to_historical.getText().toString().equals("")) {
                Snackbar.make(ll_container_historical, "Please fill From field", Snackbar.LENGTH_SHORT).show();
            }else if (!ed_from_historical.getText().toString().equals("") && ed_to_historical.getText().toString().equals("")) {
                Snackbar.make(ll_container_historical, "Please fill To field", Snackbar.LENGTH_SHORT).show();
            }else{
                try {
                    String[] durations = Utility.CalendarUtility.getDurationOfLastPosition(Utility.CalendarUtility.getDateFromString(startDateString),
                            Utility.CalendarUtility.getDateFromString(endDateString)).split(", ");
                    int durationDay = Integer.parseInt(durations[0].substring(0, durations[0].indexOf(" ")));
                    if (durationDay < 5) {
                        progressDialog.show();
                        doObtainReportVehicleData();
                    }else {
                        new AlertDialog.Builder(appCompatActivity)
                                .setCancelable(false)
                                .setMessage("Maximal input date no more than 5 days!")
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (ParseException e) {
                    Log.d(TAG, "Exception " + e.getMessage());
                }

            }
        }
    }

    class VehicleDataCallback implements ParkingAssetAdapterCallback {

        @Override
        public void ObtainVehicleData(int pos) {
            selectedParkingAssetPosition = pos;
            Log.d(TAG, "selectedParkingAssetPosition " + selectedParkingAssetPosition);
        }
    }

    class ShowFromDate implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            fromDatePicker.show(appCompatActivity.getFragmentManager(), "Datepickerdialog");
        }
    }

    class VehicleCallback implements Callback {

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
                    InitAllVehicleDataToList(responseString[0]);
                }
            });
        }
    }

    class ReportVehicleCallback implements Callback {

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
                    InitAllParkingDataToList(responseString[0]);
                }
            });
        }
    }

    class ReloadVehicleData implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Utility.ConnectionUtility.isNetworkConnected(appCompatActivity)) {
                rl_refresh_asset_historical.startAnimation(Utility.DisplayUtility.setZeroAlphaAnimationToView(appCompatActivity));
                rv_asset_historical.startAnimation(Utility.DisplayUtility.setAnimationToView(appCompatActivity));
                rl_refresh_asset_historical.setVisibility(View.GONE);
                rv_asset_historical.setVisibility(View.VISIBLE);
                doObtainAllVehicleData();
            }else {
                snackbar = Snackbar.make(rl_content2_historical, "No internet connection", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissLastPosition1Snackbar());
                snackbar.show();
            }
        }
    }

    class FromDateTimePicker implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
            String monthFix = (monthOfYear < 10) ? "0" + String.valueOf(monthOfYear + 1) : "" + String.valueOf(monthOfYear + 1);
            String monthEndFix = (monthOfYearEnd < 10) ? "0" + String.valueOf(monthOfYearEnd + 1) : "" + String.valueOf(monthOfYearEnd + 1);
            fromDateString = "" + dayOfMonth + "-" + monthFix + "-" + year;
            date1String = "" + year + "-" + monthFix + "-" + dayOfMonth;
            startDateString = "" + year + "-" + monthFix + "-" + dayOfMonth;
            toDateString = "" + dayOfMonthEnd + "-" + monthEndFix + "-" + yearEnd;
            date2String = "" + yearEnd + "-" + monthEndFix + "-" + dayOfMonthEnd;
            endDateString = "" + yearEnd + "-" + monthEndFix + "-" + dayOfMonthEnd;
            fromTimePicker.show(appCompatActivity.getFragmentManager(), "Timepickerdialog");
        }

        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
            String hourFix = (hourOfDay < 10) ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
            String minuteFix = (minute < 10) ? "0" + String.valueOf(minute) : String.valueOf(minute);
            fromDateString = fromDateString + " " + hourFix + ":" + minuteFix;
            startDateString = startDateString + " " + hourFix + ":" + minuteFix;
            String hourEndFix = (hourOfDayEnd < 10) ? "0" + String.valueOf(hourOfDayEnd) : String.valueOf(hourOfDayEnd);
            String minuteEndFix = (minuteEnd < 10) ? "0" + String.valueOf(minuteEnd) : String.valueOf(minuteEnd);
            toDateString = toDateString + " " + hourEndFix + ":" + minuteEndFix;
            endDateString = endDateString + " " + hourEndFix + ":" + minuteEndFix;
            ed_from_historical.setText(fromDateString);
            ed_to_historical.setText(toDateString);
        }
    }

}
