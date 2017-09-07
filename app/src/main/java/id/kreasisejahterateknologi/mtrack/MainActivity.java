package id.kreasisejahterateknologi.mtrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

import adapter.MainViewPagerAdapter;
import callback.MainCallback;
import callback.MainViewPagerAdapterCallback;
import fragment.AboutFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utility.SharedPreferencesProvider;

public class MainActivity extends AppCompatActivity implements Callback, MainViewPagerAdapterCallback {
    private final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar_main;
    private TabLayout tabs_main;
    private ViewPager vp_main;
    private MainViewPagerAdapter vp_main_adapter;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private int dialogMode = -1;
    private MainCallback mainCallback;
    private AboutFragment aboutFragment;
    private String[] tabTitles = {"DASHBOARD", "TRACKING ON MAP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(this.toolbar_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.tabs_main = (TabLayout) findViewById(R.id.tabs_main);
        this.vp_main = (ViewPager) findViewById(R.id.vp_main);

        this.navigationView = (NavigationView) findViewById(R.id.navigationView);
        this.navigationView.setCheckedItem(R.id.menu_tracking);
        this.navigationView.setItemIconTintList(null);
        this.navigationView.setNavigationItemSelectedListener(new ActionSelectMenu());

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }

        };
        this.drawerLayout.setDrawerListener(this.actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();

        setCompanyNameOnHeader();

        ShowTrackingContent();
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
    }

    @Override
    public void RefreshCustomViewTabLayout() {
//        for (int i = 0;i < this.tabs_main.getTabCount(); i++) {
//            setIconTabLayoutBasedOnIndeks(i);
//        }
    }

    @Override
    public void SelectLastPositionToTrackingOnMap(Intent data) {
        Log.d(TAG, "pos" + data.getIntExtra("asset_pos", 0));
        if (this.mainCallback == null)
            this.mainCallback = this.vp_main_adapter.getMainCallback();
        this.mainCallback.SendLastPositionPos(data);
        this.vp_main.setCurrentItem(1);
    }

    class ActionSelectMenu implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            drawerLayout.closeDrawers();

            switch (item.getItemId()) {
                case R.id.menu_tracking:
                    ShowTrackingContent();

                    return true;

                case R.id.menu_report:
                    ShowReportView();

                    return false;

                case R.id.menu_logout:
                    Logout();

                    return false;

                case R.id.menu_about:
                    ShowAboutPopup();

                    return false;

                default:
                    Snackbar.make(navigationView, "Somethings wrong happens", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
            }
        }
    }

    private void ShowTrackingContent() {
        this.vp_main_adapter = new MainViewPagerAdapter(getSupportFragmentManager(), this, this);
        this.vp_main.setAdapter(this.vp_main_adapter);
        this.vp_main.setOffscreenPageLimit(this.vp_main_adapter.getCount() - 1);
        this.tabs_main.setupWithViewPager(this.vp_main);

        for (int i = 0;i < this.tabs_main.getTabCount(); i++) {
            setIconTabLayoutBasedOnIndeks(i);
        }
    }

    private void setIconTabLayoutBasedOnIndeks(int indeks) {
        TabLayout.Tab tab = this.tabs_main.getTabAt(indeks);
        if(tab != null) {
            tab.setCustomView(this.vp_main_adapter.getTabView(indeks));
            ((TextView) tab.getCustomView().findViewById(R.id.tv_title_tab_widget)).setText(this.tabTitles[indeks]);
        }
    }

    private void ShowReportView() {
        Intent reportIntent = new Intent(this, ReportActivity.class);
        startActivityForResult(reportIntent, 10);
    }

    private void Logout() {
        SharedPreferencesProvider.getInstance().setLogin(this, false);
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        MainActivity.this.finish();
    }

    private void ShowAboutPopup() {
        this.aboutFragment = new AboutFragment(this);
        this.aboutFragment.setCancelable(true);
        this.aboutFragment.show(getSupportFragmentManager(), "dialogAbout");
    }

    private void ShowCloseDialog() {
        this.alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Quit MTrack?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        this.alertDialog = this.alertDialogBuilder.create();
        this.alertDialog.show();
    }

    private void setCompanyNameOnHeader() {
        View view = this.navigationView.getHeaderView(0);
        ((TextView) view.findViewById(R.id.tv_title_header)).setText(SharedPreferencesProvider.getInstance().getAccCompanyName(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.navigationView.setCheckedItem(R.id.menu_tracking);
    }

    @Override
    public void onBackPressed() {
        ShowCloseDialog();
        Log.d(TAG, "" + this.dialogMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (this.mainCallback == null)
                this.mainCallback = this.vp_main_adapter.getMainCallback();
            this.mainCallback.SendLastPositionPos(data);
            this.vp_main.setCurrentItem(1);
            Log.d(TAG, data.getStringExtra("asset_code"));
            Log.d(TAG, "pos" + data.getIntExtra("asset_pos", 0));
        }
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
            boolean isDrawerOpen = drawerLayout.isDrawerOpen(navigationView);
            if (isDrawerOpen == false)
                drawerLayout.openDrawer(navigationView);
            else
                drawerLayout.closeDrawer(navigationView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
