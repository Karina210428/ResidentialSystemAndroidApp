package activity.admin.employee;

import activity.AuthActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.gps.LocationService;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.admin.request.AllAdminRequest;
import activity.employee.EmployeeProfileActivity;
import activity.employee.EmployeeRequestList;
import activity.employee.ListAllRequestActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.residentialsystemapp.R;
import model.Employee;
import model.SharedPreference;

public class EmployeeDetailActivity extends AppCompatActivity {

    private TextView profileHeaderView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView specializationTextView;
    private TextView mobileTextView;
    private Employee employee;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_employee_screen);
        nameTextView = findViewById(R.id.profileNameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        specializationTextView = findViewById(R.id.profileSpecializationTextView);
        mobileTextView = findViewById(R.id.profileMobileTextView);
        profileHeaderView = findViewById(R.id.profile_username);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            employee = (Employee) arguments.getSerializable("employee");
        }
        setTextView(employee);
    }

    private void setTextView(Employee employee){
        profileHeaderView.setText(employee.getEmail());
        nameTextView.setText(employee.getFullName());
        emailTextView.setText(employee.getEmail());
        mobileTextView.setText(employee.getPhone());
        specializationTextView.setText(employee.getSpecialization().getSpecialization());
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
