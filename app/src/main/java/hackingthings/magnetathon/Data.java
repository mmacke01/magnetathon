package hackingthings.magnetathon;

import com.google.android.gms.common.data.DataHolder;

/**
 * Created by Bailey on 6/24/2016.
 */
public class Data {
    private String SoftContactMessage;
    public String getMessage() {return SoftContactMessage;}
    public void setMessage(String mess) {SoftContactMessage = mess;}

    private String SoftContactName;
    public String getName() {return SoftContactName;}
    public void setName(String mess) {SoftContactName = mess;}

    private String SoftContactNumber;
    public String getNumber() {return SoftContactNumber;}
    public void setNumber(String mess) {SoftContactNumber = mess;}

    private static final Data holder = new Data();
    public static Data getInstance() {return holder;}
}
