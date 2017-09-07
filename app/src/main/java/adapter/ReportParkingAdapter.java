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

import model.Parking;

/**
 * Created by apridosandyasa on 5/30/16.
 */
public class ReportParkingAdapter extends RecyclerView.Adapter<ReportParkingAdapter.ReportParkingViewHolder> {
    private AppCompatActivity appCompatActivity;
    private List<Parking> parkingList;
    private int selectedPosition = 0;

    public ReportParkingAdapter(AppCompatActivity aca, List<Parking> objects) {
        this.appCompatActivity = aca;
        this.parkingList = objects;
    }

    @Override
    public ReportParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_parking, parent, false);
        ReportParkingViewHolder reportParkingViewHolder = new ReportParkingViewHolder(view);
        return reportParkingViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportParkingViewHolder holder, int position) {
        holder.tv_indeks_item_parking.setText(String.valueOf(position + 1));
        holder.tv_assetcode_item_parking.setText(this.parkingList.get(position).getAssetCode());
        holder.tv_toff_item_parking.setText(this.parkingList.get(position).getTimestampOff());
        holder.tv_ton_item_parking.setText(this.parkingList.get(position).getTimestampOn());
        holder.tv_duration_item_parking.setText(this.parkingList.get(position).getDuration());
        holder.tv_address_item_parking.setText(this.parkingList.get(position).getAddress());
        holder.tv_distance_item_parking.setText(this.parkingList.get(position).getDistance());
        if (this.selectedPosition == position) {
            holder.ll_container_item_parking.setBackgroundResource(R.drawable.list_item_lastposition1_blue);
            holder.tv_indeks_item_parking.setTextColor(Color.WHITE);
            holder.tv_assetcode_item_parking.setTextColor(Color.WHITE);
            holder.tv_toff_item_parking.setTextColor(Color.WHITE);
            holder.tv_duration_item_parking.setTextColor(Color.WHITE);
            holder.tv_address_item_parking.setTextColor(Color.WHITE);
            holder.tv_distance_item_parking.setTextColor(Color.WHITE);
            holder.tv_ton_item_parking.setTextColor(Color.WHITE);
        }
        else {
            holder.ll_container_item_parking.setBackgroundResource(R.drawable.list_item_lastposition1_bg);
            holder.tv_indeks_item_parking.setTextColor(Color.BLACK);
            holder.tv_assetcode_item_parking.setTextColor(Color.BLACK);
            holder.tv_toff_item_parking.setTextColor(Color.BLACK);
            holder.tv_duration_item_parking.setTextColor(Color.BLACK);
            holder.tv_address_item_parking.setTextColor(Color.BLACK);
            holder.tv_distance_item_parking.setTextColor(Color.BLACK);
            holder.tv_ton_item_parking.setTextColor(Color.BLACK);
        }
        holder.ll_container_item_parking.setOnClickListener(new HighlightReportParkingData(position));
    }

    @Override
    public int getItemCount() {
        return this.parkingList.size();
    }

    class HighlightReportParkingData implements View.OnClickListener {
        private int vPosition;

        public HighlightReportParkingData(int p) {
            this.vPosition = p;
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = vPosition;
            notifyItemChanged(selectedPosition);
        }
    }

    public static class ReportParkingViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_container_item_parking;
        private TextView tv_indeks_item_parking;
        private TextView tv_assetcode_item_parking;
        private TextView tv_toff_item_parking;
        private TextView tv_ton_item_parking;
        private TextView tv_duration_item_parking;
        private TextView tv_address_item_parking;
        private TextView tv_distance_item_parking;

        ReportParkingViewHolder(View view) {
            super(view);
            this.ll_container_item_parking = (LinearLayout) view.findViewById(R.id.ll_container_item_parking);
            this.tv_indeks_item_parking = (TextView) view.findViewById(R.id.tv_indeks_item_parking);
            this.tv_assetcode_item_parking = (TextView) view.findViewById(R.id.tv_assetcode_item_parking);
            this.tv_toff_item_parking = (TextView) view.findViewById(R.id.tv_toff_item_parking);
            this.tv_ton_item_parking = (TextView) view.findViewById(R.id.tv_ton_item_parking);
            this.tv_duration_item_parking = (TextView) view.findViewById(R.id.tv_duration_item_parking);
            this.tv_address_item_parking = (TextView) view.findViewById(R.id.tv_address_item_parking);
            this.tv_distance_item_parking = (TextView) view.findViewById(R.id.tv_distance_item_parking);
        }
    }
}
