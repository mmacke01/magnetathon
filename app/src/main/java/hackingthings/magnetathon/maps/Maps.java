package hackingthings.magnetathon.maps;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;

import hackingthings.magnetathon.HomeScreen;

/**
 * Created by jwichanski on 6/23/2016.
 */
public class Maps implements IMapsInterface
{
    private GoogleMap mMap;

    public Maps(GoogleMap map, final HomeScreen activity)
    {
        if (map == null)
            return;
    }

    public GregorianCalendar suggestedTime()
    {
        return null;
    }
}
