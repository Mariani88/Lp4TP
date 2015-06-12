package lenguajes4.botondepanico;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    //Variable global para chequear que la registracion se haga al comienzo de la applicacion
    boolean usuarioRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        usuarioRegistrado = prefs.GetPreferences("UsuarioRegistrado");

        Intent intent;


        if (usuarioRegistrado==true) {

            setContentView(R.layout.activity_main);
            Button botonActivarAlarma = (Button) findViewById(R.id.botonActivarAlarma);
            botonActivarAlarma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProcesoDeActivacionActivity.class);
                    startActivity(intent);
                }
            });
        }

        else {

            setContentView(R.layout.activity_registro_datos_usuario);
            intent = new Intent(MainActivity.this, RegistracionActivity.class);
            startActivity(intent);


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
            case R.id.opcionModificarRegistroDeUsuario:
                intent = new Intent(MainActivity.this, RegistracionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}