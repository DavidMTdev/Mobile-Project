package fr.ynov.sycker.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import fr.ynov.sycker.AppActivity;

import fr.ynov.sycker.MapsActivity;
import fr.ynov.sycker.R;
import fr.ynov.sycker.models.merchant.Fields;
import fr.ynov.sycker.utils.Favorite;
import fr.ynov.sycker.utils.Preference;

public class DataDetails extends AppActivity {
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private Fields merchant;
    private TextView title;
    private TextView type;
    private TextView adresse;
    private TextView postal;
    private Button telephone;
    private Button mail;
    private TextView description;
    private TextView services;
    private Button site;
    private Button buttonFavorite;
    private Button deleteFavori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);

        this.title = findViewById(R.id.title);
        this.type = findViewById(R.id.type);
        this.adresse = findViewById(R.id.adresse);
        this.postal = findViewById(R.id.postal);
        this.telephone = findViewById(R.id.telephone);
        this.mail = findViewById(R.id.mail);
        this.description = findViewById(R.id.description);
        this.services = findViewById(R.id.services);
        this.site = findViewById(R.id.site);
        this.buttonFavorite = findViewById(R.id.buttonFavori);
        this.deleteFavori = findViewById(R.id.buttonDelete);

        if(getIntent().getExtras() != null) {
            merchant = (Fields) getIntent().getExtras().get("merchant");

            this.title.setText(merchant.getNom_du_commerce());
            this.type.setText(merchant.getType_de_commerce());
            this.adresse.setText(merchant.getAdresse());
            this.postal.setText(merchant.getCode_postal());
            this.telephone.setText(merchant.getTelephone());
            this.mail.setText(merchant.getMail());
            this.description.setText(merchant.getDescription());
            this.services.setText(merchant.getServices());
            this.site.setText(merchant.getSite_internet());

            this.disableButtonFavorite();
        }
    }

    public void showGoogleMap(View view) {
        // Intent
        Intent intent = new Intent(DataDetails.this, MapsActivity.class);

        // passage de l'objet
        intent.putExtra("merchant", merchant);

        startActivity(intent);
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ merchant.getTelephone()));

        // check de la version Android >= API 23
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // M == API 23
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{ Manifest.permission.CALL_PHONE }, REQUEST_CODE_CALL_PHONE);

                return;
            }
        }

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_CALL_PHONE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.telephone.performClick();

            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void sendMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "objet");
        intent.putExtra(Intent.EXTRA_TEXT, "corps du mail");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ this.mail.getText().toString() });
        intent.putExtra(Intent.EXTRA_CC, new String[]{ "mathieu.masset@vivaneo.fr"});

        startActivity(Intent.createChooser(intent, "Partager par e-mail"));
    }

    public void showSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(this.site.getText().toString()));
        startActivity(intent);
    }

    public void addFavori(View view) {
        Favorite favorite = new Favorite();
        favorite.saveData(DataDetails.this, merchant);
        Toast.makeText(this, "Commerçant ajouté aux favoris", Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
    }

    public void disableButtonFavorite(){
        Favorite favorite = new Favorite();
        ArrayList<Fields> preference = favorite.loadData(DataDetails.this);
        for( Fields value : preference ) {
            if (value.getNom_du_commerce().equals(this.merchant.getNom_du_commerce())){
                this.buttonFavorite.setVisibility(View.GONE);
                this.deleteFavori.setVisibility(View.VISIBLE);
            }
        }
    }

    public void deleteFavori(View view) {
        Favorite favorite = new Favorite();
        favorite.deleteData(DataDetails.this, merchant);
        Toast.makeText(this, "Suppresion du favori", Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
    }
}
