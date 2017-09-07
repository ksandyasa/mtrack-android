package adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

/**
 * Created by Andy on 5/23/2016.
 */
public class MainVehicleAdapter extends RecyclerView.Adapter<MainVehicleAdapter.MainVehicleViewHolder> {
    private AppCompatActivity appCompatActivity;
    private int backgroundColor;

    public MainVehicleAdapter(AppCompatActivity aca, int color) {
        this.appCompatActivity = aca;
        this.backgroundColor = color;
    }

    @Override
    public MainVehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vehicle_main, parent, false);
        MainVehicleViewHolder mainVehicleViewHolder = new MainVehicleViewHolder(view);
        return mainVehicleViewHolder;
    }

    @Override
    public void onBindViewHolder(MainVehicleViewHolder holder, int position) {
        holder.rl_container_item_vehicle_main.setBackgroundColor(this.backgroundColor);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MainVehicleViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_container_item_vehicle_main;
        private TextView tv_title_item_vehicle_main;

        MainVehicleViewHolder(View view) {
            super(view);
            this.rl_container_item_vehicle_main = (RelativeLayout) view.findViewById(R.id.rl_container_item_vehicle_main);
            this.tv_title_item_vehicle_main = (TextView) view.findViewById(R.id.tv_title_item_vehicle_main);
        }
    }
}
