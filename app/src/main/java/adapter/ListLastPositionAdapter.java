package adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.kreasisejahterateknologi.mtrack.R;

import java.util.List;

import model.LastPosition;

/**
 * Created by apridosandyasa on 5/30/16.
 */
public class ListLastPositionAdapter extends BaseAdapter {
    private AppCompatActivity appCompatActivity;
    private List<LastPosition> lastPositionList;
    private ListLastPositionViewHolder listViewHolder;
    private int selectedPosition = 0;

    public ListLastPositionAdapter(AppCompatActivity aca, List<LastPosition> objects) {
        this.appCompatActivity = aca;
        this.lastPositionList = objects;
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
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lastposition, parent, false);
        this.listViewHolder = new ListLastPositionViewHolder();
        convertView.setTag(this.listViewHolder);
        this.listViewHolder.rl_container_item_lastposition = (RelativeLayout) convertView.findViewById(R.id.rl_container_item_lastposition);
        this.listViewHolder.tv_title_item_lastposition = (TextView) convertView.findViewById(R.id.tv_title_item_lastposition);
        this.listViewHolder.tv_title_item_lastposition.setText(this.lastPositionList.get(position).getAssetCode());
        String[] arrayDuration = this.lastPositionList.get(position).getDuration().split(", ");
        int durationDays = Integer.parseInt(arrayDuration[0].substring(0, arrayDuration[0].indexOf(" ")));
        float durationHour = Float.parseFloat(arrayDuration[1].substring(0, arrayDuration[1].indexOf(" ")).trim());
        float durationMinute = Float.parseFloat(arrayDuration[2].substring(0, arrayDuration[2].indexOf(" ")).trim()) / 60;
        durationHour = durationHour + durationMinute;
        if (this.selectedPosition == position) {
            this.listViewHolder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(R.color.colorActivateList));
            this.listViewHolder.tv_title_item_lastposition.setTextColor(Color.WHITE);
        }else {
            if (durationDays == 0 && durationHour < 12) {
                this.listViewHolder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(R.color.colorWhite));
                this.listViewHolder.tv_title_item_lastposition.setTextColor(Color.BLACK);
            }else if (durationDays == 0 && (durationHour > 12 && durationHour < 24)) {
                this.listViewHolder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(android.R.color.holo_orange_light));
                this.listViewHolder.tv_title_item_lastposition.setTextColor(Color.BLACK);
            }else if (durationDays > 0){
                this.listViewHolder.rl_container_item_lastposition.setBackgroundColor(this.appCompatActivity.getResources().getColor(android.R.color.holo_red_light));
                this.listViewHolder.tv_title_item_lastposition.setTextColor(Color.BLACK);
            }
        }

        return convertView;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    static class ListLastPositionViewHolder {
        private RelativeLayout rl_container_item_lastposition;
        private TextView tv_title_item_lastposition;
    }
}
