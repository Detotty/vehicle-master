package pk.roadpartner.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;
import rimon.rsa.vehicle.R;
import pk.roadpartner.model.Vehicle;
import pk.roadpartner.ui.OnCardSelectListener;

/**
 * Created by User on 4/12/2016.
 */
public class CarSelectCard extends Card {
    private ImageView carImage;
    private TextView carTitleTextview;
    private Vehicle vehicle;
    private OnCardSelectListener onCardSelectListener;
    private boolean isCardSelected;

    public CarSelectCard(Context context, Vehicle vehicle, OnCardSelectListener onCardSelectListener) {
        this(context, R.layout.card_select_car);
        this.vehicle = vehicle;
        this.onCardSelectListener = onCardSelectListener;
    }

    private CarSelectCard(Context context, int innerLayout) {
        super(context, R.layout.card_select_car);
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                onCardSelectListener.onFragmentInteraction(getId());
                setIsCardSelected(!isCardSelected);
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        carTitleTextview = (TextView) view.findViewById(R.id.carTitleTextview);
        carImage = (ImageView) view.findViewById(R.id.carImage);
        carTitleTextview.setText(vehicle.getmName());
        carImage.setEnabled(false);
        carTitleTextview.setEnabled(false);
        Picasso.with(getContext()).load(vehicle.getmUrl()).resize(80, 80).centerInside().into(carImage);
        selectCard();
    }

    public boolean isCardSelected() {
        return isCardSelected;
    }

    public void setIsCardSelected(boolean isCardSelected) {
        this.isCardSelected = isCardSelected;
    }

    private void selectCard() {
        if (isCardSelected) setBackgroundResourceId(R.color.colorAccent);
        else setBackgroundResourceId(android.R.color.white);
    }

    public Vehicle getVehicleData() {
        return vehicle;
    }
}

