package hackingthings.magnetathon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import hackingthings.magnetathon.alerts.Alerter;
import hackingthings.magnetathon.alerts.exceptions.SoftAlertInformationMissingException;

public class GoScreen extends AppCompatActivity {

    public long minutes = 0;
    public int counter = 0;
    private CountDownTimer timer;

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

        startTimer(300000);

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
    public void addTimeClick(View view){
        timer.cancel();
        TextView time = (TextView) findViewById(R.id.timeRemainingText);
        EditText added = (EditText) findViewById(R.id.timeInput);
        int timeR = Integer.parseInt(time.getText().toString());
        int timeA = Integer.parseInt(added.getText().toString());
        minutes = timeR + timeA;
        long milliseconds = minutes*60*1000;
        startTimer(milliseconds);
    }
    public void startTimer(long mill){
        timer = new CountDownTimer(mill, 1000) {
            @Override
            /**
             * Classifying what will happen after every tick of the timer, in this case every second
             * <p>
             *     Every second my textField is updated to show how much time (in seconds) is left before the countdown is done
             * </p>
             * @param  millisUntilFinished specifies how many milliseconds until the timer is done counting down
             */
            public void onTick(long millisUntilFinished) {
                long inSeconds = millisUntilFinished/1000;
                minutes = inSeconds/60;
                minutes += 1;
                String temp = Long.toString(minutes);
                TextView time = (TextView) findViewById(R.id.timeRemainingText);
                time.setText(temp);
            }

            @Override
            /**
             * Classifying what will happen after the timer has finished its countdown
             * <p>
             *     At the end of the timer countdown a random number is chosen.
             *     This random number is used to select a URL from the list
             *     This URL is then used to call the setRandomImage function
             *     It then restarts the timer
             * </p>
             */
            public void onFinish() {
                Context context = getApplicationContext();
                CharSequence text = "Please let us know you're home!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };
        timer.start();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            counter++;
            if(counter == 3){
                softAlert();
            }
            else if(counter == 5){
                hardAlert();
            }
            else if(counter == 1){
                reset();
            }

        }
        return true;
    }
    public void reset(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(counter<3){
                    counter = 0;
                }
            }
        }, 5000);
    }
    public void softAlert(){
        final Alerter alert = new Alerter(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(counter<7){
                    String data = Data.getInstance().getMessage();
                    TextView t = (TextView) findViewById(R.id.alertMessage);
                    t.setText("Soft Alert has been sent");
                    try {
                        alert.sendSoftAlert();
                    } catch (SoftAlertInformationMissingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 5000);
    }
    public void hardAlert(){
        final Alerter alert = new Alerter(this);
        TextView t = (TextView) findViewById(R.id.alertMessage);
        t.setText("Emergency Mode has been entered.");
        alert.sendHardAlert("hard alert");
        Button b = (Button) findViewById(R.id.homeButton);
        b.setText("I am safe");
    }
}
