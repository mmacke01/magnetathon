package hackingthings.magnetathon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hackingthings.magnetathon.maps.Maps;

public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback, GoogleMap.OnMapClickListener {
    private static Maps _maps;
    private static final String[] LOCATION_PERMS =
        {
            Manifest.permission.ACCESS_FINE_LOCATION,
        };
    private static final int LOCATION_REQUEST = 1;


    private GoogleMap googleMap;
    private LocationManager locationManager;
    private String serverKey = "AIzaSyBi7q5da5QF1vlNUsE5Hal9coT-WddVLz0";
    private LatLng camera;
    private LatLng origin;
    private LatLng destination;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Check if we have proper permissions, if not request them
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }

        try {
                ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        camera = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId())
        {
            case R.id.action_soft_contact:
                Intent intent = new Intent(this,softContact.class);
                startActivity(intent);
                return true;
            case R.id.action_faq:
                Intent intentF = new Intent(this,FAQ.class);
                startActivity(intentF);
                return true;
            default:
                return false;
        }
    }

    public void goClick(View view){
        Intent intent = new Intent(this,GoScreen.class);
        startActivity(intent);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody)
    {
        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin));
            googleMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            // direction.getRouteList().get(0).getLegList().get(0)
        }
    }

    @Override
    public void onDirectionFailure(Throwable t)
    {
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 13));

        this.googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Clear all markers and directions
        googleMap.clear();

        String serverKey = "AIzaSyBi7q5da5QF1vlNUsE5Hal9coT-WddVLz0";
        this.origin = new LatLng(latitude, longitude);
        this.destination = new LatLng(latLng.latitude, latLng.longitude);
        GoogleDirection.withServerKey(serverKey)
                .from(this.origin)
                .to(this.destination)
                .transportMode(TransportMode.WALKING)
                .execute(this);
    }
}
