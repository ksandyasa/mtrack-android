package utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesProvider {
	private static SharedPreferencesProvider instance = new SharedPreferencesProvider();

	private final String pref_uid = "prefUid";
	private final String pref_accID = "prefAccID";
	private final String pref_accCompanyName = "prefAccCompanyName";
	private final String pref_Login = "prefLogin";
	private final String pref_username = "prefUsername";

	public static SharedPreferencesProvider getInstance() {
		return instance;
	}

	public String getUid(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(pref_uid, "null");
	}

	public void setUid(Context context, String uid) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(pref_uid, uid);
		editor.commit();
	}

	public String getAccID(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(pref_accID, "null");
	}

	public void setAccID(Context context, String acc_id) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(pref_accID, acc_id);
		editor.commit();
	}

	public String getAccCompanyName(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(pref_accCompanyName, "null");
	}

	public void setAccCompanyName(Context context, String acc_company_name) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(this.pref_accCompanyName, acc_company_name);
		editor.commit();
	}

	public boolean getLogin(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(pref_Login, false);
	}

	public void setLogin(Context context, boolean login) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putBoolean(pref_Login, login);
		editor.commit();
	}

	public String getUsername(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(pref_username, "null");
	}

	public void setUsername(Context context, String username) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(pref_username, username);
		editor.commit();
	}

	// public ModelSettings getNotificationSetting(Context context) {
	// SharedPreferences preferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// ModelSettings modelsetting = new ModelSettings();
	// try {
	// modelsetting.createFromJson(new JSONObject(preferences.getString(
	// pref_notification, "null")));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return modelsetting;
	// }
	//
	// public void setNotificationSetting(Context context,
	// ModelSettings modelSettings) {
	// SharedPreferences shared = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// SharedPreferences.Editor editor = shared.edit();
	// editor.putString(pref_notification, modelSettings.getJson().toString());
	// editor.commit();
	//
	// }

}
