package pk.roadpartner.model;

/**
 * Created by Gazi Rimon on 3/30/2016.
 */
public class BidResponse {
    public BidResponse(String bidResponse, String message) {
        this.bidResponse = bidResponse;
        this.message = message;
    }

    public String getBidResponse() {
        return bidResponse;
    }

    public void setBidResponse(String bidResponse) {
        this.bidResponse = bidResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String bidResponse, message;
}
