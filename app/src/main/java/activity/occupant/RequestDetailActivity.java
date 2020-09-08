package activity.occupant;

import activity.AuthActivity;
import activity.admin.news.AllNewsActivity;
import activity.employee.EmployeeDetailRequestActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import api.RetrofitClient;
import com.example.residentialsystemapp.R;
import model.Request;
import model.SharedPreference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;

public class RequestDetailActivity extends AppCompatActivity {

    private TextView textProblem;
    private TextView textDateOfRequest;
    private TextView textDateOfDeadline;
    private TextView textFullNameOccupant;
    private TextView textFullNameEmployee;
    private TextView textStatus;
    private Button buttonDeleteRequest;
    private Button buttonEditDoneRequest;
    private Button buttonRequestConfirm;
    private Request request;
    private TextView textConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        textDateOfDeadline = findViewById(R.id.dateOfDeadlineInfoTextView);
        textDateOfRequest = findViewById(R.id.dateOfRequestInfoTextView);
        textFullNameOccupant = findViewById(R.id.fullNameOccupantInfoTextView);
        textFullNameEmployee = findViewById(R.id.fullNameEmployeeInfoTextView);
        textProblem = findViewById(R.id.problemInfoTextView);
        textStatus = findViewById(R.id.statusInfoTextView);
        buttonDeleteRequest = findViewById(R.id.buttonDeleteRequest);
        buttonEditDoneRequest = findViewById(R.id.buttonEditDoneRequest);
        buttonRequestConfirm= findViewById(R.id.buttonRequestRegister);
        textConfirm = findViewById(R.id.confirmEmployeeInfoTextView);

        buttonDeleteRequest.setOnClickListener(v->showAlertDeleteRequest(v));
        buttonEditDoneRequest.setOnClickListener(v->showAlertEditRequest());
        buttonRequestConfirm.setOnClickListener(v->confirmRequest());

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            request = (Request) arguments.getSerializable("request");
        }
        setText(request);

    }

    private void openMyRequestScreen(Request request){
        Intent intent = new Intent(this, RequestDetailActivity.class);
        intent.putExtra("request", request);
        startActivity(intent);
        finish();
    }

    private void setText(Request request){
        String status = "";
        if(request.getDone()==null) status = "не выполнено";
        else status = request.getDone()?"выполнено":"не выполнено";

        String confirm = "";
        if(request.getConfirm()==null) confirm = "нет";
        else confirm = request.getConfirm()?"да":"нет";

        String dateOfDeadline = request.getDateOfDeadline() == null ? "": request.getDateOfDeadline().toString();
        String nameEmployee = request.getEmployee() == null ? "": request.getEmployee().getName();

        textDateOfDeadline.setText(dateOfDeadline);
        textDateOfRequest.setText(request.getDateOfRequest().toString());
        textFullNameEmployee.setText(nameEmployee);
        textConfirm.setText(confirm);
        textFullNameOccupant.setText(request.getOccupant().getFullName());
        textProblem.setText(request.getProblem());
        textStatus.setText(status);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void confirmRequest(){
        request.setConfirm(true);
        updateRequest(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateRequest(Request request1){
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        RetrofitClient.getInstance().getRequestApi().update(token, request1).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.code()==200 && response.body() != null) {
                    request = response.body();
                    Toast.makeText(getApplicationContext(), "ok!" , Toast.LENGTH_LONG).show();
                    openMyRequestScreen(request);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editRequest(String problem){
        request.setProblem(problem);
        request.setDateOfRequest(LocalDate.now());
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        Call<Request> call = RetrofitClient
                .getInstance().getRequestApi().update(token, request,request.getId());

        call.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.code()==200 && response.body() != null) {
                    request = response.body();
                    Toast.makeText(RequestDetailActivity.this, "Заявка изменена!" , Toast.LENGTH_LONG).show();
                   // startActivity(new Intent(getApplicationContext(), StudentRequestActivity.class));
                    openMyRequestScreen(request);
                    //finish();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Toast.makeText(RequestDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteRequest(){
        String token = SharedPreference.getInstance(getApplicationContext()).getToken();
        Call<ResponseBody> call = RetrofitClient
                .getInstance().getRequestApi().delete(token, request.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.body() != null) {
                    ResponseBody responseBody = response.body();
                    Toast.makeText(RequestDetailActivity.this, "Заявка удалена!" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), StudentRequestActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RequestDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlertEditRequest(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_request, null);

        final EditText editText = dialogView.findViewById(R.id.editTextDialogEditRequest);
        Button buttonDialogEditRequest = dialogView.findViewById(R.id.buttonDialogEditRequest);
        Button buttonDialogCancel = dialogView.findViewById(R.id.buttonDialogCancelRequest);

        buttonDialogCancel.setOnClickListener(view -> dialogBuilder.cancel());
        buttonDialogEditRequest.setOnClickListener(view -> {
            editRequest(editText.getText().toString().trim());
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showAlertDeleteRequest(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Подтвердить удаление...");

        alertDialog.setMessage("Вы уверены, что хотите отменить заявку?");

        alertDialog.setPositiveButton("Да", (dialog, which) -> {
            deleteRequest();
        });

        alertDialog.setNegativeButton("Нет", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
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
