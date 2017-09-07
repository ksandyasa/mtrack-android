package dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import callback.SearchTrackingDialogCallback;
import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/19/2016.
 */
@SuppressLint("ValidFragment")
public class SearchTrackingDialog extends AppCompatDialogFragment {
    private AppCompatActivity appCompatActivity;
    private View view;
    private EditText ed_search_tracking;
    private Button btn_search_tracking;
    private SearchTrackingDialogCallback searchTrackingDialogCallback;

    public SearchTrackingDialog() {

    }

    public SearchTrackingDialog(AppCompatActivity aca, SearchTrackingDialogCallback listener) {
        this.appCompatActivity = aca;
        this.searchTrackingDialogCallback = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.dialog_search_tracking, container, false);

        this.ed_search_tracking = (EditText) this.view.findViewById(R.id.ed_search_tracking);
        this.btn_search_tracking = (Button) this.view.findViewById(R.id.btn_search_tracking);

        this.btn_search_tracking.setOnClickListener(new CloseSearchTrackingDialog());

        return this.view;
    }

    class CloseSearchTrackingDialog implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            searchTrackingDialogCallback.setNoVehicle(ed_search_tracking.getText().toString().toUpperCase(Locale.getDefault()));
            dismiss();
        }
    }
}
