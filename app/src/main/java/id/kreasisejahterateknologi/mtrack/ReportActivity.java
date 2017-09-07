package id.kreasisejahterateknologi.mtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import adapter.ReportViewPagerAdapter;

/**
 * Created by Andy on 5/19/2016.
 */
public class ReportActivity extends AppCompatActivity {
    private final String TAG = ReportActivity.class.getSimpleName();

    private TabLayout tabs_report;
    private ViewPager vp_report;
    private ReportViewPagerAdapter vp_report_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Report");
        getSupportActionBar().setSubtitle(R.string.app_name);

        this.tabs_report = (TabLayout) findViewById(R.id.tabs_report);
        this.vp_report = (ViewPager) findViewById(R.id.vp_report);

        this.vp_report_adapter = new ReportViewPagerAdapter(getSupportFragmentManager(), this);
        this.vp_report.setAdapter(this.vp_report_adapter);
        this.vp_report.setOffscreenPageLimit(this.vp_report_adapter.getCount() - 1);
        this.tabs_report.setupWithViewPager(this.vp_report);

        for (int i = 0;i < tabs_report.getTabCount(); i++) {
            setIconTabLayoutBasedOnIndeks(i);
        }
    }

    private void setIconTabLayoutBasedOnIndeks(int indeks) {
        TabLayout.Tab tab = this.tabs_report.getTabAt(indeks);
        if(tab != null) {
            tab.setCustomView(this.vp_report_adapter.getTabView(indeks));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode " + requestCode);
        Log.d(TAG, data.getStringExtra("asset_code"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            ReportActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
