package lenguajes4.botondepanico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends Activity {

    private TextView campoTipoDeUso;
    private String cuerpoDeAlerta;
    private String cuerpoFalsaAlarma;
    //Variable global para la configuracion inicial
    boolean configuracionInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        configuracionInicial = prefs.GetPreferences("ConfiguracionInicial");
        Intent intent;
        File ruta = new File(Environment.getExternalStorageDirectory(), "Notes");
        File archivoAmigos = new File (ruta, "amigos.dat");
        this.configuracionInicial = archivoAmigos.exists();

        if (!configuracionInicial ){

            setContentView(R.layout.activity_configuracion_inicial);
            intent = new Intent(MainActivity.this, ConfiguracionInicialActivity.class);
            startActivity(intent);

        }
        else{

            setContentView(R.layout.activity_main);

            this.campoTipoDeUso = (TextView) findViewById(R.id.campoTipoDeUso);
            Button botonAlertarContacto = (Button) findViewById(R.id.botonAlertarContacto);

            botonAlertarContacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this
                            , EnvioDeSMSActivity.class);

                    setearCuerpoDeAlerta();
                    setearCuerpoFalsaAlarma();

                    campoTipoDeUso.setVisibility(View.INVISIBLE);
                    Bundle b = new Bundle();
                    b.putString("MENSAJE DE ALERTA", cuerpoDeAlerta);
                    b.putString("MENSAJE FALSA ALARMA", cuerpoFalsaAlarma);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            Button botonAlertarAmigos = (Button) findViewById(R.id.botonAlertarAmigos);

            botonAlertarAmigos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Agregar logica para alertar amigos
                    campoTipoDeUso.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void setearCuerpoDeAlerta() {
        if (campoTipoDeUso.getVisibility() == View.VISIBLE){
            this.cuerpoDeAlerta = getString(R.string.textoAEnviarDePrueba);
        }else{
            this.cuerpoDeAlerta = getString(R.string.textoAEnviar);
        }
    }

    private void setearCuerpoFalsaAlarma(){
        if (campoTipoDeUso.getVisibility() == View.VISIBLE){
            this.cuerpoFalsaAlarma = getString(R.string.pruebaFalsaAlarma);

        }else{
            this.cuerpoFalsaAlarma = getString(R.string.falsaAlarma);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;

        switch (item.getItemId()) {
            case R.id.opcionAgregarAmigos:
                intent = new Intent(MainActivity.this, OpcionAgregarAmigos.class);
                startActivity(intent);
                return true;
            case R.id.probarAplicacion:
                campoTipoDeUso.setVisibility(View.VISIBLE);
                /*intent = new Intent(MainActivity.this, EnvioDeSMSActivity.class);
                startActivity(intent);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}