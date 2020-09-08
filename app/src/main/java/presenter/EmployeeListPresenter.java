package presenter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import api.EmployeeApi;
import api.RetrofitClient;
import model.Employee;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.EmployeeView;

import java.util.List;

public class EmployeeListPresenter {

    private EmployeeView employeeView;
    private EmployeeApi employeeApi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public EmployeeListPresenter(EmployeeView view){
        this.employeeView = view;
        employeeApi = RetrofitClient.getInstance().getEmployeeApi();
    }

    public void getEmployees(Context context){
        String token = SharedPreference.getInstance(context).getToken();
        employeeApi.getAll(token).enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(response.code()==200 && response.body()!=null){
                    List<Employee> list = response.body();
                    employeeView.employeeReady(list);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                employeeView.showError();
            }
        });
    }
}
