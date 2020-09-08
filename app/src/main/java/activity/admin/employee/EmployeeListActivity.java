package activity.admin.employee;

import activity.AuthActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.admin.request.AllAdminRequest;
import activity.employee.ListAllRequestActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import com.example.residentialsystemapp.R;

import java.util.List;

import api.RetrofitClient;
import model.Employee;
import model.SharedPreference;
import presenter.EmployeeListPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.EmployeeView;

public class EmployeeListActivity extends AppCompatActivity implements EmployeeView {

    private ListView lView;

    private EmployeeListAdapter lAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_occupant_activity);
        lView = findViewById(R.id.androidList);
        EmployeeListPresenter presenter = new EmployeeListPresenter(this);
        presenter.getEmployees(this);
        checkPermission();
        //setOccupantList();
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);

            return;
        }
    }

    @Override
    public void employeeReady(List<Employee> employeeList) {
        setAdapter(employeeList);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Список сотрудников пуст", 1000);
    }

    private void setAdapter(List<Employee> employees){
        lAdapter = new EmployeeListAdapter(this, employees);
        lView.setAdapter(lAdapter);
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
                //finish();
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
