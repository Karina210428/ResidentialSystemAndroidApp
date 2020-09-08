package activity.employee;

import activity.admin.employee.EmployeeListActivity;
import activity.admin.news.AllNewsActivity;
import activity.occupant.UserActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.residentialsystemapp.R;

import java.time.LocalDate;

import activity.AuthActivity;
import api.RetrofitClient;
import model.Employee;
import model.Request;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailRequestActivity extends AppCompatActivity {

    private TextView textProblem;
    private TextView textDateOfRequest;
    private TextView textDateOfDeadline;
    private TextView textFullNameOccupant;
    private TextView textFullNameEmployee;
    private TextView textStatus;
    private Button buttonDeleteRequest;
    private Button buttonDoneRequest;
    private Button buttonRequestRegister;
    private TextView textConfirm;
    private Request request;
    private Employee employee;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail_employee);
        textDateOfDeadline = findViewById(R.id.dateOfDeadlineInfoTextViewEmployee);
        textDateOfRequest = findViewById(R.id.dateOfRequestInfoTextViewEmployee);
        textFullNameOccupant = findViewById(R.id.fullNameOccupantInfoTextViewEmployee);
        textFullNameEmployee = findViewById(R.id.fullNameEmployeeInfoTextViewEmployee);
        textProblem = findViewById(R.id.problemInfoTextViewEmployee);
        textStatus = findViewById(R.id.statusInfoTextViewEmployee);
        buttonDeleteRequest = findViewById(R.id.buttonRequestDeleteEmployee);
        buttonDoneRequest = findViewById(R.id.buttonRequestDoneEmployee);
        buttonRequestRegister = findViewById(R.id.buttonRequestRegisterEmployee);
        buttonDeleteRequest.setOnClickListener(v->showAlertDeleteRequest(v));
        buttonDoneRequest.setOnClickListener(v->showAlertEditRequest());
        buttonRequestRegister.setOnClickListener(v->updateRequestEmployeeCall(request));
        textConfirm = findViewById(R.id.confirmEmployeeInfoTextView);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            request = (Request) arguments.getSerializable("request");
        }
        setText(request);
    }

    private void setText(Request request){
        String status = "";
        if(request.getDone()==null) status = "не выполнено";
        else status = request.getDone()?"выполнено":"не выполнено";
        String dateOfDeadline = request.getDateOfDeadline() == null ? "": request.getDateOfDeadline().toString();
        String nameEmployee = request.getEmployee() == null ? "": request.getEmployee().getFullName();

        String confirm = "";
        if(request.getConfirm()==null) confirm = "нет";
        else confirm = request.getConfirm()?"да":"нет";
        textConfirm.setText(confirm);
        textDateOfDeadline.setText(dateOfDeadline);
        textDateOfRequest.setText(request.getDateOfRequest().toString());
        textFullNameEmployee.setText(nameEmployee);
        textFullNameOccupant.setText(request.getOccupant().getFullName());
        textProblem.setText(request.getProblem());
        textStatus.setText(status);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doneRequest(){
        request.setDateOfDeadline(LocalDate.now());
        request.setDone(true);
        updateRequestEmployeeCall(request);
    }

    private void openMyRequestScreen(Request request){
        Intent intent = new Intent(this, EmployeeDetailRequestActivity.class);
        intent.putExtra("request", request);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRequestEmployeeCall(Request request1){
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        RetrofitClient.getInstance().getRequestApi().updateEmployee(token, request1,request1.getId()).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.code()==200 && response.body() != null) {
                    request = response.body();
                    Toast.makeText(getApplicationContext(), "ok!" , Toast.LENGTH_LONG).show();
                    openMyRequestScreen(request);
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRequestCall(Request request1){
        request.setOccupant(null);
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        RetrofitClient.getInstance().getRequestApi().update(token, request1,request1.getId()).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.code()==200 && response.body() != null) {
                    request = response.body();
                    Toast.makeText(getApplicationContext(), "ok!" , Toast.LENGTH_LONG).show();
                    openMyRequestScreen(request);
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRequestCall1(Request request){
        request.setOccupant(null);
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        RetrofitClient.getInstance().getRequestApi().update(token, request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.code()==200 && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "ok!" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), EmployeeListActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAlertEditRequest(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Выполнено...");

        alertDialog.setMessage("Подтвердить выполнение заявки?");

        alertDialog.setPositiveButton("Да", (dialog, which) -> {
            doneRequest();
        });

        alertDialog.setNegativeButton("Нет", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showAlertDeleteRequest(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Удаление из список ваших работ...");

        alertDialog.setMessage("Вы уверены, что хотите отменить заявку?");

        alertDialog.setPositiveButton("Да", (dialog, which) -> {
            updateRequestCall1(request);
        });

        alertDialog.setNegativeButton("Нет", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
