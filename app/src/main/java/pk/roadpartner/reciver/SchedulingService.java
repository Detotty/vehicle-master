package pk.roadpartner.reciver;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.User;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code AlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SchedulingService extends IntentService {

    public final String TAG = this.getClass().getSimpleName();
    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        UserDatabase database = new UserDatabase(this);
        User user = database.getUserData();
        user.setCurrentBidAbility();
        database.setUserData(user);
        Log.d(TAG, "data changed");
        // Release the wake lock provided by the BroadcastReceiver.
        AlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }
}
