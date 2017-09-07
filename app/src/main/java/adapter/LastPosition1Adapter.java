package adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

import java.util.List;

import callback.LastPositionAdapterCallback1;
import model.LastPosition;

/**
 * Created by apridosandyasa on 5/26/16.
 */
public class LastPosition1Adapter extends BaseAdapter {
    private final String TAG = LastPosition1Adapter.class.getSimpleName();

    private AppCompatActivity appCompatActivity;
    private List<LastPosition> lastPositionList;
    private int selectedPosition = 0;
    private LastPosition1ViewHolder holder;
    private LastPositionAdapterCallback1 lastPositionAdapterCallback1;

    public LastPosition1Adapter(AppCompatActivity aca, List<LastPosition> objects, LastPositionAdapterCallback1 listener) {
        this.appCompatActivity = aca;
        this.lastPositionList = objects;
        this.lastPositionAdapterCallback1 = listener;
    }

    private void setTextviewGravity(LastPosition1ViewHolder holder, int gravity) {
        holder.tv_indeks_item_lastposition1.setGravity(gravity);
        holder.tv_assetcode_item_lastposition1.setGravity(gravity);
        holder.tv_address_item_lastposition1.setGravity(gravity);
        holder.tv_timestamp_item_lastposition1.setGravity(gravity);
        holder.tv_status_item_lastposition1.setGravity(gravity);
        holder.tv_engine_item_lastposition1.setGravity(gravity);
        holder.tv_phone_item_lastposition1.setGravity(gravity);
        holder.tv_distance_item_lastposition1.setGravity(gravity);
        holder.tv_speed_item_lastposition1.setGravity(gravity);
        holder.tv_duration_item_lastposition1.setGravity(gravity);
    }

    private void setTextviewGravity1(LastPosition1ViewHolder holder) {
        holder.tv_indeks_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_assetcode_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_address_item_lastposition1.setGravity(Gravity.LEFT);
        holder.tv_timestamp_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_status_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_engine_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_phone_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_distance_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_speed_item_lastposition1.setGravity(Gravity.CENTER);
        holder.tv_duration_item_lastposition1.setGravity(Gravity.CENTER);
    }

    private void setHeaderBackground(LastPosition1ViewHolder holder, int drawable) {
        holder.ll_container_item_lastposition1.setBackgroundResource(drawable);
    }

    private void setTextviewTextColor(LastPosition1ViewHolder holder, int color) {
        holder.tv_indeks_item_lastposition1.setTextColor(color);
        holder.tv_assetcode_item_lastposition1.setTextColor(color);
        holder.tv_address_item_lastposition1.setTextColor(color);
        holder.tv_timestamp_item_lastposition1.setTextColor(color);
        holder.tv_status_item_lastposition1.setTextColor(color);
        holder.tv_engine_item_lastposition1.setTextColor(color);
        holder.tv_phone_item_lastposition1.setTextColor(color);
        holder.tv_distance_item_lastposition1.setTextColor(color);
        holder.tv_speed_item_lastposition1.setTextColor(color);
        holder.tv_duration_item_lastposition1.setTextColor(color);
    }

