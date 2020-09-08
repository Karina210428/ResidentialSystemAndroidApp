package presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import api.RetrofitClient;
import view.UserView;
import model.SharedPreference;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresenter {

    private UserView userView;

    public UserPresenter(UserView userView){
        this.userView = userView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showUserInfo(Context context) throws IOException {
        RetrofitClient.getInstance().getAuthApi().getUser(SharedPreference.getInstance(context).getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code()==200 && response.body()!=null) {
                            User user = response.body();
                          //  userView.showUsername(user);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

}
