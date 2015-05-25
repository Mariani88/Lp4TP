package lenguajes4.botondepanico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by Walter on 17/05/2015.
 */
public class DatosUsuarioActivity extends Activity {

    private EditText nombreUsuario;
    private EditText edadUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_usuario);
        nombreUsuario = (EditText)findViewById(R.id.user_data_nombre);
        edadUsuario = (EditText)findViewById(R.id.user_data_edad);
    }

    public void onClickGuardar(View v){
        String info_user = "Nombre=" + nombreUsuario.getText().toString() + "\n" + "Edad=" + edadUsuario.getText().toString();

        try{
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, "datosUsuario.txt");
            FileWriter escritor = new FileWriter(gpxfile);

            escritor.append(info_user);
            escritor.flush();
            escritor.close();

            Toast.makeText(getBaseContext(),getString(R.string.guardado), Toast.LENGTH_SHORT).show();


        }
        catch (IOException e ){
            e.getStackTrace();
        }
    }


}
