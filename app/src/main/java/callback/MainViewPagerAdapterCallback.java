package callback;

import android.content.Intent;

/**
 * Created by apridosandyasa on 5/26/16.
 */
public interface MainViewPagerAdapterCallback {
    void RefreshCustomViewTabLayout();
    void SelectLastPositionToTrackingOnMap(Intent data);
}
