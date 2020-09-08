package activity.admin.occupant;

import activity.admin.employee.EmployeeDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.residentialsystemapp.R;

import java.util.List;

import model.Occupant;

public class OccupantListAdapter extends BaseAdapter {

    private Context context;
    private List<Occupant> occupants;

    public OccupantListAdapter(Context context, List<Occupant> occupantList){
        this.context = context;
        this.occupants = occupantList;
    }

    @Override
    public int getCount() {
        return occupants.size();
    }

    @Override
    public Object getItem(int position) {
        return occupants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return occupants.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.occupant_list_single, parent, false);
            viewHolder.txtFullName = convertView.findViewById(R.id.occupantFullNameTextView);
            viewHolder.txtApartment = convertView.findViewById(R.id.occupantApartmentTextView);
            viewHolder.btn_info_occupant = convertView.findViewById(R.id.btn_info_occupant);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtFullName.setText(occupants.get(position).getFullName());
        viewHolder.txtApartment.setText("Комната: " + occupants.get(position).getApartment().getApartmentNumber());
        viewHolder.btn_info_occupant.setOnClickListener(view -> {
            Intent intent = new Intent(context, OccupantDetailActivity.class);
            intent.putExtra("occupant", occupants.get(position));
            context.startActivity(intent);
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView txtFullName;
        TextView txtApartment;
        ImageButton btn_info_occupant;
    }
}
