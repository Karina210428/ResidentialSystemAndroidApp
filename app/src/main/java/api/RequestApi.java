package api;

import model.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.*;

public interface RequestApi {

    @POST("/api/request/add")
    Call<List<Request>> add(@Header("Authorization") String token, @Body Request request);

    @POST("/api/request/getAllById/{id}")
    Call<List<Request>> getAllById(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("/api/request/getAllByType")
    Call<List<Request>> getAllByType(@Header("Authorization") String token);

    @POST("/api/request/update")
    Call<Request> update(@Header("Authorization") String token, @Body Request request);

    @PUT("/api/request/update/{id}")
    Call<Request> update(@Header("Authorization") String token, @Body Request request, @Path("id") Integer id);

    @PUT("/api/request/updateEmployee/{id}")
    Call<Request> updateEmployee(@Header("Authorization") String token, @Body Request request, @Path("id") Integer id);

    @DELETE("/api/request/delete/{id}")
    Call<ResponseBody> delete(@Header("Authorization") String token, @Path("id") Integer id);
}
