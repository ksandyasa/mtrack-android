package callback;

import android.content.Intent;

import model.LastPosition;

/**
 * Created by apridosandyasa on 5/26/16.
 */
public interface LastPositionCallback {
    void SendLastPositionDataToTracking(LastPosition lastPosition, int position);
}
