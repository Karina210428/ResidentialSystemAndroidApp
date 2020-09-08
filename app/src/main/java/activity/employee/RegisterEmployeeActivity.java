package activity.employee;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.residentialsystemapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import activity.AuthActivity;
import api.RetrofitClient;
import model.Employee;
import model.Request;
import model.SharedPreference;
import model.Specialization;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUsername,
            editTextPassword,
            editTextSurname,
            editTextName,
            editTextPatronymic,
            editTextBirthday,
            editTextPhone;
    private DatePickerDialog datePickerDialogBirthday;
    private SimpleDateFormat dateFormatter;
    private Spinner spinner;
    private Specialization specialization;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void findViewsById() {
        editTextBirthday = findViewById(R.id.editTextBirthdayEmployee);
        editTextBirthday.setInputType(InputType.TYPE_NULL);
        editTextBirthday.requestFocus();

        editTextUsername = findViewById(R.id.editTextUsernameEmployee);
        editTextPassword = findViewById(R.id.editTextPasswordEmployee);
        editTextName = findViewById(R.id.editTextNameEmployee);
        editTextSurname = findViewById(R.id.editTextSurnameEmployee);
        editTextPatronymic = findViewById(R.id.editTextPatronymicEmployee);
        editTextPhone = findViewById(R.id.editTextPhoneEmployee);
        spinner = findViewById(R.id.specializationArraySpinner);

        findViewById(R.id.buttonSignUpEmployee).setOnClickListener(this);
        findViewById(R.id.buttonLoginEmployee).setOnClickListener(this);


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
            }
        });
    }

    private void setDateTimeField() {
        editTextBirthday.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialogBirthday = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            editTextBirthday.setText(dateFormatter.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employee);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();
        setSpecializationListToSpinner();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void userSignUp() throws ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String patronymic = editTextPatronymic.getText().toString().trim();
        LocalDate birthday = LocalDate.parse(editTextBirthday.getText().toString().trim());
        String phone = editTextPhone.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            editTextUsername.setError("Enter a valid email");
            editTextUsername.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextUsername.setError("Enter a valid phone");
            editTextUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setPatronymic(patronymic);
        employee.setSurname(surname);
        employee.setPhone(phone);
        employee.setEmail(username);
        employee.setBirthday(birthday);
        employee.setSpecialization(specialization);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmployee(employee);

        Call<User> call = RetrofitClient.getInstance().getAuthApi().createUserEmployee(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==200 && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Регистрация завершена успешно!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextBirthdayEmployee:
                datePickerDialogBirthday.show();
                break;
            case R.id.buttonSignUpEmployee:
                try {
                    userSignUp();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.buttonLoginEmployee:
                startActivity(new Intent(this, AuthActivity.class));
                break;
        }
    }
}