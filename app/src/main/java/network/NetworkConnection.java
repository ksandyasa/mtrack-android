package network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apridosandyasa on 6/19/16.
 */
public class NetworkConnection extends IntentService {
    private final String TAG = NetworkConnection.class.getSimpleName();
    private String[] responseString = {""};
    private Messenger messenger;
    private Message message;
    private String url;


    public NetworkConnection() {
        super("");
    }

    public void doObtainDataFromServer(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d(TAG, url);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new NetworkConnectionCallback());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.messenger = (Messenger) intent.getParcelableExtra("messenger");
        this.url = intent.getStringExtra("url");
        this.message = Message.obtain(null, 0);
        doObtainDataFromServer(this.url);
    }

    private class NetworkConnectionCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            try {
                onResponseInMainThread(response);
            } catch (IOException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    public void onResponseInMainThread(Response response) throws IOException {
        this.responseString[0] = response.body().string();
        Log.d(TAG, "responseString[0] " + this.responseString[0]);
        Log.d(TAG, "message.what" + this.message.what);
        Bundle bundle = new Bundle();
        bundle.putString("network_response", this.responseString[0]);
        this.message.setData(bundle);
        try {
            this.messenger.send(this.message);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }

}
