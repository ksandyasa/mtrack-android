package fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/23/2016.
 */
@SuppressLint("ValidFragment")
public class AlertFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private View view;

    public AlertFragment() {

    }

    public AlertFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_alert, container, false);

        return this.view;
    }
}
