package lenguajes4.botondepanico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botonAlertarContacto = (Button)findViewById(R.id.botonAlertarContacto);

        botonAlertarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this
                        , EnvioDeSMSActivity.class);
                startActivity(intent);
            }
        });

        Button botonAlertarAmigos = (Button)findViewById(R.id.botonAlertarAmigos);

        botonAlertarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Agregar logica para alertar amigos
            }
        });
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