package adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/19/2016.
 */
public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {
    private AppCompatActivity appCompatActivity;
    private String[] items;

    public AlertAdapter(AppCompatActivity aca, String[] objects) {
        this.appCompatActivity = aca;
        this.items = objects;
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report, parent, false);
        AlertViewHolder alertViewHolder = new AlertViewHolder(view);
        return alertViewHolder;
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        holder.tv_title_item_report.setText(this.items[position]);
    }

    @Override
    public int getItemCount() {
        return this.items.length;
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item_report;
        private CardView cv_item_report;
        private RelativeLayout rl_item_report;
        private TextView tv_title_item_report;

        public AlertViewHolder(View view) {
            super(view);
            this.ll_item_report = (LinearLayout) view.findViewById(R.id.ll_item_report);
            this.cv_item_report = (CardView) view.findViewById(R.id.cv_item_report);
            this.rl_item_report = (RelativeLayout) view.findViewById(R.id.rl_item_report);
            this.tv_title_item_report = (TextView) view.findViewById(R.id.tv_title_item_report);
        }
    }
}
