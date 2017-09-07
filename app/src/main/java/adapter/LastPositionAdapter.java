package adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import callback.LastPositonAdapterCallback;
import id.kreasisejahterateknologi.mtrack.R;
import model.LastPosition;

/**
 * Created by Andy on 5/19/2016.
 */
public class LastPositionAdapter extends RecyclerView.Adapter<LastPositionAdapter.LastPositionViewHolder> {
    private final String TAG = LastPositionAdapter.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private List<LastPosition> lastPositionList;
    private LastPositonAdapterCallback lastPositonAdapterCallback;
    private int selectedPosition = 0;

    public LastPositionAdapter(AppCompatActivity aca, List<LastPosition> objects, LastPositonAdapterCallback listener) {
        this.appCompatActivity = aca;
        this.lastPositionList = objects;
        this.lastPositonAdapterCallback = listener;
    }

    @Override
    public LastPositionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lastposition, parent, false);
        LastPositionViewHolder lastPositionViewHolder = new LastPositionViewHolder(view);
        return lastPositionViewHolder;
    }

    @Override
    public void onBindViewHolder(LastPositionViewHolder holder, int position) {
        holder.tv_title_item_lastposition.setText(this.lastPositionList.get(position).getAssetCode());
        String[] arrayDuration = this.lastPositionList.get(position).getDuration().split(", ");
        int durationDays = Integer.parseInt(arrayDuration[0].substring(0, arrayDuration[0].indexOf(" ")));
        float durationHour = Float.parseFloat(arrayDuration[1].substring(0, arrayDuration[1].indexOf(" ")).trim());
        float durationMinute = Float.parseFloat(arrayDuration[2].substring(0, arrayDuration[2].indexOf(" ")).trim()) / 60;
        durationHour = durationHour + durationMinute;
        if (this.selectedPosition == position) {
            holder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(R.color.colorPrimary));
            holder.tv_title_item_lastposition.setTextColor(Color.WHITE);
        }
        else {
            if (durationDays == 0 && durationHour < 12) {
                holder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(R.color.colorWhite));
                holder.tv_title_item_lastposition.setTextColor(Color.BLACK);
            }else if (durationDays == 0 && (durationHour > 12 && durationHour < 24)) {
                holder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(android.R.color.holo_orange_light));
                holder.tv_title_item_lastposition.setTextColor(Color.WHITE);
            }else if (durationDays > 0){
                holder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(android.R.color.holo_red_light));
                holder.tv_title_item_lastposition.setTextColor(Color.WHITE);
            }
        }
        holder.rl_container_item_lastposition.setOnClickListener(new ShowDetailLastPosition(position));
    }

    @Override
    public int getItemCount() {
        return this.lastPositionList.size();
    }

    class ShowDetailLastPosition implements View.OnClickListener {
        private int vPosition;

        public ShowDetailLastPosition(int p) {
            this.vPosition = p;
        }

        @Override
        public void onClick(View v) {
            selectedPosition = vPosition;
            lastPositonAdapterCallback.setDataLastPositionFromRow(vPosition);
            Log.d(TAG, "selectedPosition " + vPosition);
        }
    }

    public void SelectLastPositionData(int p) {
        selectedPosition = p;
        lastPositonAdapterCallback.setDataLastPositionFromRow(p);
        Log.d(TAG, "selectedPosition " + p);
    }

    public static class LastPositionViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_container_item_lastposition;
        private TextView tv_title_item_lastposition;

        LastPositionViewHolder(View view) {
            super(view);
            this.rl_container_item_lastposition = (RelativeLayout) view.findViewById(R.id.rl_container_item_lastposition);
            this.tv_title_item_lastposition = (TextView) view.findViewById(R.id.tv_title_item_lastposition);
        }
    }

}
