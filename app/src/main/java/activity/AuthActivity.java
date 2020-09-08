package activity;

import activity.occupant.RegisterActivity;
import activity.occupant.UserActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.residentialsystemapp.R;

import activity.admin.employee.EmployeeListActivity;
import activity.employee.EmployeeProfileActivity;
import activity.employee.RegisterEmployeeActivity;
import view.LoginView;
import presenter.LoginPresenter;

public class AuthActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.login_progress);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        findViewById(R.id.email_sign_in_button).setOnClickListener(v -> login());
        findViewById(R.id.btn_link_signup_occupant).setOnClickListener(v -> openOccupantRegistrationScreen());
        findViewById(R.id.btn_link_signup_employee).setOnClickListener(v -> openEmployeeRegistrationScreen());
        loginPresenter = new LoginPresenter(this);
    }


    public void openOccupantRegistrationScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void openEmployeeRegistrationScreen() {
        startActivity(new Intent(this, RegisterEmployeeActivity.class));
        finish();
    }


    @Override
    public void showLoginError() {
        username.setError("Вы не ввели логин");
        username.findFocus();
    }

    @Override
    public void showPasswordError() {
        password.setError("Пароль введен не верно");
        password.findFocus();
    }

    @Override
    public void showLoginPasswordError() {
        username.setError("Вы не ввели логин");
        username.findFocus();
        password.setError("Пароль введен не верно");
        password.findFocus();
    }

    @Override
    public void openUserOccupantScreen(){
        startActivity(new Intent(this, UserActivity.class));
        finish();
    }

    @Override
    public void openOccupantListScreen() {
        startActivity(new Intent(this, EmployeeListActivity.class));
        finish();
    }

    @Override
    public void openUserEmployeeScreen(){
        startActivity(new Intent(this, EmployeeProfileActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void login() {
        loginPresenter.validateCredentials(username.getText().toString(), password.getText().toString(),getApplicationContext());
    }
}
