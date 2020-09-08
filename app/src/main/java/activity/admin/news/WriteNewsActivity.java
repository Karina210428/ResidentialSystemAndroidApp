package activity.admin.news;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.admin.request.AllAdminRequest;
import activity.employee.ListAllRequestActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.residentialsystemapp.R;
import model.SharedPreference;
import view.NewsView;
import presenter.NewsPresenter;

import java.time.LocalDateTime;

public class WriteNewsActivity extends AppCompatActivity implements NewsView {

    private EditText txtTitle;
    private EditText txtText;
    private NewsPresenter newsPresenter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_news);
        txtText = findViewById(R.id.txtText);
        txtTitle = findViewById(R.id.txtTitle);
        findViewById(R.id.btnSendNews).setOnClickListener(v->send());

        newsPresenter = new NewsPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void send(){
        newsPresenter.sendNews(txtTitle.getText().toString(), txtText.getText().toString(), LocalDateTime.now(), this);
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(), "Вы ничего не написали.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void openAllNewsScreen() {
        startActivity(new Intent(getApplicationContext(), AllNewsActivity.class));
        finish();
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
