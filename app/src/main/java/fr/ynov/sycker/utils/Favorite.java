package fr.ynov.sycker.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.ynov.sycker.models.merchant.Fields;


public class Favorite {

    public void saveData(Context context, Fields merchant) {
        ArrayList<Fields> listMerchant = loadData(context);
        if(listMerchant == null) {
            listMerchant = new ArrayList<Fields>();
        }
        listMerchant.add(merchant);

        Gson gson = new Gson();
        String json = gson.toJson(listMerchant);

        Preference.setMerchant(context, json);
    }

    public ArrayList<Fields> loadData(Context context){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Fields>>() {}.getType();
        ArrayList<Fields> obj = gson.fromJson(Preference.getMerchant(context), type);

        if (obj == null) {
            return new ArrayList<Fields>();
        }

        return obj;
    }

    public void deleteData(Context context, Fields merchant) {
        ArrayList<Fields> listMerchant = loadData(context);
        ArrayList<Fields> preference = new ArrayList<Fields>();
        for( Fields value : listMerchant ) {
            if (!value.getNom_du_commerce().equals(merchant.getNom_du_commerce())){
                preference.add(value);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(preference);

        Preference.setMerchant(context, json);
    }
}
