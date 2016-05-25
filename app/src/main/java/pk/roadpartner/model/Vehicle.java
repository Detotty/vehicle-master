
package pk.roadpartner.model;

public class Vehicle {
    public String mName;
    public String mUrl;

    public Vehicle() {
    }

    public Vehicle(String mName, String mUrl) {
        this.mName = mName;
        this.mUrl = mUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "mName='" + mName + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
