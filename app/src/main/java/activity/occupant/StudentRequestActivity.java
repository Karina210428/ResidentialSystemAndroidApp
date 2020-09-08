package activity.occupant;

import activity.AuthActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.NewsListAdapter;
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
import model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;
import java.util.stream.Collectors;

public class StudentRequestActivity extends AppCompatActivity{

    private ListView listRequest;
    private RequestAdapter adapter;

    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_request);
        listRequest = findViewById(R.id.requestList);
        intent = new Intent(this, RequestDetailActivity.class);
        setRequestList();
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
                Toast.makeText(StudentRequestActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addRequest:
                startActivity(new Intent(this, SendRequestActivity.class));
                finish();
                return true;
            case R.id.allMyRequest:
                startActivity(new Intent(this, StudentRequestActivity.class));
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
