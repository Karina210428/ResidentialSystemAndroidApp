package api;

import io.reactivex.Observable;
import io.reactivex.Single;
import model.AuthenticationRequest;
import model.AuthenticationResponse;
import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/signin")
    Call<AuthenticationResponse> signIn(@Body AuthenticationRequest authenticationRequest);

    @GET("/api/auth/me")
    Call<User> getUser(@Header("Authorization") String token);

    @POST("/api/auth/registrationOccupant")
    Call<User> createUserOccupant(@Body User user);

    @POST("/api/auth/registrationEmployee")
    Call<User> createUserEmployee(@Body User user);
}
