package activity.employee;

import activity.admin.news.AllNewsActivity;
import activity.occupant.UserActivity;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.residentialsystemapp.R;

import activity.AuthActivity;
import activity.admin.gps.LocationService;
import api.RetrofitClient;
import model.SharedPreference;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeProfileActivity extends AppCompatActivity {

    private TextView profileHeaderView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView specializationTextView;
    private TextView mobileTextView;

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
        showUsername();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, LocationService.class));
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, 200);
        }
    }

    private void setTextView(User user){
        profileHeaderView.setText(user.getUsername());
        nameTextView.setText(user.getEmployee().getFullName());
        emailTextView.setText(user.getUsername());
        mobileTextView.setText(user.getEmployee().getPhone());
        specializationTextView.setText(user.getEmployee().getSpecialization().getSpecialization());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showUsername() {
        RetrofitClient.getInstance().getAuthApi().getUser(SharedPreference.getInstance(getApplicationContext()).getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code()==200 && response.body()!=null) {
                            User user = response.body();
                            setTextView(user);
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
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
