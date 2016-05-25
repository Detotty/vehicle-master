package pk.roadpartner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.lang.reflect.Field;

import rimon.rsa.vehicle.R;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.User;
import pk.roadpartner.ui.AnySectionPageAdapter;
import pk.roadpartner.ui.BidProcessingActivity;
import pk.roadpartner.ui.DashBoradActivity;
import pk.roadpartner.ui.FixedSpeedScroller;
import pk.roadpartner.ui.OnFragmentInteractionListener;
import pk.roadpartner.view.AnySwipeableViewPager;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private ImageView mLogoImage;
    private User userData;
    private UserDatabase userDatabase;
    private AnySwipeableViewPager pager;
    private AnySectionPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDatabase = new UserDatabase(this);
        userData = userDatabase.getUserData();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userData.isLoging()) {
                    if (userData.isBidRunning()) {
                        Gson gson = new Gson();
                        String data = gson.toJson(userData);
                        Intent intent = new Intent(MainActivity.this, BidProcessingActivity.class);
                        intent.putExtra(BidProcessingActivity.BID_DATA_PROCESSING_ID, data);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, DashBoradActivity.class));
                    }
                    finish();
                } else {
                    setLoginView();
                }
            }
        }, 2000);
    }

    private void setLoginView() {

        mLogoImage = (ImageView) findViewById(R.id.logoImage);
        mLogoImage.setImageResource(R.mipmap.ic_launcher);
        pager = (AnySwipeableViewPager) findViewById(R.id.pager);
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        pager.setSwipeable(true);
        adapter = new AnySectionPageAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(int pageNumber) {
        Log.d("MainActivity", userData.toString());
        pager.setCurrentItem(pageNumber);
    }

    public User getUserData() {
        return userData;
    }
}
