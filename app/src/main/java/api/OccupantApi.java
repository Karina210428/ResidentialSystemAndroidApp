package api;

import model.Occupant;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface OccupantApi {

    @GET("api/occupant/getAll")
    Call<List<Occupant>> getAll(@Header("Authorization") String token);

}
