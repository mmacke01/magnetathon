package hackingthings.magnetathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Bailey on 6/23/2016.
 */
public class Pop extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.8));
    }
    public void yesClick(View view){
        Intent intent = new Intent(this,GoScreen.class);
        startActivity(intent);
    }
    public void contactClick(View view){
        Intent intent = new Intent(this,softContact.class);
        startActivity(intent);
    }
}
