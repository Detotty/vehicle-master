package pk.roadpartner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.card.BidCard;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.BidData;
import pk.roadpartner.model.OrderNo;
import pk.roadpartner.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rimon.rsa.vehicle.R;

public class DashBoradActivity extends AppCompatActivity implements Callback<List<BidData>> {

    public static final int REQUEST_EXIT = 11;
    private CardListView mBidListView;
    private TextView mNoResultPreviewTextView;
    // private ProgressBar mProgressBar;
    private CardArrayAdapter cardArrayAdapter;
    private Button button;
    private Animation rightInAnimation;
    private Handler mTimeHandler, mListHandler;
    private Toolbar toolbar;
    private Retrofit retrofit;
    private boolean newRun = false;
    private Call<List<BidData>> call;
    private MyApiEndpointInterface myApiEndpointInterface;
    private volatile OrderNo orderNo;
    private List<Card> bidCards;
    private Timer mTimerUpdateCardTime, mTimerUpdateCardList;
    private User user;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_borad);
        userDatabase = new UserDatabase(this);
        user = userDatabase.getUserData();
        setActionBar();
        initView();
        setRetrofit();
        setCardUpdateTime();
        setListTimer();
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    private void initView() {
        mBidListView = (CardListView) findViewById(R.id.bidListView);
        mNoResultPreviewTextView = (TextView) findViewById(R.id.noResultPreviewTextView);
        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        rightInAnimation = AnimationUtils.loadAnimation(DashBoradActivity.this, R.anim.slide_in_right);
        bidCards = new ArrayList<>();
        cardArrayAdapter = new CardArrayAdapter(this, bidCards);
        mBidListView.setAdapter(cardArrayAdapter);
    }

    private void setRetrofit() {
        RoadApp app = (RoadApp) getApplicationContext();
        orderNo = new OrderNo();
        orderNo.setmCity(user.getmCity());
        orderNo.setmTypeOfCar(user.getmTypeOfCar());
        orderNo.setOrderNo(OrderNo.ALL);
        orderNo.setmTypeOfVehicle(user.getmTypeOfVehicle());
        orderNo.setmCnic(user.getmCnic());
        retrofit = app.getRetrofit();
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
//        Log.d("BBBBBBBBBBBBBBBBB",orderNo.toString());
//        Gson d= new Gson();

        call = myApiEndpointInterface.getBidData(orderNo);
        Log.d("DashBoradActivity",orderNo.toString());
        call.enqueue(this);
    }

    private void setCardUpdateTime() {
        mTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (cardArrayAdapter != null && cardArrayAdapter.getCount() > 0) {
                    int i = cardArrayAdapter.getCount() - 1;
                    while (i >= 0) {
                        BidCard bidCard = (BidCard) cardArrayAdapter.getItem(i);
                        BidData data = bidCard.getBidData();
                        data.setReduceCountDown();
                        if (data.getMillisInFuture() <= 0) {
                            cardArrayAdapter.remove(cardArrayAdapter.getItem(i));
                        }
                        --i;
                    }
                    cardArrayAdapter.notifyDataSetChanged();
                }
            }
        };
        mTimerUpdateCardTime = new Timer();
        mTimerUpdateCardTime.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mTimeHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cardArrayAdapter != null && cardArrayAdapter.getCount() > 0) {
            user = userDatabase.getUserData();
            for (int i = 0; i < cardArrayAdapter.getCount(); i++) {
                ((BidCard) cardArrayAdapter.getItem(i)).setIsBidDetails(user.isBidDetailsAvailable());
            }
            cardArrayAdapter.notifyDataSetChanged();
        }

        Log.d("DashBoradActivity", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
    }

    private void setCardList(List<BidData> data) {
        for (int i = 0; i < data.size(); i++) {
            BidData bidData = data.get(i);
            orderNo.setOrderNo(bidData.getOderNo());
            Card card = new BidCard(this, bidData, user.isBidDetailsAvailable());
            card.setId(bidData.getOderNo());
            cardArrayAdapter.insert(card, 0);
            cardArrayAdapter.notifyDataSetChanged();
        }
        if (cardArrayAdapter.getCount() > 0) {
            toggleListViewVisible(true);
        } else {
            toggleListViewVisible(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EXIT) {
            this.finish();
        }
    }

    @Override
    public void onResponse(Call<List<BidData>> call, Response<List<BidData>> response) {
        newRun = true;

        Log.d("DashBoradActivity", response.message() + " ");
        Log.d("DashBoradActivity", response.toString() + " ");
        List<BidData> bidData = response.body();
        if (bidData != null) {
            List<BidData> data = new ArrayList<>();
            for (int i = 0; i < bidData.size(); i++) {
                BidData mData = bidData.get(i);
                Log.d("DashBoradActivity", "city : " + user.getmCity() + "data city:  " + mData.getPickUpAddress());
                Log.d("DashBoradActivity", "city : " + mData.getPickUpAddress() + "data city:  " + mData.getPickUpAddress());
                if (mData.getTypeOfCar().equalsIgnoreCase(user.getmTypeOfCar()) &&
                        mData.getPickUpAddress().contains(user.getmCity())) {
                    mData.setAccurateTimeMillisInFuture();
                    data.add(mData);
                    Log.d("DashBoradActivity", "AAAAAAAAAAAAAAAAAAAAAAAAAddddd");
                }
                Log.d("DashBoradActivity", mData.toString());
                Log.d("DashBoradActivity", mData.toString());
                Log.d("DashBoradActivity", bidData.get(i).getOderNo() + " ");
            }
            Collections.sort(data);
            Log.d("DashBoradActivity", response.message() + " ");
            Log.d("DashBoradActivity", response.toString() + " ");
            for (int i = 0; i < bidData.size(); i++) {
                Log.d("DashBoradActivity", bidData.get(i).getOderNo() + " ");
            }
            setCardList(data);
        }
    }


    @Override
    public void onFailure(Call<List<BidData>> call, Throwable t) {
        Log.d("DashBoradActivity", "EEEEEEEEEEEEEE");
        newRun = true;
    }

    private void setListTimer() {
        mListHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (newRun) {
                    newRun = false;
                    myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
                    call = myApiEndpointInterface.getBidData(orderNo);
                    call.enqueue(DashBoradActivity.this);
                }
            }
        };
        mTimerUpdateCardList = new Timer();
        mTimerUpdateCardList.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mListHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 10000);
    }

    private void toggleListViewVisible(boolean visible) {
        if (visible) {
            mBidListView.setVisibility(View.VISIBLE);
            // mProgressBar.setVisibility(View.INVISIBLE);
            mNoResultPreviewTextView.setVisibility(View.INVISIBLE);
        } else {
            mBidListView.setVisibility(View.INVISIBLE);
            //  mProgressBar.setVisibility(View.VISIBLE);
            mNoResultPreviewTextView.setVisibility(View.VISIBLE);
        }
    }
}
