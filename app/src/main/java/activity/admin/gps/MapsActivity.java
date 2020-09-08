package activity.admin.gps;

import activity.AuthActivity;
import activity.admin.employee.EmployeeListActivity;
import activity.admin.news.AllNewsActivity;
import activity.admin.news.WriteNewsActivity;
import activity.admin.occupant.OccupantListActivity;
import activity.admin.request.AllAdminRequest;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;

import com.example.residentialsystemapp.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import model.Request;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double latitude;
    private Double longitude;
    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            latitude = (Double) arguments.getSerializable("latitude");
            longitude = (Double) arguments.getSerializable("longitude");
            fullname = (String) arguments.getSerializable("fullname");
        }
    }

    private void showPosition(){
        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .zoom(20).target(latLng).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.addMarker(new MarkerOptions().position(latLng).title(fullname));
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showPosition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.allRequestAdmin:
                startActivity(new Intent(this, AllAdminRequest.class));
                finish();
                return true;
            case R.id.allEmployeeAdmin:
                startActivity(new Intent(this, EmployeeListActivity.class));
                finish();
                return true;
            case R.id.allOccupantAdmin:
                startActivity(new Intent(this, OccupantListActivity.class));
                finish();
                return true;
            case R.id.news:
                startActivity(new Intent(this, AllNewsActivity.class));
                finish();
                return true;
            case R.id.addNewsAdmin:
                startActivity(new Intent(this, WriteNewsActivity.class));
                finish();
                return true;
            case R.id.allEmployeeLocation:
                startActivity(new Intent(this, AllEmployeeOnMapActivity.class));
                finish();
                return true;
            case R.id.logout:
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
