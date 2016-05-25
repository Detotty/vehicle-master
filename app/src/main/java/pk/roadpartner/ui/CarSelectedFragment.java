package pk.roadpartner.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import pk.roadpartner.MainActivity;
import rimon.rsa.vehicle.R;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.card.CarSelectCard;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.MyResponse;
import pk.roadpartner.model.User;
import pk.roadpartner.model.Vehicle;
import pk.roadpartner.model.VehicleType;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarSelectedFragment extends Fragment implements View.OnClickListener, Callback<List<VehicleType>>, OnCardSelectListener {

    private View view;
    public static final int PAGE_NUMBER = 1;
    private NiceSpinner vehicleTypeSpinner;
    private ImageButton backImageButton;
    private CardGridView gridView;
    private Retrofit retrofit;
    private Call<List<VehicleType>> call;
    private MyApiEndpointInterface myApiEndpointInterface;
    private String LOG_TAG = this.getClass().getSimpleName();
    private List<CardGridArrayAdapter> mCardArrayAdapter;
    private ArrayAdapter<String> vehicleTypeAdapter;
    private OnFragmentInteractionListener listener;
    private ProgressBar registrationProgressBar;
    private Button createAcccountButton, mLetsGoButton, retryButton;
    ;
    private User userData;
    private UserDatabase userDatabase;
    private Call<MyResponse> responseCall;
    private TextView message;
    private RelativeLayout userInputLinearLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_car_selected, container, false);
        initView();
        setRetrofit();
        getUserData();
        return view;
    }

    private void getUserData() {
        userDatabase = new UserDatabase(getContext());
        userData = ((MainActivity) getActivity()).getUserData();
    }

    private void initView() {
        createAcccountButton = (Button) view.findViewById(R.id.createAcccountButton);
        createAcccountButton.setOnClickListener(this);
        createAcccountButton.setEnabled(false);
        backImageButton = (ImageButton) view.findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(this);
        vehicleTypeSpinner = (NiceSpinner) view.findViewById(R.id.vehicleTypeSpinner);
        gridView = (CardGridView) view.findViewById(R.id.carddemo_grid_base1);

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        vehicleTypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item
        );

        vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mCardArrayAdapter = new ArrayList<>();
        registrationProgressBar = (ProgressBar) view.findViewById(R.id.registrationProgressBar);

        mLetsGoButton = (Button) view.findViewById(R.id.letsGoButton);
        mLetsGoButton.setOnClickListener(this);
        retryButton = (Button) view.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(this);
        message = (TextView) view.findViewById(R.id.message);
        userInputLinearLayout = (RelativeLayout) view.findViewById(R.id.userInputLinearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImageButton:
                listener.onFragmentInteraction(LoginDetailsFragment.PAGE_NUMBER);
                break;
            case R.id.createAcccountButton:
                hideSoftKeyboard();
                createAccount();
                break;
            case R.id.letsGoButton:
                getContext().startActivity(new Intent(getContext(), DashBoradActivity.class));
                ((Activity) getContext()).finish();
                break;
            case R.id.retryButton:
                loginViewEnable();
                break;
        }
    }

    private void letsGoViewEnable() {
        ProgressBarDisable();
        mLetsGoButton.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        message.setText(R.string.re_com);
    }

    private void letsGoViewDisable() {
        mLetsGoButton.setVisibility(View.GONE);
        message.setText("");
        message.setVisibility(View.GONE);
    }

    private void retryViewEnable(int id) {
        ProgressBarDisable();
        retryButton.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        message.setText(id);
    }

    private void retryViewDisable() {
        retryButton.setVisibility(View.GONE);
        message.setText("");
        message.setVisibility(View.GONE);
    }

    private void loginViewEnable() {
        retryViewDisable();
        letsGoViewDisable();
        userInputLinearLayout.setVisibility(View.VISIBLE);
    }


    private void ProgressBarEnable() {
        registrationProgressBar.setVisibility(View.VISIBLE);
    }

    private void ProgressBarDisable() {
        registrationProgressBar.setVisibility(View.GONE);
    }

    private void loginViewDisable() {
        userInputLinearLayout.setVisibility(View.GONE);
    }

    public void hideSoftKeyboard() {
        if (((Activity) getContext()).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void createAccount() {

        if (checkInput() == false)
            return;

        loginViewDisable();
        ProgressBarEnable();
        Log.d(LOG_TAG, userData.toString());
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
        responseCall = myApiEndpointInterface.createUser(userData);
        responseCall.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                myResponse.getMessage();
                if (myResponse.getRegistationResponse().contains("1")) {
                    userData.setIsLoging(true);
                    userDatabase.setUserData(userData);
                    letsGoViewEnable();
                } else if (myResponse.getRegistationResponse().contains("3")) {
                    retryViewEnable(R.string.user_exit);
                } else {
                    retryViewEnable(R.string.re_fail);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                retryViewEnable(R.string.re_fail);
            }
        });
    }

    private boolean checkInput() {
        int mCarType = vehicleTypeSpinner.getSelectedIndex();
        String vehicleType = vehicleTypeAdapter.getItem(mCarType);
        userData.setmTypeOfVehicle(vehicleType);
        CardGridArrayAdapter adapter = mCardArrayAdapter.get(mCarType);
        for (int i = 0; i < adapter.getCount(); i++) {
            CarSelectCard card = (CarSelectCard) adapter.getItem(i);
            if (card.isCardSelected()) {
                Vehicle v = card.getVehicleData();
                userData.setmTypeOfCar(v.getmName());
                return true;
            }
        }
        return false;
    }

    private void setRetrofit() {
        RoadApp app = (RoadApp) getActivity().getApplicationContext();
        retrofit = app.getRetrofit();
        myApiEndpointInterface = retrofit.create(MyApiEndpointInterface.class);
        call = myApiEndpointInterface.getVehicle();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<VehicleType>> call, Response<List<VehicleType>> response) {
        ProgressBarDisable();
        List<VehicleType> vehicleTypes = response.body();
        ArrayList<String> arrayStrings = new ArrayList<String>();

        for (VehicleType vType : vehicleTypes) {
//            vehicleTypeAdapter.add(vType.getmTypeOfVehicle());
            arrayStrings.add(vType.getmTypeOfVehicle());
            List<Vehicle> vehicle = vType.getVehicles();
            List<Card> cards = new ArrayList<Card>();
            ObjectComparator comparator = new ObjectComparator();
            Collections.sort(vehicle, comparator);
            for (Vehicle mVehicle : vehicle) {
                CardHeader c = new CardHeader(getContext());
                Card mCard = new CarSelectCard(getContext(), mVehicle, this);
                mCard.addCardHeader(c);
                mCard.setId(mVehicle.getmName());
                cards.add(mCard);
            }
            mCardArrayAdapter.add(new CardGridArrayAdapter(getContext(), cards));
        }
        Collections.reverse(arrayStrings);
        Collections.reverse(mCardArrayAdapter);
        vehicleTypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,arrayStrings
        );
        vehicleTypeSpinner.setAdapter(vehicleTypeAdapter);
        gridView.setAdapter(mCardArrayAdapter.get(0));
        gridView.refreshDrawableState();
        createAcccountButton.setEnabled(true);
    }

    @Override
    public void onFailure(Call<List<VehicleType>> call, Throwable t) {
        setRetrofit();
        ProgressBarEnable();
    }

    @Override
    public void onFragmentInteraction(String id) {
        resetCardDefaultView(id);
        notifyAllAdapter();
    }

    private void notifyAllAdapter() {
        for (CardGridArrayAdapter adapter : mCardArrayAdapter) {
            adapter.notifyDataSetChanged();
        }
    }

    private void changeAdapter(int position) {
        gridView.setAdapter(mCardArrayAdapter.get(position));
        resetCardDefaultView("-1");
        notifyAllAdapter();
    }

    private void resetCardDefaultView(String id) {
        for (CardGridArrayAdapter adapter : mCardArrayAdapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                CarSelectCard card = (CarSelectCard) adapter.getItem(i);
                if (card.getId().compareTo(id) != 0) {
                    card.setIsCardSelected(false);
                }
            }
        }
    }

    public class ObjectComparator implements Comparator<Vehicle> {

        public int compare(Vehicle obj1, Vehicle obj2) {
            return obj1.getmName().compareTo(obj2.getmName());
        }

    }
}