    @Override
    public int getCount() {
        return this.lastPositionList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lastPositionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_lastposition1, parent, false);
        this.holder = new LastPosition1ViewHolder();
        convertView.setTag(this.holder);
        holder.ll_container_item_lastposition1 = (LinearLayout) convertView.findViewById(R.id.ll_container_item_lastposition1);
        holder.tv_indeks_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_indeks_item_lastposition1);
        holder.tv_assetcode_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_assetcode_item_lastposition1);
        holder.tv_address_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_address_item_lastposition1);
        holder.tv_timestamp_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_timestamp_item_lastposition1);
        holder.tv_status_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_status_item_lastposition1);
        holder.tv_engine_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_engine_item_lastposition1);
        holder.tv_phone_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_phone_item_lastposition1);
        holder.tv_distance_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_distance_item_lastposition1);
        holder.tv_speed_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_speed_item_lastposition1);
        holder.tv_duration_item_lastposition1 = (TextView) convertView.findViewById(R.id.tv_duration_item_lastposition1);
        setLastPosition1Holder(holder, position);
        return convertView;
    }

    private void setLastPosition1Holder(LastPosition1ViewHolder holder, int position) {
        holder.tv_indeks_item_lastposition1.setText(String.valueOf(position + 1));
        holder.tv_assetcode_item_lastposition1.setText(this.lastPositionList.get(position).getAssetCode());
        holder.tv_address_item_lastposition1.setText(this.lastPositionList.get(position).getAddress());
        holder.tv_timestamp_item_lastposition1.setText(this.lastPositionList.get(position).getTimestamp());
        holder.tv_status_item_lastposition1.setText(this.lastPositionList.get(position).getInputMask());
        holder.tv_engine_item_lastposition1.setText(this.lastPositionList.get(position).getStatusCode());
        holder.tv_phone_item_lastposition1.setText(this.lastPositionList.get(position).getPhoneNumber());
        holder.tv_distance_item_lastposition1.setText(this.lastPositionList.get(position).getDistance());
        holder.tv_speed_item_lastposition1.setText(this.lastPositionList.get(position).getSpeed());
        holder.tv_duration_item_lastposition1.setText(this.lastPositionList.get(position).getDuration());
        setTextviewGravity1(holder);
        String[] arrayDuration = this.lastPositionList.get(position).getDuration().split(", ");
        int durationDays = Integer.parseInt(arrayDuration[0].substring(0, arrayDuration[0].indexOf(" ")));
        float durationHour = Float.parseFloat(arrayDuration[1].substring(0, arrayDuration[1].indexOf(" ")).trim());
        float durationMinute = Float.parseFloat(arrayDuration[2].substring(0, arrayDuration[2].indexOf(" ")).trim()) / 60;
        durationHour = durationHour + durationMinute;
        Log.d(TAG, "durationHour " + durationHour);
        if (this.selectedPosition == position) {
            setHeaderBackground(holder, R.drawable.list_item_lastposition1_blue);
            setTextviewTextColor(holder, this.appCompatActivity.getResources().getColor(R.color.colorWhite));
        }else {
            if (durationDays == 0 && durationHour < 12) {
                setHeaderBackground(holder, R.drawable.list_item_lastposition1_bg);
                setTextviewTextColor(holder, this.appCompatActivity.getResources().getColor(R.color.colorBlack));
            }else if (durationDays == 0 && (durationHour > 12 && durationHour < 24)) {
                setHeaderBackground(holder, R.drawable.list_item_lastposition1_yellow);
                setTextviewTextColor(holder, this.appCompatActivity.getResources().getColor(R.color.colorBlack));
            }else if (durationDays > 0){
                setHeaderBackground(holder, R.drawable.list_item_lastposition1_red);
                setTextviewTextColor(holder, this.appCompatActivity.getResources().getColor(R.color.colorBlack));
            }
        }
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    class SelectLastPosition implements View.OnLongClickListener {
        private int vPosition;

        public SelectLastPosition(int p) {
            this.vPosition = p;
        }

        @Override
        public boolean onLongClick(View v) {
            lastPositionAdapterCallback1.SelectLastPositionDataToTracking(this.vPosition);

            return true;
        }
    }

    static class LastPosition1ViewHolder {
        private LinearLayout ll_container_item_lastposition1;
        private TextView tv_indeks_item_lastposition1;
        private TextView tv_assetcode_item_lastposition1;
        private TextView tv_address_item_lastposition1;
        private TextView tv_timestamp_item_lastposition1;
        private TextView tv_status_item_lastposition1;
        private TextView tv_engine_item_lastposition1;
        private TextView tv_phone_item_lastposition1;
        private TextView tv_distance_item_lastposition1;
        private TextView tv_speed_item_lastposition1;
        private TextView tv_duration_item_lastposition1;
    }
}
