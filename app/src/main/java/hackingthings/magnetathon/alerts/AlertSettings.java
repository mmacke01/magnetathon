package hackingthings.magnetathon.alerts;

/**
 * Created by mmackenzie on 6/23/2016.
 */
public class AlertSettings {

    private static String hardContactNumber = "2265808249";
    private String softContactNumber;
    private String softMessage;

    public static String getHardContactNumber() {
        return hardContactNumber;
    }

    public String getSoftContactNumber() {
        return softContactNumber;
    }

    public void setSoftContactNumber(String softContactNumber) {
        this.softContactNumber = softContactNumber;
    }

    public String getSoftMessage() {
        return softMessage;
    }

    public void setSoftMessage(String softMessage) {
        this.softMessage = softMessage;
    }
}
