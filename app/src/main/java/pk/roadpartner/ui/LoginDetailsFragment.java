package pk.roadpartner.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import pk.roadpartner.MainActivity;
import rimon.rsa.vehicle.R;
import pk.roadpartner.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDetailsFragment extends Fragment {

    private View view;
    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final int PAGE_NUMBER = 0;
    private final int CNIC_LENTH = 13;
    private final int MOBILE_NUMBER_LENTH = 11;
    private User userData;
    private EditText mCnicEditText, mAddressEditText, mMobileNumberEditText, mUserNameEditText;
    private AutoCompleteTextView mCityEditText;
    private String city, cnic, address, mobileNumber, userName;
    private Button mFowWordButton, mLetsGoButton, retryButton;

    private ArrayAdapter<String> adapter;
    private String cities[];
    private OnFragmentInteractionListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login_details, container, false);
        initView();
        return view;
    }

    private void initView() {
        cities = getResources().getStringArray(R.array.cities);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item, cities);

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
                city = parent.getItemAtPosition(id).toString();
            }
        });

        mFowWordButton = (Button) view.findViewById(R.id.fowWordButton);
        mFowWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });
        getUserData();
    }

    private void getUserData() {
        userData = ((MainActivity) getActivity()).getUserData();
    }


    private void inputValidation() {
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
            userData.setmUserName(userName);
            userData.setmMobileNumber(mobileNumber);
            userData.setmAddress(address);
            userData.setmCity(city);
            userData.setmCnic(cnic);
            listener.onFragmentInteraction(CarSelectedFragment.PAGE_NUMBER);
        }
    }
}
