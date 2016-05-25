package pk.roadpartner.api;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import pk.roadpartner.model.BidData;
import pk.roadpartner.model.BidResponse;
import pk.roadpartner.model.MyResponse;
import pk.roadpartner.model.OrderNo;
import pk.roadpartner.model.User;
import pk.roadpartner.model.VehicleType;

/**
 * Created by User on 3/29/2016.
 */
public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @POST("/service.php")
    Call<List<BidData>> getBidData(@Body OrderNo orderNo);

    @POST("/bidconfirm.php")
    Call<BidResponse> bidConFirm(@Body User user);

    @POST("/registration.php")
    Call<MyResponse> createUser(@Body User user);

    @POST("/placebid.php")
    Call<BidResponse> placeUserBid(@Body User user);

    @POST("/bidwinner.php")
    Call<User> bidWinner(@Body User user);

    @GET("/file_display.php")
    Call<List<VehicleType>> getVehicle();
}
