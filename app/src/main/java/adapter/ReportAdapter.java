package adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.AlertActivity;
import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/19/2016.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private AppCompatActivity appCompatActivity;
    private String[] items;

    public ReportAdapter(AppCompatActivity aca, String[] objects) {
        this.appCompatActivity = aca;
        this.items = objects;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report, parent, false);
        ReportViewHolder reportViewHolder = new ReportViewHolder(view);
        return reportViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        holder.tv_title_item_report.setText(this.items[position]);
        if (position == 2)
            holder.cv_item_report.setOnClickListener(new ShowAlertView());
    }

    @Override
    public int getItemCount() {
        return this.items.length;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item_report;
        private CardView cv_item_report;
        private RelativeLayout rl_item_report;
        private TextView tv_title_item_report;

        public ReportViewHolder(View view) {
            super(view);
            this.ll_item_report = (LinearLayout) view.findViewById(R.id.ll_item_report);
            this.cv_item_report = (CardView) view.findViewById(R.id.cv_item_report);
            this.rl_item_report = (RelativeLayout) view.findViewById(R.id.rl_item_report);
            this.tv_title_item_report = (TextView) view.findViewById(R.id.tv_title_item_report);
        }
    }

    class ShowAlertView implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent alertIntent = new Intent(appCompatActivity, AlertActivity.class);
            appCompatActivity.startActivity(alertIntent);
        }
    }
}
