package hackingthings.magnetathon.alerts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

import hackingthings.magnetathon.alerts.exceptions.SoftAlertInformationMissingException;

/**
 * Created by mmackenzie on 6/23/2016.
 */
public class Alerter {

    private SharedPreferences settings;
    private SmsManager sms;

    public Alerter(Activity parentActivity) {

        settings = parentActivity.getPreferences(Context.MODE_PRIVATE);
        sms = SmsManager.getDefault();
    }

    public boolean sendSoftAlert() throws SoftAlertInformationMissingException {

        String softContactNumber = settings.getString("SoftContactNumber", null);
        String softAlertMessage = settings.getString("SoftContactMessage", null);

        if (softContactNumber == null || softAlertMessage == null) {

            throw new SoftAlertInformationMissingException("Contact Number or Message is missing. Contact Number: " + softContactNumber + ", Message: " + softAlertMessage);
        }

        try {

            sms.sendTextMessage(softContactNumber, null, softAlertMessage, null, null);
            return true;

        } catch (Exception e) { //send failed

            return false;
        }
    }

    public boolean sendHardAlert(String hardContactNumber) {

        String locationMessage = "coordinates will go here";

        try {

            sms.sendTextMessage(hardContactNumber, null, locationMessage, null, null);
            return true;

        } catch (Exception e) { //send failed

            return false;
        }
    }
}
