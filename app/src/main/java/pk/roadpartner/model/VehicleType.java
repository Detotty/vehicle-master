
package pk.roadpartner.model;

import java.util.ArrayList;
import java.util.List;

public class VehicleType {
    public String mTypeOfVehicle;
    public List<Vehicle> vehicle = new ArrayList<Vehicle>();

    public VehicleType() {
    }

    public VehicleType(String mTypeOfVehicle, List<Vehicle> vehicles) {
        this.mTypeOfVehicle = mTypeOfVehicle;
        this.vehicle = vehicles;
    }

    public String getmTypeOfVehicle() {
        return mTypeOfVehicle;
    }

    public void setmTypeOfVehicle(String mTypeOfVehicle) {
        this.mTypeOfVehicle = mTypeOfVehicle;
    }

    public List<Vehicle> getVehicles() {
        return vehicle;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicle = vehicles;
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "mTypeOfVehicle='" + mTypeOfVehicle + '\'' +
                ", vehicles=" + vehicle +
                '}';
    }
}
