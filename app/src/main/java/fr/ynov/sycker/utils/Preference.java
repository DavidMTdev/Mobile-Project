package fr.ynov.sycker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Preference {
    public static final String PREFERENCE_MERCHANT = "merchant";

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setMerchant(Context context, String merchant) {
        getPreference(context)
                .edit()
                .putString(PREFERENCE_MERCHANT, merchant)
                .apply(); // enregistrement des informations
    }

    // getCity()
    public static String getMerchant(Context context) {
        return getPreference(context).getString(PREFERENCE_MERCHANT, ""); // ""
    }


}
