package id.kreasisejahterateknologi.mtrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import model.Group;
import model.Login;
import network.NetworkConnection;
import utility.ConstantAPI;
import utility.SharedPreferencesProvider;
import utility.Utility;

/**
 * Created by Andy on 5/18/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private RelativeLayout rl_content_login;
    private EditText ed_user_login, ed_pass_login;
    private Button btn_login;
    private NetworkConnection networkConnection;
    private String[] responseString = {""};
    private Snackbar snackbar;
    private int modeNetwork = -1;
    private Handler loginHandler;
    private Messenger loginMessenger;
    private Message loginMessage;
    private Bundle loginBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");

        this.rl_content_login = (RelativeLayout) findViewById(R.id.rl_content_login);
        this.ed_user_login = (EditText) findViewById(R.id.ed_user_login);
        this.ed_pass_login = (EditText) findViewById(R.id.ed_pass_login);
        this.btn_login = (Button) findViewById(R.id.btn_login);

        setLoginProgressDialog();

        if (!SharedPreferencesProvider.getInstance().getUsername(this).equals("null"))
            this.ed_user_login.setText(SharedPreferencesProvider.getInstance().getUsername(this));

        this.btn_login.setOnClickListener(new ShowMainView());
    }

    private void setLoginProgressDialog() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Loading.. please wait");
    }

    class ShowMainView implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (Utility.ConnectionUtility.isNetworkConnected(LoginActivity.this)) {
                if (ed_user_login.getText().toString().equals("") && ed_pass_login.getText().toString().equals("")) {
                    snackbar = Snackbar.make(rl_content_login, "Please fill all field.", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new DismissSnackbar());
                    snackbar.show();
                }else if (ed_user_login.getText().toString().equals("") && !ed_pass_login.getText().toString().equals("")) {
                    snackbar = Snackbar.make(rl_content_login, "Please fill Username field.", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new DismissSnackbar());
                    snackbar.show();
                }else if (!ed_user_login.getText().toString().equals("") && ed_pass_login.getText().toString().equals("")) {
                    snackbar = Snackbar.make(rl_content_login, "Please fill Password field.", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new DismissSnackbar());
                    snackbar.show();
                }else {
                    progressDialog.show();
                    modeNetwork = 0;
                    doProcessLogin();
                }
            }else {
                snackbar = Snackbar.make(rl_content_login, "No internet connection.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Dismiss", new DismissSnackbar());
                snackbar.show();
            }
        }
    }

    private void doProcessResponse(String response) {
        if (modeNetwork == 0) {
            try {
                Login login = Utility.JSONUtility.getLoginDataFromJSON(response);
                if (login.getSuccess() == 1) {
                    SharedPreferencesProvider.getInstance().setUid(this, login.getUid());
                    SharedPreferencesProvider.getInstance().setAccID(this, login.getId());
                    SharedPreferencesProvider.getInstance().setAccCompanyName(this, login.getCompanyName());
                    SharedPreferencesProvider.getInstance().setLogin(this, true);
                    SharedPreferencesProvider.getInstance().setUsername(this, ed_user_login.getText().toString());
                    this.ed_pass_login.setText("");
                    modeNetwork = 1;
                    doProcessGroup(login.getUid(), login.getId());
                }else {
                    progressDialog.dismiss();
                    snackbar = Snackbar.make(rl_content_login, "Incorrect username or password.", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Dismiss", new DismissSnackbar());
                    snackbar.show();
                }
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }else {
            try {
                List<Group> groupList = Utility.JSONUtility.getListGroupFromJSON(response);
                if (groupList.size() != 0) {
                    progressDialog.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    LoginActivity.this.finish();
                }
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }


    private void doProcessLogin() {
        this.loginHandler = new Handler(this.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                doParseMessageToString(msg);
            }
        };
        doNetworkService(ConstantAPI.getLoginURL(ed_user_login.getText().toString(), ed_pass_login.getText().toString()));
    }

    private void doProcessGroup(String uid, String acc_id) {
        this.loginHandler = new Handler(this.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                doParseMessageToString(msg);
            }
        };
        doNetworkService(ConstantAPI.getGroupURL(uid, acc_id));
    }

    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this, NetworkConnection.class);
        this.loginMessenger = new Messenger(this.loginHandler);
        networkIntent.putExtra("messenger", this.loginMessenger);
        networkIntent.putExtra("url", url);
        startService(networkIntent);
    }

    private void doParseMessageToString(Message message) {
        loginMessage = message;
        Bundle b = loginMessage.getData();
        responseString[0] = b.getString("network_response");
        Log.d(TAG, "responseString[0] " + responseString[0]);
        doProcessResponse(responseString[0]);
    }

    class DismissSnackbar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    }
}
