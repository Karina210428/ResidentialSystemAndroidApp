package presenter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;
import api.RetrofitClient;
import model.Specialization;
import view.SendRequestView;
import model.Request;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendRequestPresenter {

    private SendRequestView sendRequestView;

    public SendRequestPresenter(SendRequestView sendRequestView){
        this.sendRequestView = sendRequestView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendRequest(String problem, Specialization specialization, Context context){
        new Handler().postDelayed(() -> {
            if (TextUtils.isEmpty(problem)) {
                sendRequestView.showRequestError();
                return;
            }else {
                sendRequestToApi(problem,specialization,context);
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Request sendRequestToApi(String problem, Specialization specialization, Context context) {
        String token = SharedPreference.getInstance(context).getToken();
        Request request = new Request();
        request.setDone(false);
        request.setSpecialization(specialization);
        request.setDateOfRequest(LocalDate.now());
        request.setProblem(problem);
        RetrofitClient.getInstance().getRequestApi().add(token,request).enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if(response.code()==200 && response.body()!=null){
                    sendRequestView.openOccupantRequestScreen();
                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {

            }
        });
        return request;
    }
}
