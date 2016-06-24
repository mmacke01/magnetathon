package hackingthings.magnetathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import hackingthings.magnetathon.alerts.Alerter;

public class softContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_contact);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_soft_contact, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_home:
                Intent intent = new Intent(this,HomeScreen.class);
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

    public void contactsClick(View view){
        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);
    }

    public void saveSoftContactMessage(View view){
        EditText mEdit;
        mEdit   = (EditText)findViewById(R.id.messageText);
        System.out.println(mEdit.getText().toString());

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("SoftContactMessage", mEdit.getText().toString());
        editor.commit();
    }
}
