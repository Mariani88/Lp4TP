package lenguajes4.botondepanico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Fernando.Ares on 17/06/2015.
 */
public class ConfiguracionInicialActivity extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_inicial);
    }


    public void onClickAgregarAmigosConfiguracionInicial(View v){

        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        prefs.SavePreferences("ConfiguracionInicial", true); // Storing boolean - true/false

        Intent intent = new Intent(ConfiguracionInicialActivity.this, OpcionAgregarAmigos.class);
        startActivity(intent);

        }

}


