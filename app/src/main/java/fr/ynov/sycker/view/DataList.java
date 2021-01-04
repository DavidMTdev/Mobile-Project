package fr.ynov.sycker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
import fr.ynov.sycker.view.item.MerchantAdapter;

public class DataList extends AppActivity{

    private ListView listViewData;
    private Spinner spinner;

    private List<Records> records;
    private final String[]items = {"Artisanat d'art", "Restaurant ou traiteur", "Boucherie - charcuterie - rôtisserie"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        listViewData = findViewById(R.id.listViewData);
        spinner = findViewById(R.id.spinner);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DataList.this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        showDataList(queue, url);
    }

    public void showDataList(RequestQueue queue, String url){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        ApiMerchant api = new Gson().fromJson(json, ApiMerchant.class);

                        records = api.getRecords();
                        List<Fields> stringList = new ArrayList<>();

                        if (records != null && records.size() > 0) {


                            for (int i = 0; i < records.size(); i++) {
                                Fields fields = records.get(i).getFields();

                                stringList.add(fields);
                            }
                        }

                        listViewData.setAdapter(new MerchantAdapter(
                                DataList.this,
                                R.layout.item_merchant,
                                stringList)
                        );

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