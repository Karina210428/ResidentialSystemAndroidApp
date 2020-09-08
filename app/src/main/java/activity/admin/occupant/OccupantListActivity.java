package activity.admin.occupant;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.request.AllAdminRequest;
import activity.employee.ListAllRequestActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.residentialsystemapp.R;

import java.util.List;

import api.RetrofitClient;
import model.Occupant;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OccupantListActivity extends AppCompatActivity {

    private ListView lView;

    private OccupantListAdapter lAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_occupant_activity);
        lView = findViewById(R.id.androidList);

        setOccupantList();
    }

    private void setAdapter(List<Occupant> occupants){
        lAdapter = new OccupantListAdapter(this, occupants);
        lView.setAdapter(lAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setOccupantList(){
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();

        RetrofitClient.getInstance().getOccupantApi().getAll(token).enqueue(new Callback<List<Occupant>>() {
            @Override
            public void onResponse(Call<List<Occupant>> call, Response<List<Occupant>> response) {
                if (response.isSuccessful()) {
                    List<Occupant> responseList = response.body();
                    if(responseList.size()!=0) {
                        setAdapter(responseList);
                    }else {
                        Toast.makeText(getApplicationContext(), "Список пуст", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Occupant>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
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
