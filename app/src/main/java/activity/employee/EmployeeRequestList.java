package activity.employee;

import activity.admin.news.AllNewsActivity;
import activity.occupant.RequestAdapter;
import activity.occupant.UserActivity;
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

import com.example.residentialsystemapp.R;

import java.util.List;
import java.util.stream.Collectors;

import activity.AuthActivity;
import api.RetrofitClient;
import model.Request;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRequestList extends AppCompatActivity {

    private ListView listRequest;
    private Intent intent;
    private RequestAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_all_request);
        listRequest = findViewById(R.id.employeeRequestList);
        intent = new Intent(this, EmployeeDetailRequestActivity.class);
        setRequestList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setRequestList(){
        int id = SharedPreference.getInstance(getApplicationContext()).getUserId();
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();

        RetrofitClient.getInstance().getRequestApi().getAllById(token, id).enqueue(new Callback<List<Request>>() {
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
    private void setAdapter(List<Request> requestList){
        adapter = new RequestAdapter(this, requestList);
        listRequest.setAdapter(adapter);
        listRequest.setOnItemClickListener((parent, view, position, id) -> {
            int itemValue = position;
            intent.putExtra("request", requestList.get(itemValue));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.allRequestEmployee:
                startActivity(new Intent(this, ListAllRequestActivity.class));
                finish();
                return true;
            case R.id.allMyRequestEmployee:
                startActivity(new Intent(this, EmployeeRequestList.class));
                finish();
                return true;
            case R.id.profile:
                startActivity(new Intent(this, UserActivity.class));
                finish();
                return true;
            case R.id.news:
                startActivity(new Intent(this, AllNewsActivity.class));
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
