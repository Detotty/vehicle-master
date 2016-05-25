package pk.roadpartner.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pk.roadpartner.RoadApp;
import pk.roadpartner.model.User;

/**
 * Created by User on 3/27/2016.
 */
public class UserDatabase {

    private User userData;
    private SharedPreferences preferences;
    private Context context;
    private Gson gson;
    private SharedPreferences.Editor editor;
    public static final String USER_TABLE_NAME = "USER_TABLE_NAME";

    public UserDatabase(Context context) {
        this.context = context;
        RoadApp app = (RoadApp) context.getApplicationContext();
        preferences = app.getSharePref();
        gson = new Gson();
        editor = preferences.edit();
    }

    public User getUserData() {
        String dataString = preferences.getString(USER_TABLE_NAME, null);
        if (dataString == null) {
            return new User();
        } else {
            return gson.fromJson(dataString, User.class);
        }
    }

    public void setUserData(User userData) {
        putData(userData);
    }

    private void putData(User data) {
        editor.putString(USER_TABLE_NAME, gson.toJson(data));
        editor.apply();
    }
}
