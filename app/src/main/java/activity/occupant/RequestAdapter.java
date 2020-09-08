package activity.occupant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.residentialsystemapp.R;
import model.Request;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class RequestAdapter extends BaseAdapter {

    private Context context;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList){
        this.context = context;
        this.requestList = requestList;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new RequestAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.request_list_item, parent, false);
            viewHolder.txtProblem = convertView.findViewById(R.id.txtRequestProblem);
            viewHolder.txtDate = convertView.findViewById(R.id.txtRequestDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RequestAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txtProblem.setText(requestList.get(position).getProblem());
        String date = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                        .withLocale(new Locale("ru"))
                        .format(requestList.get(position).getDateOfRequest());
        viewHolder.txtDate.setText(date);

        return convertView;
    }

    private static class ViewHolder {
        TextView txtProblem;
        TextView txtDate;
    }

}

