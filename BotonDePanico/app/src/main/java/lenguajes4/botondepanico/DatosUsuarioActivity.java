package lenguajes4.botondepanico;
import android.os.Bundle;
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
        ArchivosDeDatos guardar = new ArchivosDeDatos();
        if (guardar.guardarDatos(info_user, getString(R.string.datosUsuario))) {
            Toast.makeText(getBaseContext(), getString(R.string.msjeGuardado), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(),getString(R.string.msjeNoGuardado), Toast.LENGTH_SHORT).show();
        }
    }


}
