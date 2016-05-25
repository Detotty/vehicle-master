package pk.roadpartner.card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import it.gmariotti.cardslib.library.internal.Card;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rimon.rsa.vehicle.R;
import pk.roadpartner.RoadApp;
import pk.roadpartner.api.MyApiEndpointInterface;
import pk.roadpartner.database.UserDatabase;
import pk.roadpartner.model.MyResponse;
import pk.roadpartner.model.User;
import pk.roadpartner.ui.DashBoradActivity;

/**
 * Created by User on 3/22/2016.
 */
public class LoginCard extends Card {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final int CNIC_LENTH = 13;
    private final int MOBILE_NUMBER_LENTH = 11;
    private User userData;
    private UserDatabase userDatabase;
    private EditText mCnicEditText, mAddressEditText, mMobileNumberEditText, mUserNameEditText;
    private AutoCompleteTextView mCityEditText;
    private String city, cnic, address, mobileNumber, userName;
    private Button mCreateAcccountButton, mLetsGoButton, retryButton;
    private ProgressBar mRegistrationProgressBar;
    private LinearLayout mUserInputLinearLayout;
    private Retrofit retrofit;
    private MyApiEndpointInterface apiService;
    private Call<MyResponse> responseCall;
    private ArrayAdapter<String> adapter;
    private String cities[];
    private TextView message;

    public LoginCard(Context context, User userData, UserDatabase userDatabase) {
        this(context, R.layout.card_login);
        this.userData = userData;
        this.userDatabase = userDatabase;
        RoadApp app = (RoadApp) context.getApplicationContext();
        retrofit = app.getRetrofit();
        cities = context.getResources().getStringArray(R.array.cities);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cities);
    }

    private LoginCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        mUserNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        mAddressEditText = (EditText) view.findViewById(R.id.addressEditText);
        mCnicEditText = (EditText) view.findViewById(R.id.cnicEditText);
        mMobileNumberEditText = (EditText) view.findViewById(R.id.mobileNumberEditText);

        mCityEditText = (AutoCompleteTextView) view.findViewById(R.id.cityEditText);
        mCityEditText.setAdapter(adapter);

        mCityEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int id,
                                    long arg3) {
                city=parent.getItemAtPosition(id).toString();
            }
        });

        mCreateAcccountButton = (Button) view.findViewById(R.id.createAcccountButton);
        mCreateAcccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidatiuon();
            }
        });

        mRegistrationProgressBar = (ProgressBar) view.findViewById(R.id.registrationProgressBar);
        mUserInputLinearLayout = (LinearLayout) view.findViewById(R.id.userInputLinearLayout);
        mLetsGoButton = (Button) view.findViewById(R.id.letsGoButton);
        mLetsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().startActivity(new Intent(getContext(), DashBoradActivity.class));
                ((Activity) getContext()).finish();
            }
        });
        retryButton = (Button) view.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginView();
            }
        });
        message = (TextView) view.findViewById(R.id.message);
    }

    private void inputValidatiuon() {
        userName = mUserNameEditText.getText().toString().trim();
        mobileNumber = mMobileNumberEditText.getText().toString().trim();
        address = mAddressEditText.getText().toString().trim();

        cnic = mCnicEditText.getText().toString().trim();
        mUserNameEditText.setError(null);
        mMobileNumberEditText.setError(null);
        mAddressEditText.setError(null);
        mCityEditText.setError(null);
        mCnicEditText.setError(null);

        if (userName == null || userName.isEmpty()) {
            mUserNameEditText.setError(getContext().getString(R.string.invalidInput));
        } else if (mobileNumber == null || mobileNumber.isEmpty() || mobileNumber.length() != MOBILE_NUMBER_LENTH) {
            mMobileNumberEditText.setError(getContext().getString(R.string.invalidInput));
        } else if (address == null || address.isEmpty()) {
            mAddressEditText.setError(getContext().getString(R.string.invalidInput));
        } else if (city == null || city.isEmpty()) {
            mCityEditText.setError(getContext().getString(R.string.invalidInput));
        } else if (cnic == null || cnic.isEmpty() || cnic.length() != CNIC_LENTH) {
            mCnicEditText.setError(getContext().getString(R.string.invalidInput));
        } else {
            createAccount();
        }
    }

    private void createAccount() {
        hideSoftKeyboard();
        mUserInputLinearLayout.setVisibility(View.INVISIBLE);
        mRegistrationProgressBar.setVisibility(View.VISIBLE);

        userData.setmUserName(userName);
        userData.setmMobileNumber(mobileNumber);
        userData.setmAddress(address);
        userData.setmCity(city);
        userData.setmCnic(cnic);
        Gson gson = new Gson();
        String d = gson.toJson(userData);
        Log.d(LOG_TAG, d);

        apiService = retrofit.create(MyApiEndpointInterface.class);
        responseCall = apiService.createUser(userData);
        responseCall.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                myResponse.getMessage();
                if (myResponse.getRegistationResponse().contains("1")) {
                    userData.setIsLoging(true);
                    userDatabase.setUserData(userData);
                    letsGoView();
                } else if (myResponse.getRegistationResponse().contains("3")) {
                    retryView(R.string.user_exit);
                } else {
                    retryView(R.string.re_fail);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                retryView(R.string.re_fail);
            }
        });
    }

    private void letsGoView() {
        mRegistrationProgressBar.setVisibility(View.INVISIBLE);
        mLetsGoButton.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        message.setText(R.string.re_com);
    }

    private void retryView(int id) {
        mRegistrationProgressBar.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        message.setText(id);
    }

    private void loginView() {
        mUserInputLinearLayout.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
    }

    public void hideSoftKeyboard() {
        if (((Activity) getContext()).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) getContext()).getCurrentFocus().getWindowToken(), 0);
        }
    }
}
