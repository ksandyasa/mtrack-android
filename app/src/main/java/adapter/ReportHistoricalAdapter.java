package adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

import java.util.List;

import model.Historical;

/**
 * Created by apridosandyasa on 6/8/16.
 */
public class ReportHistoricalAdapter extends RecyclerView.Adapter<ReportHistoricalAdapter.ReportHistoricalViewHolder> {
    private AppCompatActivity appCompatActivity;
    private List<Historical> historicalList;
    private int selectedPosition = 0;

    public ReportHistoricalAdapter(AppCompatActivity aca, List<Historical> objects) {
        this.appCompatActivity = aca;
        this.historicalList = objects;
    }

    @Override
    public ReportHistoricalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_historical, parent, false);
        ReportHistoricalViewHolder reportHistoricalViewHolder = new ReportHistoricalViewHolder(view);
        return reportHistoricalViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportHistoricalViewHolder holder, int position) {
        holder.tv_indeks_item_historical.setText(String.valueOf(position + 1));
        holder.tv_assetcode_item_historical.setText(this.historicalList.get(position).getAssetCode());
        holder.tv_date_item_historical.setText(this.historicalList.get(position).getTimestamp());
        holder.tv_address_item_historical.setText(this.historicalList.get(position).getAddress());
        holder.tv_status_item_historical.setText(this.historicalList.get(position).getInputMask());
        holder.tv_engine_item_historical.setText(this.historicalList.get(position).getStatusCode());
        holder.tv_distance_item_historical.setText(this.historicalList.get(position).getDistance());
        holder.tv_speed_item_historical.setText(this.historicalList.get(position).getSpeed());
        if (this.selectedPosition == position) {
            holder.ll_container_item_historical.setBackgroundResource(R.drawable.list_item_lastposition1_blue);
            holder.tv_indeks_item_historical.setTextColor(Color.WHITE);
            holder.tv_assetcode_item_historical.setTextColor(Color.WHITE);
            holder.tv_date_item_historical.setTextColor(Color.WHITE);
            holder.tv_address_item_historical.setTextColor(Color.WHITE);
            holder.tv_status_item_historical.setTextColor(Color.WHITE);
            holder.tv_engine_item_historical.setTextColor(Color.WHITE);
            holder.tv_distance_item_historical.setTextColor(Color.WHITE);
            holder.tv_speed_item_historical.setTextColor(Color.WHITE);
        }
        else {
            holder.ll_container_item_historical.setBackgroundResource(R.drawable.list_item_lastposition1_bg);
            holder.tv_indeks_item_historical.setTextColor(Color.BLACK);
            holder.tv_assetcode_item_historical.setTextColor(Color.BLACK);
            holder.tv_date_item_historical.setTextColor(Color.BLACK);
            holder.tv_address_item_historical.setTextColor(Color.BLACK);
            holder.tv_status_item_historical.setTextColor(Color.BLACK);
            holder.tv_engine_item_historical.setTextColor(Color.BLACK);
            holder.tv_distance_item_historical.setTextColor(Color.BLACK);
            holder.tv_speed_item_historical.setTextColor(Color.BLACK);
        }
        holder.ll_container_item_historical.setOnClickListener(new HighlightReportHistoricalData(position));
    }

    @Override
    public int getItemCount() {
        return this.historicalList.size();
    }

    class HighlightReportHistoricalData implements View.OnClickListener {
        private int vPosition;

        public HighlightReportHistoricalData(int p) {
            this.vPosition = p;
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = vPosition;
            notifyItemChanged(selectedPosition);
        }
    }

    public static class ReportHistoricalViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_container_item_historical;
        private TextView tv_indeks_item_historical;
        private TextView tv_assetcode_item_historical;
        private TextView tv_date_item_historical;
        private TextView tv_address_item_historical;
        private TextView tv_status_item_historical;
        private TextView tv_engine_item_historical;
        private TextView tv_distance_item_historical;
        private TextView tv_speed_item_historical;

        ReportHistoricalViewHolder(View view) {
            super(view);
            this.ll_container_item_historical = (LinearLayout) view.findViewById(R.id.ll_container_item_historical);
            this.tv_indeks_item_historical = (TextView) view.findViewById(R.id.tv_indeks_item_historical);
            this.tv_assetcode_item_historical = (TextView) view.findViewById(R.id.tv_assetcode_item_historical);
            this.tv_date_item_historical = (TextView) view.findViewById(R.id.tv_date_item_historical);
            this.tv_address_item_historical = (TextView) view.findViewById(R.id.tv_address_item_historical);
            this.tv_status_item_historical = (TextView) view.findViewById(R.id.tv_status_item_historical);
            this.tv_engine_item_historical = (TextView) view.findViewById(R.id.tv_engine_item_historical);
            this.tv_distance_item_historical = (TextView) view.findViewById(R.id.tv_distance_item_historical);
            this.tv_speed_item_historical = (TextView) view.findViewById(R.id.tv_speed_item_historical);
        }
    }

}
