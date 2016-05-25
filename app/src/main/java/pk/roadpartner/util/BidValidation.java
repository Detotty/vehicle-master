package pk.roadpartner.util;

import android.util.Log;

/**
 * Created by Gazi Rimon on 4/15/2016.
 */
public class BidValidation {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private String yourBid;
    private String bidOffer;
    private String mgs;
    private boolean isValid;
    private String km;

    public BidValidation(String yourBid, String bidOffer, String km) {
        this.yourBid = yourBid;
        this.bidOffer = bidOffer;
        this.km = km;
        calculate();
    }

    private void calculate() {
        try {
            double bid = Double.parseDouble(yourBid);
            double offer = Double.parseDouble(bidOffer);
            double mKm = Double.parseDouble(km);
            Log.d(LOG_TAG, "bid" + bid + "         offer" + offer + "     " + mKm);
            double pValue, nValue;

//            KM              Changes Allow
//
//                            +/-
//             1-12           200%
//             13-25           100%
//             25+             30%

            if (mKm >= 1 && mKm <= 12) {
                pValue = getPercentageValue(200, offer);
                nValue = getPercentageValue(30, offer);
                setValidation(pValue, nValue, bid);
            } else if (mKm >= 13 && mKm <= 25) {
                pValue = getPercentageValue(150, offer);
                nValue = getPercentageValue(50, offer);
                setValidation(pValue, nValue, bid);
            } else if (mKm > 25) {
                pValue = getPercentageValue(130, offer);
                nValue = getPercentageValue(70, offer);
                setValidation(pValue, nValue, bid);
            }
        } catch (NumberFormatException e) {
            Log.d(LOG_TAG, "ex");
            mgs = "Invalid input";
            isValid = false;
        }
    }

    private void setValidation(double pValue, double nValue, double bid) {
        Log.d(LOG_TAG, "pValue" + pValue + "         nValue" + nValue);
        if (bid > pValue) {
            isValid = false;
            mgs = "you bid is high";
        } else if (bid < nValue) {
            mgs = "you bid is low";
            isValid = false;
        } else {
            mgs = "";
            isValid = true;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMgs() {
        return mgs;
    }

    private double getPercentageValue(double percentage, double offer) {
        double d = (double) (percentage / 100);
        double m = offer * d;
        return m;
    }
}
