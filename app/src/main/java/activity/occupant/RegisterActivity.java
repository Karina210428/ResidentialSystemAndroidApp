package activity.occupant;

import activity.AuthActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import api.RetrofitClient;
import com.example.residentialsystemapp.R;
import model.Apartment;
import model.Occupant;
import model.User;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUsername,
            editTextPassword,
            editTextSurname,
            editTextName,
            editTextPatronymic,
            editTextBirthday,
            editTextPhone,
            editTextApartmentNumber,
            editTextApartmentFloor,
            editTextApartmentBlock;
    private DatePickerDialog datePickerDialogBirthday;
    private SimpleDateFormat dateFormatter;
    
    private void findViewsById() {
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextBirthday.setInputType(InputType.TYPE_NULL);
        editTextBirthday.requestFocus();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPatronymic = findViewById(R.id.editTextPatronymic);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextApartmentBlock = findViewById(R.id.editTextApartmentBlock);
        editTextApartmentFloor = findViewById(R.id.editTextApartmentFloor);
        editTextApartmentNumber = findViewById(R.id.editTextApartmentNumber);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();

        setDateTimeField();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void userSignUp() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String patronymic = editTextPatronymic.getText().toString().trim();
        LocalDate birthday = LocalDate.parse(editTextBirthday.getText().toString().trim());
        String phone = editTextPhone.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError("Логин не введет");
            editTextUsername.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            editTextUsername.setError("Логин не валидный");
            editTextUsername.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.setError("Номер телефона не правильно введен");
            editTextPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Поле пароль не зполнено");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Пароль должен содержать не менее 6 символов");
            editTextPassword.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Имя не введено");
            editTextName.requestFocus();
            return;
        }

        Apartment apartment = new Apartment();
        apartment.setBlock(Integer.valueOf(editTextApartmentBlock.getText().toString()));
        apartment.setFloor(Integer.valueOf(editTextApartmentFloor.getText().toString()));
        apartment.setNumber(Integer.valueOf(editTextApartmentNumber.getText().toString()));

        Occupant occupant = new Occupant();
        occupant.setName(name);
        occupant.setPatronymic(patronymic);
        occupant.setSurname(surname);
        occupant.setBirthday(birthday);
        occupant.setPhone(phone);
        occupant.setEmail(username);
        occupant.setApartment(apartment);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setOccupant(occupant);

        Call<User> call = RetrofitClient
                .getInstance()
                .getAuthApi()
                .createUserOccupant(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==200 && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Регистрация завершена успешно!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, AuthActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextBirthday:
                datePickerDialogBirthday.show();
                break;
            case R.id.buttonSignUp:
                userSignUp();
                break;
            case R.id.buttonLogin:
                startActivity(new Intent(this, AuthActivity.class));
                break;
        }
    }
}
