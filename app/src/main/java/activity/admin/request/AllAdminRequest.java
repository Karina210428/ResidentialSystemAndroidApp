package activity.admin.request;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.employee.EmployeeDetailRequestActivity;
import activity.employee.EmployeeProfileActivity;
import activity.employee.EmployeeRequestList;
import activity.employee.ListAllRequestActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import api.RetrofitClient;
import com.example.residentialsystemapp.R;
import model.Request;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;

public class AllAdminRequest extends AppCompatActivity {

    private ListView listRequest;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_all_request);
        listRequest = findViewById(R.id.employeeRequestList);
        intent = new Intent(this, AdminRequestDetail.class);
        setRequestList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setRequestList(){
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        RetrofitClient.getInstance().getRequestApi().getAllByType(token).enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if (response.isSuccessful()) {
                    List<Request> responseRequestList = response.body();
                    if(responseRequestList.size()!=0) {
                        setAdapter(responseRequestList);
                    }else {
                        Toast.makeText(getApplicationContext(), "Заявок пока нет", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAdapter(List<Request> responseRequestList){
        List<String> list = responseRequestList.stream().map(i-> i.getProblem()).collect(Collectors.toList());

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item,R.id.listItemProblem, list);
        listRequest.setAdapter(myAdapter);
        listRequest.setOnItemClickListener((parent, view, position, id) -> {
            int itemValue = position;
            intent.putExtra("request", responseRequestList.get(itemValue));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.allRequestAdmin:
                startActivity(new Intent(this, AllAdminRequest.class));
                finish();
                return true;
            case R.id.allEmployeeAdmin:
                startActivity(new Intent(this, EmployeeListActivity.class));
                finish();
                return true;
            case R.id.allOccupantAdmin:
                startActivity(new Intent(this, OccupantListActivity.class));
                finish();
                return true;
            case R.id.news:
                startActivity(new Intent(this, AllNewsActivity.class));
                finish();
                return true;
            case R.id.addNewsAdmin:
                startActivity(new Intent(this, WriteNewsActivity.class));
                finish();
                return true;
            case R.id.allEmployeeLocation:
                startActivity(new Intent(this, AllEmployeeOnMapActivity.class));
                finish();
                return true;
            case R.id.logout:
                SharedPreference.getInstance(getApplicationContext()).clearToken();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
