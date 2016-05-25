package pk.roadpartner.model;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 3/27/2016.
 */
public class BidData implements Comparable<BidData> {
    public static final long REDUCE_COUNT_DOWN = 1000;
    public static final long COUNT_DOWN_INTERVAL = 1000;
    private String pickUpAddress;
    private String dropAddress;
    private String pickUpdate;
    private String offerPrice;
    private String oderNo;
    private String yourBidRate;
    private String vehicleType, typeOfCar, cellNo;
    private String cashFromCustomer;
    private String roadPartner;
    private String UserStatus;
    private long SystemTime = 0l;
    private long millisInFuture;
    private long serviveStartTime = 0l;
    private long serviceCurrentTime = 0l;
    private String bidderName;
    private String journyOneWayorTwoWay;

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    private String mTime;

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    private String km;

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getTypeOfCar() {
        return typeOfCar;
    }

    public void setTypeOfCar(String typeOfCar) {
        this.typeOfCar = typeOfCar;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public String getCashFromCustomer() {
        return cashFromCustomer;
    }

    public void setCashFromCustomer(String cashFromCustomer) {
        this.cashFromCustomer = cashFromCustomer;
    }
    public String getJournyOneWayorTwoWay ()
    {
        return journyOneWayorTwoWay;
    }

    public void setJournyOneWayorTwoWay (String journyOneWayorTwoWay)
    {
        this.journyOneWayorTwoWay = journyOneWayorTwoWay;
    }

    public String getUserStatus ()
    {
        return UserStatus;
    }

    public void setUserStatus (String UserStatus)
    {
        this.UserStatus = UserStatus;
    }

    public BidData(String oderNo, String offerPrice, String pickUpdate, String dropAddress, String pickUpAddress, long millisInFuture) {
        this.oderNo = oderNo;
        this.offerPrice = offerPrice;
        this.pickUpdate = pickUpdate;
        this.dropAddress = dropAddress;
        this.pickUpAddress = pickUpAddress;
        this.millisInFuture = millisInFuture;
    }

    public long getServiveStartTime() {
        return serviveStartTime;
    }

    public void setServiveStartTime(long serviveStartTime) {
        this.serviveStartTime = serviveStartTime;
    }

    public long getServiceCurrentTime() {
        return serviceCurrentTime;
    }

    public void setServiceCurrentTime(long serviceCurrentTime) {
        this.serviceCurrentTime = serviceCurrentTime;
    }

    public BidData(String cashFromCustomer, String roadPartner) {
        this.cashFromCustomer = cashFromCustomer;
        this.roadPartner = roadPartner;
    }

    public long getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(long systemTime) {
        SystemTime = systemTime;
    }

    public String getYourBidRate() {
        return yourBidRate;
    }

    public void setYourBidRate(String yourBidRate) {
        this.yourBidRate = yourBidRate;
    }

    public BidData(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public String getOderNo() {
        return oderNo;
    }

    public void setOderNo(String oderNo) {
        this.oderNo = oderNo;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getPickUpdate() {
        return pickUpdate;
    }

    public void setPickUpdate(String pickUpdate) {
        this.pickUpdate = pickUpdate;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public long getMillisInFuture() {
        return this.millisInFuture;
    }


    public String getTimeCounter() {
        String h = String.format("%02d:", TimeUnit.MILLISECONDS.toHours(millisInFuture));
        String m = String.format("%02d:", TimeUnit.MILLISECONDS.toMinutes(millisInFuture) % TimeUnit.HOURS.toMinutes(1));
        String s = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisInFuture) % TimeUnit.MINUTES.toSeconds(1));
        return h + m + s;
    }

    public void setReduceCountDown() {
        this.millisInFuture = millisInFuture - REDUCE_COUNT_DOWN;
    }

    @Override
    public int compareTo(BidData another) {
        int lastCmp = oderNo.compareTo(another.getOderNo());
        return (lastCmp != 0 ? lastCmp : oderNo.compareTo(another.getOderNo()));
    }

    public void setAccurateTimeMillisInFuture() {
        long timeDifferent = (this.serviceCurrentTime - this.serviveStartTime) * 1000;
        this.millisInFuture = this.millisInFuture - timeDifferent;
    }

    @Override
    public String toString() {
        return "BidData{" +
                "pickUpAddress='" + pickUpAddress + '\'' +
                ", dropAddress='" + dropAddress + '\'' +
                ", pickUpdate='" + pickUpdate + '\'' +
                ", offerPrice='" + offerPrice + '\'' +
                ", oderNo='" + oderNo + '\'' +
                ", yourBidRate='" + yourBidRate + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", typeOfCar='" + typeOfCar + '\'' +
                ", cellNo='" + cellNo + '\'' +
                ", cashFromCustomer='" + cashFromCustomer + '\'' +
                ", roadPartner='" + roadPartner + '\'' +
                ", SystemTime=" + SystemTime +
                ", millisInFuture=" + millisInFuture +
                ", serviveStartTime=" + serviveStartTime +
                ", serviceCurrentTime=" + serviceCurrentTime +
                ", bidderName='" + bidderName + '\'' +
                ", mTime='" + mTime + '\'' +
                ", km='" + km + '\'' +
                '}';
    }
}
//    return   String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisInFuture),
//                TimeUnit.MILLISECONDS.toMinutes(millisInFuture) % TimeUnit.HOURS.toMinutes(1),
//                TimeUnit.MILLISECONDS.toSeconds(millisInFuture) % TimeUnit.MINUTES.toSeconds(1));