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
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.residentialsystemapp.R;
import model.SharedPreference;

public class SendEmailActivity extends AppCompatActivity{

    private EditText txtTo;
    private EditText txtSubject;
    private EditText txtMessage;
    private Button btn;
    private String email;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        txtSubject = findViewById(R.id.txtSubject);
        txtTo = findViewById(R.id.txtTo);
        txtMessage = findViewById(R.id.txtMessage);
        btn = findViewById(R.id.btnSendMessage);
        btn.setOnClickListener(v->send());

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            email = arguments.getString("email");
        }

        txtTo.setText(email);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void send(){
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{txtTo.getText().toString()});
        it.putExtra(Intent.EXTRA_SUBJECT,txtSubject.getText().toString());
        it.putExtra(Intent.EXTRA_TEXT,txtMessage.getText());
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it,"Choose Mail App"));
    }


    public void openAllRequestScreen() {
        startActivity(new Intent(getApplicationContext(), AllAdminRequest.class));
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
