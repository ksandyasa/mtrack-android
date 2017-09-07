package fragment;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by apridosandyasa on 5/30/16.
 */
@SuppressLint("ValidFragment")
public class AboutFragment extends DialogFragment {
    private final String TAG = AboutFragment.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private View view;
    private TextView tv_title_dialog_about;
    private TextView tv_version_dialog_about;

    public AboutFragment() {

    }

    public AboutFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setAttributes(windowParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_about_bg);

        this.view = inflater.inflate(R.layout.dialog_about, container, false);

        this.tv_title_dialog_about = (TextView) this.view.findViewById(R.id.tv_title_dialog_about);
        this.tv_version_dialog_about = (TextView) this.view.findViewById(R.id.tv_version_dialog_about);

        Typeface tf = Typeface.createFromAsset(this.appCompatActivity.getAssets(), "fonts/ComicSansMSRegular.ttf");

        this.tv_title_dialog_about.setText("Mobile M-Track");
        this.tv_title_dialog_about.setTypeface(tf);

        try {
            PackageInfo pInfo = this.appCompatActivity.getPackageManager().getPackageInfo(this.appCompatActivity.getPackageName(), 0);
            this.tv_version_dialog_about.setText("Version " + pInfo.versionName);
            this.tv_version_dialog_about.setTypeface(tf);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "Exception "+ e.getMessage());
        }

        return this.view;
    }
}
