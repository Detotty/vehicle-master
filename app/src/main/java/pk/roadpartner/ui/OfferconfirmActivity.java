package pk.roadpartner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rimon.rsa.vehicle.R;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.BidResponse;
import pk.roadpartner.model.User;

public class OfferconfirmActivity extends AppCompatActivity implements Callback<BidResponse> {

    public static final String USER_DATA_ID = "userdataid";
    private User user;
    private UserDatabase userDatabase;
    private Gson gson;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private TextView mKM,orderNoTextView, pickUpTextView, dropTextView, mobileNumberTextView, nameTextView, cashFroamCustomerTextview, yourBidTextView;
    private Button confrimButton;
    private Retrofit retrofit;
    private Call<BidResponse> call;
    private MyApiEndpointInterface myApiEndpointInterface;
    private Toolbar toolbar;
    private LinearLayout bidProcessingLayout;
    private TextView hintTextview;
    ImageView journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerconfirm);
        getData();
        initiView();
        setRetrofit();
        setActionBar();
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

    private void setRetrofit() {
        RoadApp app = (RoadApp) getApplicationContext();
        retrofit = app.getRetrofit();
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
    }

    private void callService() {
        call = myApiEndpointInterface.bidConFirm(user);
        call.enqueue(this);
    }

    private void initiView() {
        orderNoTextView = (TextView) findViewById(R.id.orderNoTextView);
        orderNoTextView.setText(user.getBidData().getOderNo());

        mKM = (TextView) findViewById(R.id.KM);
        mKM.setText(user.getBidData().getKm()+" KM");

        journey = (ImageView) findViewById(R.id.j_way);
        if (user.getBidData().getJournyOneWayorTwoWay().equals("1"))
            journey.setImageResource(R.drawable.oneway);
        else
            journey.setImageResource(R.drawable.twoway);

        pickUpTextView = (TextView) findViewById(R.id.pickUpTextView);
        pickUpTextView.setText(user.getBidData().getPickUpAddress());

        dropTextView = (TextView) findViewById(R.id.dropTextView);
        dropTextView.setText(user.getBidData().getDropAddress());

        mobileNumberTextView = (TextView) findViewById(R.id.mobileNumberTextView);
        mobileNumberTextView.setText(user.getBidData().getCellNo());

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(user.getBidData().getBidderName());
        cashFroamCustomerTextview = (TextView) findViewById(R.id.cashFroamCustomerTextview);
        cashFroamCustomerTextview.setText(user.getBidData().getYourBidRate());
        yourBidTextView = (TextView) findViewById(R.id.yourBidTextView);
        yourBidTextView.setText(user.getBidData().getYourBidRate());
        confrimButton = (Button) findViewById(R.id.confrimButton);

        confrimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidProcessingLayout.setVisibility(View.VISIBLE);
                confrimButton.setVisibility(View.GONE);
                callService();
                hintTextview.setVisibility(View.GONE);
            }
        });

        bidProcessingLayout = (LinearLayout) findViewById(R.id.bidProcessingLayout);
        hintTextview = (TextView) findViewById(R.id.hintTextview);
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getData() {
        String data = getIntent().getStringExtra(USER_DATA_ID);
        Log.d(LOG_TAG, data);
        gson = new Gson();
        user = gson.fromJson(data, User.class);
        userDatabase = new UserDatabase(this);
        Log.d(LOG_TAG, user.toString());
    }

    @Override
    public void onResponse(Call<BidResponse> call, Response<BidResponse> response) {
        BidResponse bidResponse = response.body();
        if (bidResponse.getBidResponse().contains("1")) {
            setSuccessfulLayout();
            user.setIsBidRunning(false);
            userDatabase.setUserData(user);
        } else {
            setUnsuccessfulLayout();
        }
    }

    @Override
    public void onFailure(Call<BidResponse> call, Throwable t) {
        setUnsuccessfulLayout();
    }

    private void setUnsuccessfulLayout() {
        confrimButton.setVisibility(View.VISIBLE);
        bidProcessingLayout.setVisibility(View.GONE);
        hintTextview.setVisibility(View.VISIBLE);
        hintTextview.setText(R.string.bidConfirmedError);
    }

    private void setSuccessfulLayout() {
        confrimButton.setVisibility(View.GONE);
        bidProcessingLayout.setVisibility(View.GONE);
        hintTextview.setVisibility(View.VISIBLE);
        hintTextview.setText(R.string.bidConfirmedSucessful);
    }
}
