package pk.roadpartner.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rimon.rsa.vehicle.R;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.BidData;
import pk.roadpartner.model.User;
import pk.roadpartner.view.DigitalClock;

import static android.widget.Toast.makeText;

public class BidProcessingActivity extends AppCompatActivity implements Callback<User>, View.OnClickListener {

    public static final String BID_DATA_PROCESSING_ID = "bid_processing_data_id";
    private final String LOG_TAG = this.getClass().getSimpleName();
    private User user;
    private DigitalClock timeCounterDigitalClock;
    private Gson gson;
    private CountDownTimer countDownTimer;
    private Toolbar toolbar;
    private BidData bidData;
    private UserDatabase userDatabase;
    private Retrofit retrofit;
    private Call<User> call;
    private MyApiEndpointInterface myApiEndpointInterface;
    private RelativeLayout bidLossdLayout, bidWindLayout;
    private Button winButton, lossButton;
    private LinearLayout bidProcessingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_processing);
        setActionBar();
        getData();
        setRetrofit();
        initView();
    }

    private void setRetrofit() {
        RoadApp app = (RoadApp) getApplicationContext();
        retrofit = app.getRetrofit();
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
    }

    private void getData() {
        String data = getIntent().getStringExtra(BID_DATA_PROCESSING_ID);
        Log.d(LOG_TAG, data);
        gson = new Gson();
        user = gson.fromJson(data, User.class);
        bidData = user.getBidData();
        userDatabase = new UserDatabase(this);
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        bidLossdLayout = (RelativeLayout) findViewById(R.id.bidLossdLayout);
        bidWindLayout = (RelativeLayout) findViewById(R.id.bidWindLayout);
        bidProcessingLayout = (LinearLayout) findViewById(R.id.bidProcessingLayout);
        winButton = (Button) findViewById(R.id.winButton);
        winButton.setOnClickListener(this);
        lossButton = (Button) findViewById(R.id.lossButton);
        lossButton.setOnClickListener(this);
        timeCounterDigitalClock = (DigitalClock) findViewById(R.id.timeCounterDigitalClock);
    }

    private void callCountDown() {
        if (bidData.getMillisInFuture() > BidData.COUNT_DOWN_INTERVAL) {
            timeCounterDigitalClock.setText(bidData.getTimeCounter());
            setCountDown();
        } else {
            disableBiddingView();
            callService();
        }
    }

    private void setCountDown() {
        if (bidData.getSystemTime() == 0) {
            Log.d(LOG_TAG, "timer call");
            setTimmer();
        } else if (bidData.getSystemTime() > 0) {
            long time = getTimeDifference();
            if (time > BidData.COUNT_DOWN_INTERVAL) {
                Log.d(LOG_TAG, "timer call 2");
                bidData.setMillisInFuture(time);
                user.setBidData(bidData);
                userDatabase.setUserData(user);
                setTimmer();
            } else {
                Log.d(LOG_TAG, "timer call service");
                disableBiddingView();
                callService();
            }

        } else {
            disableBiddingView();
            callService();
        }
    }

    private long getTimeDifference() {
        long timeDifference = System.currentTimeMillis() - bidData.getSystemTime();
        return bidData.getMillisInFuture() - timeDifference;
    }

    private void disableBiddingView() {
        timeCounterDigitalClock.setTextColor(Color.RED);
        timeCounterDigitalClock.setText("00:00:00");
    }

    private void setTimmer() {
        countDownTimer = new CountDownTimer(bidData.getMillisInFuture(), BidData.COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                bidData.setMillisInFuture(millisUntilFinished);
                timeCounterDigitalClock.setText(bidData.getTimeCounter());
            }

            @Override
            public void onFinish() {
                callService();
                disableBiddingView();
                bidData.setMillisInFuture(0);
                bidData.setSystemTime(0l);
                user.setBidData(bidData);
                userDatabase.setUserData(user);
            }
        }.start();
    }

    static int i = 0;

    /*private void callService() {
            call = myApiEndpointInterface.bidWinner(user);
            call.enqueue(BidProcessingActivity.this);

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                if (countDownTimer != null)
                    countDownTimer.cancel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        callCountDown();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (countDownTimer != null) countDownTimer.cancel();
        if (call != null) call.cancel();
        saveData();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void saveData() {
        bidData.setSystemTime(System.currentTimeMillis());
        user.setBidData(bidData);
        userDatabase.setUserData(user);
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        Log.d("LOG_TAG", response.message() + "RRRRRRRRRRR");
        Log.d("LOG_TAG", response.toString() + "RRRRRRRRRRR");
        User userResponse = response.body();
        Log.d("LOG_TAG", userResponse.toString() + "RRRRRRRRRRR");
        // win bid
        if (userResponse.getmCnic() == null || userResponse.getmCnic().isEmpty()) {
            callService();
        } else if (userResponse.getmCnic().compareTo(user.getmCnic()) == 0) {
            bidProcessingLayout.setVisibility(View.GONE);
            bidWindLayout.setVisibility(View.VISIBLE);
        } else {
            //loss bid
            bidProcessingLayout.setVisibility(View.GONE);
            bidLossdLayout.setVisibility(View.VISIBLE);
            user.setBidData(null);
            user.setIsBidRunning(false);
            userDatabase.setUserData(user);
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Log.d("LOG_TAG", "FAil");
        callService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.winButton:
                userDatabase.setUserData(user);
                Intent i = new Intent(this, OfferconfirmActivity.class);
                String data = gson.toJson(user);
                i.putExtra(OfferconfirmActivity.USER_DATA_ID, data);
                startActivity(i);
                finish();
                break;
            case R.id.lossButton:
                startActivity(new Intent(this, DashBoradActivity.class));
                break;
        }
    }


    //region Service Calling
    public void bidWinner() {
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("oderNo", user.getBidData().getOderNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://developer.roadpartner.pk/bidwinner.php";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, mainObj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("GetUser response", jsonObject.toString());
                try {
                    String cnic = jsonObject.getString("cmnci");

//                        if (cnic.compareTo(user.getmCnic()) == 0) {
                        if (cnic.equals(user.getmCnic())) {
                            bidProcessingLayout.setVisibility(View.GONE);
                            bidWindLayout.setVisibility(View.VISIBLE);
                        } else {
                            //loss bid
                            bidProcessingLayout.setVisibility(View.GONE);
                            bidLossdLayout.setVisibility(View.VISIBLE);
                            user.setBidData(null);
                            user.setIsBidRunning(false);
                            userDatabase.setUserData(user);
                        }

                } catch (JSONException e) {
                    bidProcessingLayout.setVisibility(View.GONE);
                    bidLossdLayout.setVisibility(View.VISIBLE);
                    user.setBidData(null);
                    user.setIsBidRunning(false);
                    userDatabase.setUserData(user);
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG_TAG", error.getMessage()+ "RRRRRRRRRRR");
            }
        });
        RoadApp.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void  callService(){
        String url = "http://developer.roadpartner.pk/cvb.php";
        new HttpAsyncTask().execute(url);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
            {
                result = convertInputStreamToString(inputStream);
            }
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            bidWinner();
        }
    }
    //endregion
}
