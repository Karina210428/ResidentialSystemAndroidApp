package presenter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import api.RetrofitClient;
import view.LoginView;
import model.AuthenticationRequest;
import model.AuthenticationResponse;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter {

    private LoginView loginView;

    public LoginPresenter(LoginView loginView){
        this.loginView = loginView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validateCredentials(String username, String password, Context context) {
       if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
           loginView.showLoginPasswordError();
           return;
       }
        if (TextUtils.isEmpty(username)) {
            loginView.showLoginError();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginView.showPasswordError();
            return;
        }else {
            try {
                login(username, password, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(String username, String password, Context context) throws IOException {
        RetrofitClient.getInstance().getAuthApi()
                .signIn(new AuthenticationRequest(username, password))
                .enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if(response.code()==200 && response.body()!=null) {
                    SharedPreference sP = SharedPreference.getInstance(context);
                    sP.saveToken(response.body().getToken());
                    sP.saveUserId(response.body().getUser_id());
                    String role = response.body().getRole();
                    sP.saveRole(role);
                    if(role.equals("OCCUPANT")){
                        loginView.openUserOccupantScreen();
                    }else if(role.equals("EMPLOYEE")){
                        loginView.openUserEmployeeScreen();
                    }else {
                        loginView.openOccupantListScreen();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {

            }
        });
    }
}
