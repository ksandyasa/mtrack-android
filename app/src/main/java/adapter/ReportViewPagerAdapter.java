package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fragment.HistoricalFragment;
import fragment.LastPosition1Fragment;
import fragment.ParkingFragment;

import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/23/2016.
 */
public class ReportViewPagerAdapter extends FragmentStatePagerAdapter {
    private AppCompatActivity appCompatActivity;
    private List<Fragment> fragmentList;
    private String[] tabTitles = {"LAST POSITION", "PARKING", "HISTORICAL   "};

    public ReportViewPagerAdapter(FragmentManager fm, AppCompatActivity aca) {
        super(fm);
        this.appCompatActivity = aca;
        initFragmentListData(this.appCompatActivity);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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

    private void initFragmentListData(AppCompatActivity appCompatActivity) {
        this.fragmentList = new ArrayList<>();
        this.fragmentList.add(new LastPosition1Fragment(appCompatActivity));
        this.fragmentList.add(new ParkingFragment(appCompatActivity));
        this.fragmentList.add(new HistoricalFragment(appCompatActivity));
    }
}
