package pk.roadpartner.card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import it.gmariotti.cardslib.library.internal.Card;
import rimon.rsa.vehicle.R;
import pk.roadpartner.model.BidData;
import pk.roadpartner.ui.BidDetailsActivity;
import pk.roadpartner.ui.DashBoradActivity;
import pk.roadpartner.view.DigitalClock;

/**
 * Created by Gazi Rimon on 3/24/2016.
 */
public class BidCard extends Card implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private BidData bidData;
    private TextView mPickUpTextView, mDropTextView, mPickUpDateTextView, pickUpTimeTextView;
    private Button mBidButton;
    private DigitalClock timeCounterDigitalClock;
    private Gson gson;
    ImageView journey;
    private boolean isBidDetails;

    public void setIsBidDetails(boolean isBidDetails) {
        this.isBidDetails = isBidDetails;
    }

    public BidCard(Context context, BidData data, boolean isBidDetails) {
        this(context, R.layout.bid_card);
        this.bidData = data;
        gson = new Gson();
        this.isBidDetails = isBidDetails;
    }

    private BidCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        mBidButton = (Button) view.findViewById(R.id.bidButton);
//        mBidButton.setEnabled(false);
        mBidButton.setOnClickListener(this);
//        mBidButton.setEnabled(isBidDetails);
        try {
            if (bidData.getUserStatus().equals("active"))
                mBidButton.setEnabled(isBidDetails);
            else
                mBidButton.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeCounterDigitalClock = (DigitalClock) view.findViewById(R.id.timeCounterDigitalClock);
        timeCounterDigitalClock.setText(bidData.getTimeCounter());
        mPickUpTextView = (TextView) view.findViewById(R.id.pickUpTextView);
        mPickUpTextView.setText(bidData.getPickUpAddress());
        mDropTextView = (TextView) view.findViewById(R.id.dropTextView);
        mDropTextView.setText(bidData.getDropAddress());
        mPickUpDateTextView = (TextView) view.findViewById(R.id.pickUpDateTextView);
        mPickUpDateTextView.setText(bidData.getPickUpdate());
        pickUpTimeTextView = (TextView) view.findViewById(R.id.pickUpTimeTextView);
        pickUpTimeTextView.setText(bidData.getmTime());
        journey = (ImageView) view.findViewById(R.id.j_way);
        if (bidData.getJournyOneWayorTwoWay().equals("1"))
            journey.setImageResource(R.drawable.oneway);
        else
            journey.setImageResource(R.drawable.twoway);

    }

    @Override
    public void onClick(View v) {
        String data = gson.toJson(bidData);
        Log.d(LOG_TAG, data);
        Intent intent = new Intent(getContext(), BidDetailsActivity.class);
        intent.putExtra(BidDetailsActivity.BID_DATA_ID, data);
        ((Activity) getContext()).startActivityForResult(intent, DashBoradActivity.REQUEST_EXIT);
    }

    public BidData getBidData() {
        return bidData;
    }

    public void setBidData(BidData bidData) {
        this.bidData = bidData;
    }
}
