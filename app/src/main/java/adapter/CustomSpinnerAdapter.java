package adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import callback.CustomSpinnerAdapterCallback;
import id.kreasisejahterateknologi.mtrack.R;
import model.Vehicle;

/**
 * Created by apridosandyasa on 1/23/16.
 */
public class CustomSpinnerAdapter extends BaseAdapter {
    private AppCompatActivity appCompatActivity;
    private List<Vehicle> spinItems;
    private ViewHolder viewHolder;
    private CustomSpinnerAdapterCallback customSpinnerAdapterCallback;

    public CustomSpinnerAdapter(AppCompatActivity aca, List<Vehicle> objects, CustomSpinnerAdapterCallback listener) {
        this.appCompatActivity = aca;
        this.spinItems = objects;
        this.customSpinnerAdapterCallback = listener;
    }

    static class ViewHolder {
        RelativeLayout rl_container_item_spinnser;
        TextView tv_item_spinner;
    }

    @Override
    public int getCount() {
        return this.spinItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.spinItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.appCompatActivity).inflate(R.layout.list_item_spinner, parent, false);
        this.viewHolder = new ViewHolder();
        convertView.setTag(this.viewHolder);
        this.viewHolder.rl_container_item_spinnser = (RelativeLayout) convertView.findViewById(R.id.rl_container_item_spinner);
        this.viewHolder.tv_item_spinner = (TextView) convertView.findViewById(R.id.tv_item_spinner);
        this.viewHolder.tv_item_spinner.setText("" + this.spinItems.get(position).getAssetCode());
        //this.viewHolder.rl_container_item_spinnser.setOnClickListener(new ReShowSearchTrackingDialog());
        return convertView;
    }

    class ReShowSearchTrackingDialog implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //customSpinnerAdapterCallback.reshowSearchDialog();
        }
    }
}
