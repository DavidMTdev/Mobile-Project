package fr.ynov.sycker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.ynov.sycker.AppActivity;

import fr.ynov.sycker.R;
import fr.ynov.sycker.models.merchant.ApiMerchant;
import fr.ynov.sycker.models.merchant.Fields;
import fr.ynov.sycker.models.merchant.Records;
import fr.ynov.sycker.utils.Constant;
import fr.ynov.sycker.utils.FastDialog;
import fr.ynov.sycker.utils.Network;

public class DataList extends AppActivity {

    private ListView listViewData;

    private List<Records> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        listViewData = findViewById(R.id.listViewData);

        if (!Network.isNetworkAvailable(DataList.this)) {
            FastDialog.showDialog(
                    DataList.this,
                    FastDialog.SIMPLE_DIALOG,
                    getString(R.string.dialog_network_error)
            );
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        ApiMerchant api = new Gson().fromJson(json, ApiMerchant.class);

                        records = api.getRecords();
                        List<String> stringList = new ArrayList<>();

                        if (records != null && records.size() > 0) {


                            for (int i = 0; i < records.size(); i++) {
                                Fields fields = records.get(i).getFields();

                                stringList.add(fields.getNom_du_commerce());
                            }
                        }

                        // Adapter = recyclage des items d'une ListView
                        ArrayAdapter adapter = new ArrayAdapter<>(
                                DataList.this,
                                android.R.layout.simple_list_item_1, // template
                                stringList
                        );

                        listViewData.setAdapter(adapter);

                        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Fields merchant = records.get(position).getFields();

                                // Intent
                                Intent intent = new Intent(DataList.this, DataDetails.class);

                                // passage de l'objet
                                intent.putExtra("merchant", merchant);

                                startActivity(intent);
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = new String(error.networkResponse.data);

                Log.e("volley", json);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}