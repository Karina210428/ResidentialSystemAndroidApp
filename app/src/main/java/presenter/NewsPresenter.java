package presenter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import api.RetrofitClient;
import view.NewsView;
import model.News;
import model.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.List;

public class NewsPresenter {

    private NewsView newsView;

    public NewsPresenter(NewsView newsView){
        this.newsView = newsView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNews(String title, String text, LocalDateTime date, Context context){
        new Handler().postDelayed(() -> {
            if (TextUtils.isEmpty(text)) {
                newsView.showError();
                return;
            }else {
                sendNewsToApi(title,text,date,context);
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNewsToApi(String title, String text, LocalDateTime date, Context context) {
        String token = SharedPreference.getInstance(context).getToken();
        News news = new News();
        news.setHeading(title);
        news.setText(text);
        news.setPublicationDate(date);
        RetrofitClient.getInstance().getNewsApi().add(token,news).enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if(response.code()==200 && response.body()!=null){
                    newsView.openAllNewsScreen();
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {

            }
        });
    }
}
