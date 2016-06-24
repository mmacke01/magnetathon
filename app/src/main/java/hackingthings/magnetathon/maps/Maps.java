package hackingthings.magnetathon.maps;

import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.GregorianCalendar;

/**
 * Created by jwichanski on 6/23/2016.
 */
public class Maps implements IMapsInterface
{
    private GoogleMap mMap;

    public Maps(GoogleMap map)
    {
        if (map == null)
            return;

        mMap = map;
        try {
                mMap.setMyLocationEnabled(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mMap.setOnMapClickListener(
                new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng latLng)
                    {

                    }
                }
        );
    }

    public GregorianCalendar suggestedTime()
    {
        return null;
    }
}
