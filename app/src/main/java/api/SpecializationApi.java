package api;

import java.util.List;

import model.Occupant;
import model.Specialization;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SpecializationApi {

    @GET("api/specialization/getAll")
    Call<List<Specialization>> getAll();
}
