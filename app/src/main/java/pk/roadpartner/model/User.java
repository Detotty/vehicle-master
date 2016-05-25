package pk.roadpartner.model;

/**
 * Created by User on 3/24/2016.
 */
public class User {

    private final int DEFAULT_BID_ABILITY = 5;
    private String mCity, mCnic, mAddress, mMobileNumber, mUserName;
    private boolean isLoging;
    private BidData bidData;
    private boolean isBidRunning;
    private String mTypeOfVehicle, mTypeOfCar;
    private long bidCalculateTime;
    private int currentBidAbility = DEFAULT_BID_ABILITY;

    public boolean isBidDetailsAvailable() {
        return currentBidAbility > 0;
    }

    public int getCurrentBidAbility() {
        return currentBidAbility;
    }

    public void deCreaseBidAbility() {
        this.currentBidAbility--;
    }

    public void setCurrentBidAbility() {
        this.currentBidAbility = DEFAULT_BID_ABILITY;
    }

    public User() {
    }

    public User(String mCnic, String mCity, String mAddress, String mMobileNumber, String mUserName) {
        this.mCnic = mCnic;
        this.mCity = mCity;
        this.mAddress = mAddress;
        this.mMobileNumber = mMobileNumber;
        this.mUserName = mUserName;
        isLoging = false;
    }

    public String getmTypeOfCar() {
        return mTypeOfCar;
    }

    public void setmTypeOfCar(String mTypeOfCar) {
        this.mTypeOfCar = mTypeOfCar;
    }

    public String getmTypeOfVehicle() {
        return mTypeOfVehicle;
    }

    public void setmTypeOfVehicle(String mTypeOfVehicle) {
        this.mTypeOfVehicle = mTypeOfVehicle;
    }

    public boolean isBidRunning() {
        return isBidRunning && bidData != null;
    }

    public void setIsBidRunning(boolean isBidRunning) {
        this.isBidRunning = isBidRunning;
    }

    public BidData getBidData() {
        return bidData;
    }

    public void setBidData(BidData bidData) {
        this.bidData = bidData;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCnic() {
        return mCnic;
    }

    public void setmCnic(String mCnic) {
        this.mCnic = mCnic;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmMobileNumber() {
        return mMobileNumber;
    }

    public void setmMobileNumber(String mMobileNumber) {
        this.mMobileNumber = mMobileNumber;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public boolean isLoging() {
        return isLoging;
    }

    public void setIsLoging(boolean isLoging) {
        this.isLoging = isLoging;
    }

    @Override
    public String toString() {
        return "User{" +
                "mCity='" + mCity + '\'' +
                ", mCnic='" + mCnic + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mMobileNumber='" + mMobileNumber + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", isLoging=" + isLoging +
                ", bidData=" + bidData +
                ", isBidRunning=" + isBidRunning +
                ", mTypeOfVehicle='" + mTypeOfVehicle + '\'' +
                ", mTypeOfCar='" + mTypeOfCar + '\'' +
                '}';
    }
}
