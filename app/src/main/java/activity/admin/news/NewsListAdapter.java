package activity.admin.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.residentialsystemapp.R;
import model.News;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {

    private Context context;
    private List<News> newsList;

    public NewsListAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new NewsListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.news_list_item, parent, false);
            viewHolder.txtText = convertView.findViewById(R.id.news_text);
            viewHolder.txtTitle = convertView.findViewById(R.id.news_title);
            viewHolder.txtDate = convertView.findViewById(R.id.news_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsListAdapter.ViewHolder) convertView.getTag();
        }
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        viewHolder.txtTitle.setText(newsList.get(position).getHeading());
        viewHolder.txtText.setText(newsList.get(position).getText());
        viewHolder.txtDate.setText(dTF.format(newsList.get(position).getPublicationDate()));

        return convertView;
    }

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtText;
        TextView txtDate;
    }
}
