package hackingthings.magnetathon.alerts;

import android.provider.Telephony;
import android.telephony.SmsManager;

/**
 * Created by mmackenzie on 6/23/2016.
 */
public class Alerter {

    private AlertSettings settings;
    private SmsManager sms;

    public Alerter(AlertSettings settings) {

        this.settings = settings;
        sms = SmsManager.getDefault();
    }

    public boolean sendSoftAlert() {

        try {

            sms.sendTextMessage(settings.getSoftContactNumber(), null, settings.getSoftMessage(), null, null);
            return true;

        } catch (Exception e) { //send failed

            return false;
        }
    }

    public boolean sendHardAlert() {

        String locationMessage = "coordinates will go here";

        try {

            sms.sendTextMessage(settings.getHardContactNumber(), null, locationMessage, null, null);
            return true;

        } catch (Exception e) { //send failed

            return false;
        }
    }
}
