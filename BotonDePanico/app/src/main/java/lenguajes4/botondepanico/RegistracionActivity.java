package lenguajes4.botondepanico;

/**
 * Created by Fernando.Ares on 08/06/2015.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegistracionActivity extends Activity {

    boolean usuarioRegistrado=false;

    // Progress Dialog
    private ProgressDialog pDialog;

    public JSONParser jsonParser = new JSONParser();
    EditText inputNombre;
    EditText inputApellido;
    EditText inputTelefono;
    EditText inputEdad;

    // url to create new user
    private static String url_create_user = "http://botondepanico.net63.net/android_connect/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_usuario);

        // Edit Text
        inputNombre = (EditText) findViewById(R.id.user_data_nombre);
        inputApellido = (EditText) findViewById(R.id.user_data_apellido);
        inputTelefono = (EditText) findViewById(R.id.user_data_telefono);
        inputEdad = (EditText) findViewById(R.id.user_data_edad);

       }

    public void onClickGuardar(View v){

        // Crea un usuario en background thread
        new CreateNewUser().execute();

    }

    /**
     * Background Async Task to Create new user
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistracionActivity.this);
            pDialog.setMessage("Guardando Usuario..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating user
         * */
        protected String doInBackground(String... args) {
            String nombre = inputNombre.getText().toString();
            String apellido = inputApellido.getText().toString();
            String telefono = inputTelefono.getText().toString();
            String edad = inputTelefono.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("apellido", apellido));
            params.add(new BasicNameValuePair("telefono", telefono));
            params.add(new BasicNameValuePair("edad", edad));


            // getting JSON Object
            // Note that create user url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,"POST",params);

            // check log cat fro response
            Log.d("Create Response", json.toString());


            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    //IMPORTANTE: Devolver mensaje de Registrado Exitosamente

                    //Setea el flag de registracion en true
                    PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
                    prefs.SavePreferences("UsuarioRegistrado", true); // Storing boolean - true/false

                    //Retorna al Main Activity
                    Intent intent = new Intent(RegistracionActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                } else {

                    // IMPORTANTE: Devolver mensaje de error

                    //Retorna al Main Activity
                    Intent intent = new Intent(RegistracionActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
