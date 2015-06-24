package nju.com.piece.database;

import android.content.Context;
import android.content.SharedPreferences;

import nju.com.piece.ApplicationStatic;

/**
 * Created by shen on 15/6/22.
 */
public class PreferenceHelper {
    private SharedPreferences sharedPreferences;

    private static PreferenceHelper helper_instance;

    public static PreferenceHelper instance(){
        if (helper_instance == null){
            helper_instance = new PreferenceHelper(ApplicationStatic.getAppContext().getSharedPreferences("public_args", Context.MODE_PRIVATE));
        }

        return helper_instance;
    }

    private PreferenceHelper(SharedPreferences preferences){
        sharedPreferences = preferences;
    }

    private final static String first_key="IF_FIRST";

    public boolean ifFirst(){
        return sharedPreferences.getBoolean(first_key,false);
    }

    public void setFirst(boolean if_first){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(first_key, if_first);
        prefsEditor.commit();
    }
}
