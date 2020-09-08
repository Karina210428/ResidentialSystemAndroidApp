package api;

import android.os.Build;

import androidx.annotation.RequiresApi;
import com.example.residentialsystemapp.BuildConfig;
import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.75.1:8081/";
    private static RetrofitClient instance;
    private Retrofit retrofit;

    public static synchronized RetrofitClient getInstance(){
        if(instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext)
                    -> LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, typeOfSrc, context)
                    -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext)
                    -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (date, typeOfSrc, context)
                    -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();


    private RetrofitClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public AuthApi getAuthApi(){
        return retrofit.create(AuthApi.class);
    }

    public OccupantApi getOccupantApi(){
        return retrofit.create(OccupantApi.class);
    }

    public RequestApi getRequestApi() {return retrofit.create(RequestApi.class); }

    public SpecializationApi getSpecializationApi() {return retrofit.create(SpecializationApi.class); }

    public EmployeeApi getEmployeeApi() {return retrofit.create(EmployeeApi.class); }

    public NewsApi getNewsApi() {return  retrofit.create(NewsApi.class);}

}


