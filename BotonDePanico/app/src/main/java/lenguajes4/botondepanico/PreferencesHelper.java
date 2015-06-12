package lenguajes4.botondepanico;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fernando.Ares on 12/06/2015.
 */
public class PreferencesHelper {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("Registracion",0);
        this.editor = sharedPreferences.edit(); }

    public Boolean GetPreferences(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void SavePreferences(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }
}