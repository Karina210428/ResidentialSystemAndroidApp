package api;

import model.Employee;
import model.News;
import model.Request;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;

public interface NewsApi {

    @GET("/api/news/getAll")
    Call<List<News>> getAll(@Header("Authorization") String token);

    @POST("/api/news/add")
    Call<List<News>> add(@Header("Authorization") String token, @Body News news);

}
