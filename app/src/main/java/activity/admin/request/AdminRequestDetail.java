package activity.admin.request;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.gps.AllEmployeeOnMapActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.employee.ListAllRequestActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.residentialsystemapp.R;
import model.Employee;
import model.Request;
import model.SharedPreference;

public class AdminRequestDetail extends AppCompatActivity {

    private TextView textProblem;
    private TextView textDateOfRequest;
    private TextView textFullNameOccupant;
    private Button buttonGetAnswer;
    private Request request;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail_admin);
        textDateOfRequest = findViewById(R.id.dateOfRequestInfoTextViewAdmin);
        textFullNameOccupant = findViewById(R.id.fullNameOccupantInfoTextViewAdmin);
        textProblem = findViewById(R.id.problemInfoTextViewAdmin);
        buttonGetAnswer = findViewById(R.id.buttonGetAnswer);
        buttonGetAnswer.setOnClickListener(v-> openSentEmailActivity());

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            request = (Request) arguments.getSerializable("request");
        }
        setText(request);
    }

    private void setText(Request request){
        textDateOfRequest.setText(request.getDateOfRequest().toString());
        textFullNameOccupant.setText(request.getOccupant().getFullName());
        textProblem.setText(request.getProblem());
    }

    private void openSentEmailActivity(){
        Intent intent = new Intent(getApplicationContext(), SendEmailActivity.class);
        intent.putExtra("email", request.getOccupant().getEmail());
        startActivity(intent);
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
