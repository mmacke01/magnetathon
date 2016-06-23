package hackingthings.magnetathon.alerts.exceptions;

/**
 * Created by mmackenzie on 6/23/2016.
 */
public class SoftAlertInformationMissingException extends Exception {

    public SoftAlertInformationMissingException(String message) {
        super(message);
    }

    public SoftAlertInformationMissingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
