package activity.admin.occupant;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.request.AllAdminRequest;
import activity.employee.EmployeeProfileActivity;
import activity.employee.EmployeeRequestList;
import activity.employee.ListAllRequestActivity;
import android.content.Intent;
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
import model.Occupant;
import model.SharedPreference;

public class OccupantDetailActivity extends AppCompatActivity {

    private TextView profileHeaderView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView mobileTextView;
    private TextView apartmentTextView;
    private Occupant occupant;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        nameTextView = findViewById(R.id.profileNameTextView);
        emailTextView = findViewById(R.id.profileEmailTextView);
        apartmentTextView = findViewById(R.id.profileApartmentTextView);
        mobileTextView = findViewById(R.id.profileMobileTextView);
        profileHeaderView = findViewById(R.id.profile_username);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            occupant = (Occupant) arguments.getSerializable("occupant");
        }
        setTextView(occupant);
    }

    private void setTextView(Occupant occupant){
        profileHeaderView.setText(occupant.getEmail());
        nameTextView.setText(occupant.getFullName());
        emailTextView.setText(occupant.getEmail());
        mobileTextView.setText(occupant.getPhone());
        apartmentTextView.setText(occupant.getApartment().getApartmentNumber());
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
