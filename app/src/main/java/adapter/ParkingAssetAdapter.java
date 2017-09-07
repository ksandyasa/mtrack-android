package adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

import java.util.List;

import callback.ParkingAssetAdapterCallback;
import model.LastPosition;
import model.Vehicle;

/**
 * Created by apridosandyasa on 5/26/16.
 */
public class ParkingAssetAdapter extends RecyclerView.Adapter<ParkingAssetAdapter.ParkingAssetViewHolder> {
    private AppCompatActivity appCompatActivity;
    private List<LastPosition> vehicleList;
    private ParkingAssetAdapterCallback parkingAssetAdapterCallback;
    private int selectedPosition = 0;

    public ParkingAssetAdapter(AppCompatActivity aca, List<LastPosition> objects, ParkingAssetAdapterCallback listener) {
        this.appCompatActivity = aca;
        this.vehicleList = objects;
        this.parkingAssetAdapterCallback = listener;
    }

    @Override
    public ParkingAssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_asset_parking, parent, false);
        ParkingAssetViewHolder parkingAssetViewHolder = new ParkingAssetViewHolder(view);
        return parkingAssetViewHolder;
    }

    @Override
    public void onBindViewHolder(ParkingAssetViewHolder holder, int position) {
        holder.tv_title_item_asset_parking.setText(this.vehicleList.get(position).getAssetCode());
        holder.rl_container_item_asset_parking.setOnClickListener(new ObtainVehicleData(position));
        if (this.selectedPosition == position) {
            holder.rl_container_item_asset_parking.setBackgroundResource(R.drawable.list_item_lastposition1_blue);
            holder.tv_title_item_asset_parking.setTextColor(Color.WHITE);
        }
        else {
            holder.rl_container_item_asset_parking.setBackgroundResource(R.drawable.list_item_lastposition1_bg);
            holder.tv_title_item_asset_parking.setTextColor(Color.DKGRAY);
        }

    }

    @Override
    public int getItemCount() {
        return this.vehicleList.size();
    }

    class ObtainVehicleData implements View.OnClickListener {
        private int vPosition;

        public ObtainVehicleData(int p) {
            this.vPosition = p;
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = vPosition;
            notifyItemChanged(selectedPosition);
            parkingAssetAdapterCallback.ObtainVehicleData(this.vPosition);
        }
    }

    public static class ParkingAssetViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_container_item_asset_parking;
        private TextView tv_title_item_asset_parking;

        ParkingAssetViewHolder(View view) {
            super(view);
            this.rl_container_item_asset_parking = (RelativeLayout) view.findViewById(R.id.rl_container_item_asset_parking);
            this.tv_title_item_asset_parking = (TextView) view.findViewById(R.id.tv_title_item_asset_parking);
        }
    }
}
