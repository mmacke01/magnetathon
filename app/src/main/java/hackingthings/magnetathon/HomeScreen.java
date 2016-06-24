package hackingthings.magnetathon;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hackingthings.magnetathon.alerts.exceptions.SoftAlertInformationMissingException;
import hackingthings.magnetathon.maps.Maps;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback, GoogleMap.OnMapClickListener
{
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] SMS_PERMS =
    {
            Manifest.permission.SEND_SMS,
    };
    private static final int INITIAL_REQUEST=333;
    private static final int REQUEST =INITIAL_REQUEST+3;

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
            requestPermissions(LOCATION_PERMS, REQUEST);
        }

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(CONTACTS_PERMS, REQUEST + 1);
        }

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(SMS_PERMS, REQUEST + 2);
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

        Data storedData = Data.getInstance();
        try {
            FileInputStream fis = openFileInput("AlertData");
            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = fis.read()) != -1) {
                builder.append((char) c);
            }
            String[] data = builder.toString().split("\n");
            storedData.setName(data[0]);
            storedData.setNumber(data[1]);
            storedData.setMessage(data[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        String number = Data.getInstance().getNumber();
        String mess = Data.getInstance().getMessage();

        if (number == null || mess == null) {
            startActivity(new Intent(HomeScreen.this, Pop.class));
        }
        else{
            Intent intent = new Intent(this,GoScreen.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody)
    {
        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin));
            googleMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            String temp = direction.getRouteList().get(0).getLegList().get(0).getDuration().getValue();
            System.out.print(temp);
            Data.getInstance().setTime(temp);
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
        this.googleMap.setMyLocationEnabled(true);
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
