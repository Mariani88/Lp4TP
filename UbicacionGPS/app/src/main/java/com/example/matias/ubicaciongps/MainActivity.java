package com.example.matias.ubicaciongps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private EditText longitud;
    private  EditText latitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botonLocalizar = (Button)findViewById(R.id.botonLocalizar);
        this.latitud = (EditText)findViewById(R.id.latitudField);
        this.longitud = (EditText)findViewById(R.id.longitudField);


        botonLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comenzarLocalizacion ();

            }
        });



    }

    private void comenzarLocalizacion() {

        LocationManager locManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        this.escribirPosicion(location);

        LocationListener locationer = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               escribirPosicion(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

                latitud.setText("cambio status");
            }

            @Override
            public void onProviderEnabled(String provider) {
                latitud.setText("proveedor habilitado");

            }

            @Override
            public void onProviderDisabled(String provider) {

                latitud.setText("GPS deshabilitado");
                longitud.setText("GPS deshabilitado");
            }
        };


        locManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 30000, 0, locationer);
    }


    private void escribirPosicion (Location location){

        if (location != null){
            latitud.setText(String.valueOf(location.getLatitude() ));
            longitud.setText(String.valueOf(location.getLongitude() ));
        }else{
            latitud.setText("buscando");
            longitud.setText("buscando");
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
