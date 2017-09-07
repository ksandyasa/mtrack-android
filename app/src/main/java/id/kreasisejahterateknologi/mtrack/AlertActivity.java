package id.kreasisejahterateknologi.mtrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import adapter.AlertAdapter;

/**
 * Created by Andy on 5/19/2016.
 */
public class AlertActivity extends AppCompatActivity{
    private RecyclerView rv_alert;
    private AlertAdapter rv_alert_adapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        this.rv_alert = (RecyclerView) findViewById(R.id.rv_alert);
        this.gridLayoutManager = new GridLayoutManager(this, 2);
        this.rv_alert.setHasFixedSize(true);
        this.rv_alert.setLayoutManager(this.gridLayoutManager);

        this.rv_alert_adapter = new AlertAdapter(this, getResources().getStringArray(R.array.alert_items));
        this.rv_alert.setAdapter(this.rv_alert_adapter);
    }
}