package fr.ynov.sycker;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import fr.ynov.sycker.models.merchant.Fields;
import fr.ynov.sycker.utils.FastDialog;
import fr.ynov.sycker.utils.Network;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Fields merchant;
    private GoogleMap mMap;
    private Map<String, Fields> markers = new HashMap<String, Fields>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (!Network.isNetworkAvailable(MapsActivity.this)) {
            FastDialog.showDialog(MapsActivity.this, FastDialog.SIMPLE_DIALOG, getString(R.string.dialog_network_error));
            return;
        }

        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Fields fields = markers.get(marker.getId());
                Toast.makeText(MapsActivity.this, "ID: " + marker.getId() + " - " + fields.getNom_du_commerce(), Toast.LENGTH_SHORT).show();
            }
        });

        getData(mMap);
    }

    private void getData(GoogleMap mMap) {

        merchant = (Fields) getIntent().getExtras().get("merchant");
        Marker marker = mMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(
                                merchant.getGeo_point_2d()[0],
                                merchant.getGeo_point_2d()[1]
                        )
                ).title(merchant.getNom_du_commerce()).snippet(merchant.getAdresse() + " " + merchant.getCode_postal())
        );
        markers.put(marker.getId(), merchant);


    }
}