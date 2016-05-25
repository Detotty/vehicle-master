package pk.roadpartner.model;

/**
 * Created by User on 3/29/2016.
 */
public class OrderNo {

    public static String ALL="all";

    private String mTypeOfVehicle;
    private String mTypeOfCar;
    private String orderNo;
    private String mCity;

    public String getmCnic() {
        return mCnic;
    }

    public void setmCnic(String mCnic) {
        this.mCnic = mCnic;
    }

    private String mCnic;

    public OrderNo(){
    }
    public OrderNo(String mTypeOfCar, String mTypeOfVehicle, String orderNo, String mCity) {
        this.mTypeOfCar = mTypeOfCar;
        this.mTypeOfVehicle = mTypeOfVehicle;
        this.orderNo = orderNo;
        this.mCity = mCity;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getmTypeOfVehicle() {
        return mTypeOfVehicle;
    }

    public void setmTypeOfVehicle(String mTypeOfVehicle) {
        this.mTypeOfVehicle = mTypeOfVehicle;
    }

    public String getmTypeOfCar() {
        return mTypeOfCar;
    }

    public void setmTypeOfCar(String mTypeOfCar) {
        this.mTypeOfCar = mTypeOfCar;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    @Override
    public String toString() {
        return "OrderNo{" +
                "mTypeOfVehicle='" + mTypeOfVehicle + '\'' +
                ", mTypeOfCar='" + mTypeOfCar + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mCnic='" + mCnic + '\'' +
                '}';
    }
}
