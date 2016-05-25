package pk.roadpartner.model;

/**
 * Created by User on 3/29/2016.
 */
public class MyResponse {
    public MyResponse(String registationResponse, String message) {
        this.registationResponse = registationResponse;
        this.message = message;
    }

    public String getRegistationResponse() {
        return registationResponse;
    }

    public void setRegistationResponse(String registationResponse) {
        this.registationResponse = registationResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String registationResponse;
    private  String message;
}
