package adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

import java.util.ArrayList;
import java.util.List;

import callback.LastPositionCallback;
import callback.MainCallback;
import callback.MainFragmentCallback;
import callback.MainViewPagerAdapterCallback;
import callback.TrackingFragmentCallback;
import fragment.LastPositionFragment;
import fragment.MainFragment;
import fragment.TrackingFragment;
import model.LastPosition;

/**
 * Created by Andy on 5/25/2016.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter implements LastPositionCallback,
        TrackingFragmentCallback, MainCallback, MainFragmentCallback {
    private final String TAG = MainViewPagerAdapter.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private MainViewPagerAdapterCallback mainViewPagerAdapterCallback;

    private String[] tabTitles = {"DASHBOARD", "TRACKING ON MAP"};

    public MainViewPagerAdapter(FragmentManager fm, AppCompatActivity aca, MainViewPagerAdapterCallback listener) {
        super(fm);
        this.appCompatActivity = aca;
        this.fragmentManager = fm;
        this.mainViewPagerAdapterCallback = listener;
        initFragmentListData(this.appCompatActivity);
    }

    @Override
    public Fragment getItem(int position) {
        return (position != 1) ? this.fragmentList.get(0) : this.fragment;
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof LastPositionFragment && fragment instanceof TrackingFragment)
            return POSITION_NONE;
        if (object instanceof TrackingFragment && fragment instanceof LastPositionFragment)
            return POSITION_NONE;

        return POSITION_UNCHANGED;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this.appCompatActivity).inflate(R.layout.custom_tab_layout, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title_tab_widget);
        tv_title.setText(this.tabTitles[position]);

        return view;
    }

    public MainCallback getMainCallback() {
        return this;
    }

    private void initFragmentListData(AppCompatActivity appCompatActivity) {
        this.fragment = new LastPositionFragment(appCompatActivity, this);
        this.fragmentList = new ArrayList<>();
        this.fragmentList.add(new MainFragment(appCompatActivity, this  ));
        this.fragmentList.add(new LastPositionFragment(appCompatActivity, this));
    }

    @Override
    public void SendLastPositionDataToTracking(LastPosition lastPosition, int position) {
        Log.d(TAG, "showTrackingView");
        this.fragmentManager.beginTransaction().remove(this.fragment);
        fragment = new TrackingFragment(this.appCompatActivity, lastPosition, position, this);
        notifyDataSetChanged();
        mainViewPagerAdapterCallback.RefreshCustomViewTabLayout();
    }

    @Override
    public void ShowLastPositionView() {
        Log.d(TAG, "showLastPositionView");
        this.fragmentManager.beginTransaction().remove(this.fragment);
        fragment = new LastPositionFragment(this.appCompatActivity, this);
        notifyDataSetChanged();
        mainViewPagerAdapterCallback.RefreshCustomViewTabLayout();
    }

    @Override
    public void SendLastPositionPos(Intent data) {
        ((LastPositionFragment) this.fragment).SelectPostLastPosition(data.getIntExtra("asset_pos", 0));
    }

    @Override
    public void SelectLastPositionFromDashboard(Intent data) {
        Log.d(TAG, "pos" + data.getIntExtra("asset_pos", 0));
        mainViewPagerAdapterCallback.SelectLastPositionToTrackingOnMap(data);
    }
}
