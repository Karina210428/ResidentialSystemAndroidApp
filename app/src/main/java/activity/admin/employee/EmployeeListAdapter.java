package activity.admin.employee;

import activity.admin.gps.LocationService;
import activity.admin.gps.MapsActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.residentialsystemapp.R;

import java.util.List;

import model.Employee;

public class EmployeeListAdapter extends BaseAdapter {

    private Context context;
    private List<Employee> employees;

    public EmployeeListAdapter(Context context, List<Employee> occupantList){
        this.context = context;
        this.employees = occupantList;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new EmployeeListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_employee, parent, false);
            viewHolder.txtFullName = convertView.findViewById(R.id.employeeFullNameTextView);
            viewHolder.txtApartment = convertView.findViewById(R.id.employeeSpecializationTextView);
            viewHolder.btn_location = convertView.findViewById(R.id.btn_location);
            viewHolder.btn_info = convertView.findViewById(R.id.btn_info);
            viewHolder.btn_call = convertView.findViewById(R.id.btn_call);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txtFullName.setText(employees.get(position).getFullName());
        viewHolder.txtApartment.setText("Должность: " + employees.get(position).getSpecialization().getSpecialization());
        viewHolder.btn_location.setOnClickListener(view -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("latitude", employees.get(position).getLatitude());
            intent.putExtra("longitude", employees.get(position).getLongitude());
            intent.putExtra("fullname", employees.get(position).getFullName());
            context.startActivity(intent);
        });
        viewHolder.btn_info.setOnClickListener(view -> {
            Intent intent = new Intent(context, EmployeeDetailActivity.class);
            intent.putExtra("employee", employees.get(position));
            context.startActivity(intent);
        });

        viewHolder.btn_call.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + employees.get(position).getPhone()));
            context.startActivity(callIntent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView txtFullName;
        TextView txtApartment;
        ImageButton btn_location;
        ImageButton btn_info;
        ImageButton btn_call;
    }
}
