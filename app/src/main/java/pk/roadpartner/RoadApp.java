package pk.roadpartner;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import pk.roadpartner.reciver.AlarmReceiver;
import pk.roadpartner.util.LruBitmapCache;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 3/27/2016.
 */
public class RoadApp extends Application {
    private SharedPreferences preferences;
    private Retrofit retrofit;
    private AlarmReceiver alarmReceiver;
    public final String TAG = this.getClass().getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static RoadApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mInstance = this;

        alarmReceiver = new AlarmReceiver();

        if (alarmReceiver.isAlarmSet(this)) {
            Log.d(TAG, "alarm set");

        } else {
            Log.d(TAG, "alarm not set");
            alarmReceiver.setAlarm(this);
        }

    }

    public synchronized SharedPreferences getSharePref() {
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(RoadApp.this);
        return preferences;
    }

    public synchronized Retrofit getRetrofit() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://developer.roadpartner.pk")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static synchronized RoadApp getInstance() {
        return mInstance;
    }

}
