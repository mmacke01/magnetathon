package hackingthings.magnetathon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import hackingthings.magnetathon.alerts.exceptions.SoftAlertInformationMissingException;
import hackingthings.magnetathon.maps.Maps;

public class HomeScreen extends AppCompatActivity
{
    private static Maps _maps;
    private static final String[] LOCATION_PERMS =
        {
            Manifest.permission.ACCESS_FINE_LOCATION,
        };
    private static final int LOCATION_REQUEST = 1;
    private static final String[] SMS_PERMS =
            {
                    Manifest.permission.SEND_SMS,
            };

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
        if (checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(SMS_PERMS, LOCATION_REQUEST+1);
        }

        GoogleMap googleMap = null;
        try {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Map object that handles location requests and time estimates
        _maps = new Maps(googleMap);
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
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        String softContactNumber = settings.getString("SoftContactNumber", null);
        String softAlertMessage = settings.getString("SoftContactMessage", null);

        if (softContactNumber == null || softAlertMessage == null) {
            startActivity(new Intent(HomeScreen.this, Pop.class));
        }
        else{
            Intent intent = new Intent(this,softContact.class);
            startActivity(intent);
        }
    }
}
