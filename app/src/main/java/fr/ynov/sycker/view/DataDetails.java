package fr.ynov.sycker.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import fr.ynov.sycker.AppActivity;

import fr.ynov.sycker.R;
import fr.ynov.sycker.models.merchant.Fields;

public class DataDetails extends AppActivity {
    private Fields merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);

        if(getIntent().getExtras() != null) {
            merchant = (Fields) getIntent().getExtras().get("merchant");

            Log.e("merchant", merchant.getNom_du_commerce());
        }
    }

    public void showGoogleMap(View view) {
        // Intent
        Intent intent = new Intent(DataDetails.this, Maps.class);

        // passage de l'objet
        intent.putExtra("merchant", merchant);

        startActivity(intent);
    }
}
