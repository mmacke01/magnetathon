package hackingthings.magnetathon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import hackingthings.magnetathon.alerts.Alerter;

public class GoScreen extends AppCompatActivity {

    public int counter = 0;
    public Queue<Date> times = new LinkedList<Date>();

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST=333;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for(int i  = 0; i < 7; i++)
        {
            Date start = new Date(0000000000000);
            times.add(start);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_screen);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                }

                googleMap.setMyLocationEnabled(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void homeClick(View view){
        Intent intent = new Intent(this,HomeScreen.class);
        startActivity(intent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)){

            counter++;
            if(counter == 3){
                TextView t = (TextView) findViewById(R.id.timeRemainingText);
                t.setText("Soft Contact will now be alerted");
                Alerter alert = new Alerter(this);
                alert.sendHardAlert();
            }
            else if(counter == 5){
                TextView t = (TextView) findViewById(R.id.timeRemainingText);
                t.setText("Emergency Mode has been entered.");
                Button b = (Button) findViewById(R.id.homeButton);
                b.setText("I am safe");
            }

        }
        return true;
    }
}
