package pk.roadpartner.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rimon.rsa.vehicle.R;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.BidData;
import pk.roadpartner.model.BidResponse;
import pk.roadpartner.model.User;
import pk.roadpartner.util.BidValidation;
import pk.roadpartner.view.DigitalClock;

public class BidDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<BidResponse> {

    public static final String BID_DATA_ID = "bid_data_id";
    private BidData bidData;
    private TextView mPickUpTextView, mDropTextView, mPickUpDateTextView, mOfferPriceTextView, pickUpTimeTextView,mKM;
    private Button mStartBidButton;
    ImageView journey;
    private DigitalClock timeCounterDigitalClock;
    private Gson gson;
    private CountDownTimer countDownTimer;
    private EditText mBidEditText;
    private Toolbar toolbar;
    private User user;
    private UserDatabase userDatabase;
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Retrofit retrofit;
    private Call<BidResponse> call;
    private MyApiEndpointInterface myApiEndpointInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getStringExtra(BID_DATA_ID);
        gson = new Gson();
        bidData = gson.fromJson(data, BidData.class);
        Log.d(LOG_TAG, data);
        setContentView(R.layout.activity_bid_details);
        initView();
        userDatabase = new UserDatabase(this);
        user = userDatabase.getUserData();
        user.deCreaseBidAbility();
        userDatabase.setUserData(user);
        user = userDatabase.getUserData();
        setRetrofit();
    }

    private void setRetrofit() {
        RoadApp app = (RoadApp) getApplicationContext();
        retrofit = app.getRetrofit();
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
    }


    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        setActionBar();
        mPickUpTextView = (TextView) findViewById(R.id.pickUpTextView);
        mPickUpTextView.setText(bidData.getPickUpAddress());
        mDropTextView = (TextView) findViewById(R.id.dropTextView);
        mDropTextView.setText(bidData.getDropAddress());
        mOfferPriceTextView = (TextView) findViewById(R.id.offerPriceTextView);
        mOfferPriceTextView.setText(bidData.getOfferPrice());
        mPickUpDateTextView = (TextView) findViewById(R.id.pickUpDateTextView);
        mPickUpDateTextView.setText(bidData.getPickUpdate());
        mKM = (TextView) findViewById(R.id.KM);
        mKM.setText(bidData.getKm()+" KM");
        journey = (ImageView) findViewById(R.id.j_way);
        if (bidData.getJournyOneWayorTwoWay().equals("1"))
            journey.setImageResource(R.drawable.oneway);
        else
            journey.setImageResource(R.drawable.twoway);
        mStartBidButton = (Button) findViewById(R.id.startBidButton);
        mStartBidButton.setOnClickListener(this);
        mBidEditText = (EditText) findViewById(R.id.bidEditText);
        timeCounterDigitalClock = (DigitalClock) findViewById(R.id.timeCounterDigitalClock);

        if (bidData.getMillisInFuture() > BidData.COUNT_DOWN_INTERVAL) {
            timeCounterDigitalClock.setText(bidData.getTimeCounter());
            setCountDown();
        } else {
            disableBiddingView();
        }
        pickUpTimeTextView = (TextView) findViewById(R.id.pickUpTimeTextView);
        pickUpTimeTextView.setText(bidData.getmTime());
    }

    private void setCountDown() {
        countDownTimer = new CountDownTimer(bidData.getMillisInFuture(), BidData.COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                bidData.setMillisInFuture(millisUntilFinished);
                timeCounterDigitalClock.setText(bidData.getTimeCounter());
            }

            @Override
            public void onFinish() {
                disableBiddingView();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBidButton:
                bidProcessing();
                break;
        }
    }

    private void bidProcessing() {
        mBidEditText.setError(null);
        String bidRate = mBidEditText.getText().toString().trim();

        Log.d(LOG_TAG, bidRate + "                        offer : " + bidData.getOfferPrice()+"   km  "+bidData.getKm());
        if (bidRate != null && bidRate.length() >= 1 && notOnlyContainZero(bidRate)) {
            BidValidation bidValidation = new BidValidation(bidRate, bidData.getOfferPrice(), bidData.getKm());
            if (bidValidation.isValid()) {
                countDownTimer.cancel();
                bidData.setYourBidRate(bidRate);
                user.setBidData(bidData);
                placeBid();

                /*call = myApiEndpointInterface.placeUserBid(user);
                call.enqueue(this);*/
            } else {
                mBidEditText.setError(bidValidation.getMgs());
            }

        } else {
            mBidEditText.setError(getString(R.string.invalidInput));
        }
    }

    private void disableBiddingView() {
        timeCounterDigitalClock.setTextColor(Color.RED);
        timeCounterDigitalClock.setText("00:00:00");
        disableBiddingInput();
    }

    private void disableBiddingInput() {
        mStartBidButton.setEnabled(false);
        mBidEditText.setError(null);
        mBidEditText.setText(null);
        mBidEditText.setEnabled(false);
    }

    private void enableBiddingInput() {
        mStartBidButton.setEnabled(true);
        mBidEditText.setError(null);
        mBidEditText.setText(null);
        mBidEditText.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean notOnlyContainZero(String target) {
        int counter = 0;
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == '0') {
                counter++;
            }
        }
        return counter != target.length();
    }

    @Override
    public void onResponse(Call<BidResponse> call, Response<BidResponse> response) {
        user.setIsBidRunning(true);
        userDatabase.setUserData(user);
        String data = gson.toJson(user);
        Log.d(LOG_TAG, data);
        Intent intent = new Intent(BidDetailsActivity.this, BidProcessingActivity.class);
        intent.putExtra(BidProcessingActivity.BID_DATA_PROCESSING_ID, data);
        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onFailure(Call<BidResponse> call, Throwable t) {
        enableBiddingInput();
    }

    public void placeBid() {
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("yourBidRate", user.getBidData().getYourBidRate());
            mainObj.put("mCnic", user.getmCnic());
            mainObj.put("oderNo", user.getBidData().getOderNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://developer.roadpartner.pk/placebid.php";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, mainObj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("GetUser response", jsonObject.toString());
                try {
                    user.setIsBidRunning(true);
                    userDatabase.setUserData(user);
                    String data = gson.toJson(user);
                    Log.d(LOG_TAG, data);
                    Intent intent = new Intent(BidDetailsActivity.this, BidProcessingActivity.class);
                    intent.putExtra(BidProcessingActivity.BID_DATA_PROCESSING_ID, data);
                    startActivity(intent);
                    setResult(RESULT_OK, null);
                    finish();


                } catch (Exception e) {
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

}
