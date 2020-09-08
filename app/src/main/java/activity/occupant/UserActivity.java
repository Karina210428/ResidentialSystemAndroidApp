package activity.occupant;

import activity.AuthActivity;
import activity.admin.news.AllNewsActivity;
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

import activity.employee.ListAllRequestActivity;
import api.RetrofitClient;
import model.SharedPreference;
import model.User;
import presenter.UserPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity{

    private UserPresenter userPresenter;
    private TextView profileHeaderView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView apartmentTextView;
    private TextView mobileTextView;

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
        showUsername();
    }

    private void setTextView(User user){
        profileHeaderView.setText(user.getUsername());
        nameTextView.setText(user.getOccupant().getFullName());
        emailTextView.setText(user.getUsername());
        mobileTextView.setText(user.getOccupant().getPhone());
        apartmentTextView.setText(user.getOccupant().getApartment().getApartmentNumber());
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
        String role = SharedPreference.getInstance(getApplicationContext()).getRole();
        if(role.equals("OCCUPANT"))
            inflater.inflate(R.menu.student_menu, menu);
        else inflater.inflate(R.menu.employee_menu, menu);
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
