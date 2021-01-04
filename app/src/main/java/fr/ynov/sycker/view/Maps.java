package fr.ynov.sycker.view;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ynov.sycker.models.merchant.ApiMerchant;
import fr.ynov.sycker.models.merchant.Fields;
import fr.ynov.sycker.models.merchant.Records;

import fr.ynov.sycker.R;
import fr.ynov.sycker.utils.Constant;
import fr.ynov.sycker.utils.FastDialog;
import fr.ynov.sycker.utils.Network;

public class Maps extends FragmentActivity implements OnMapReadyCallback {
    private Fields merchant;
    private GoogleMap mMap;
    private List<Records> records;
    private Map<String, Fields> markers = new HashMap<String, Fields>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(getIntent().getExtras() != null) {
            merchant = (Fields) getIntent().getExtras().get("merchant");

            Log.e("merchant", merchant.getNom_du_commerce());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Fields fields = markers.get(marker.getId());
                Toast.makeText(Maps.this, "ID: " + marker.getId() + " - " + fields.getNom_du_commerce(), Toast.LENGTH_SHORT).show();
            }
        });
        getData(mMap);
    }

    private void getData(GoogleMap mMap) {
        if (!Network.isNetworkAvailable(Maps.this)) {
            FastDialog.showDialog(Maps.this, FastDialog.SIMPLE_DIALOG, getString(R.string.dialog_network_error));
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Log.e("volley", json);

                ApiMerchant api = new Gson().fromJson(json, ApiMerchant.class);

                if (records != null && records.size() > 0) {
                    for (int i = 0; i < records.size(); i++) {
                        Fields fields = records.get(i).getFields();
                        Marker marker = mMap.addMarker(
                                new MarkerOptions().position(
                                        new LatLng(
                                                fields.getGeo_point_2d()[0],
                                                fields.getGeo_point_2d()[1]
                                        )
                                ).title(fields.getNom_du_commerce()).snippet(fields.getCode_postal() + "/" + fields.getAdresse())
                        );
                        markers.put(marker.getId(), fields);

                        if (i == 0) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                    new LatLng(
                                            fields.getGeo_point_2d()[0],
                                            fields.getGeo_point_2d()[1]
                                    )
                            ));
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = new String(error.networkResponse.data);
                Log.e("volley", json);
            }
        });
        queue.add(stringRequest);
    }
}