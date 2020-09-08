package api;

import model.Employee;
import model.Location;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface EmployeeApi {

    @GET("/api/employee/getAll")
    Call<List<Employee>> getAll(@Header("Authorization") String token);

    @POST("api/employee/sentLocation")
    Call<ResponseBody> sentLocation(@Header("Authorization") String token, @Body Location location);

}
