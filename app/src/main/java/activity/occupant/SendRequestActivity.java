package activity.occupant;

import activity.AuthActivity;
import activity.admin.news.AllNewsActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import api.RetrofitClient;
import com.example.residentialsystemapp.R;
import com.google.android.material.textfield.TextInputLayout;

import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.SendRequestView;
import model.Specialization;
import presenter.SendRequestPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SendRequestActivity extends AppCompatActivity implements SendRequestView {

    private TextInputLayout textRequestProblem;
    private SendRequestPresenter sendRequestPresenter;
    private Spinner spinner;
    private Specialization specialization;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        textRequestProblem = findViewById(R.id.textRequestProblem);
        spinner = findViewById(R.id.typeArraySpinner);
        findViewById(R.id.buttonSendRequest).setOnClickListener(v->send());

        sendRequestPresenter = new SendRequestPresenter(this);

        setSpecializationListToSpinner();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void send(){
        sendRequestPresenter.sendRequest(textRequestProblem.getEditText().getText().toString(),
                specialization,
                this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSpecializationListToSpinner(){
        RetrofitClient.getInstance().getSpecializationApi().getAll().enqueue(new Callback<List<Specialization>>() {
            @Override
            public void onResponse(Call<List<Specialization>> call, Response<List<Specialization>> response) {
                if (response.isSuccessful()) {
                    List<Specialization> responseSpecializationList = response.body();
                    if(responseSpecializationList.size()!=0) {
                        setAdapterForSpinner(responseSpecializationList);
                    }else {
                        Toast.makeText(getApplicationContext(), "Cписок специализаций пуст", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Specialization>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showRequestError() {
        Toast.makeText(getApplicationContext(), "Вы не описали проблему.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void openOccupantRequestScreen() {
        startActivity(new Intent(getApplicationContext(), StudentRequestActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAdapterForSpinner(List<Specialization> specializationList){
        List<String> list = specializationList.stream().map(i-> i.getSpecialization()).collect(Collectors.toList());
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                specialization = specializationList.get(selectedItemPosition);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                specialization = specializationList.get(0);
            }
        });
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
