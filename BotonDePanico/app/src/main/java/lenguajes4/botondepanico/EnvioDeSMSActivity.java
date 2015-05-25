package lenguajes4.botondepanico;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


//Para obtener contacto
import android.provider.ContactsContract.CommonDataKinds.Phone;

//Para leer archivo usuario
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EnvioDeSMSActivity extends Activity {

    public static final int PICK_CONTACT_REQUEST = 1;
    private Uri contactUri;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_envio_de_sms, menu);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_de_sms);
    }

    public void initPickContacts(View v) {
        /*
        Crear un intent para seleccionar un contacto del dispositivo
         */
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        /*
        Iniciar la actividad esperando respuesta a traves
        del canal PICK_CONTACT_REQUEST
         */
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    private void renderContact(Uri uri) {

        /*
        Obtener instancias de los Views
         */
        TextView contactName = (TextView) findViewById(R.id.contactName);
        TextView contactPhone = (TextView) findViewById(R.id.contactPhone);
        ImageView contactPic = (ImageView) findViewById(R.id.contactPic);

        /*
        Setear valores
         */
        contactName.setText(getName(uri));
        contactPhone.setText(getPhone(uri));

        contactPic.setImageBitmap(getPhoto(uri));
    }

    public void sendMessage(View v) {
        /*
        Guardo el mensaje que se encuentra grabado en el formulario del envioSMS
         */
        TextView messageBody = (TextView) findViewById(R.id.messageBody);
        String message = messageBody.getText().toString();
        /*
        Creando nuestro gestor de mensajes
         */
        SmsManager smsManager = SmsManager.getDefault();

        /*
        Enviando el mensaje
         */
        Toast.makeText(this, getString(R.string.enviandoMnsj), Toast.LENGTH_LONG).show();
        if (contactUri != null) {
            if (message == "") {
                message = getString(R.string.mnsjAlerta);
            }
            smsManager.sendTextMessage(
                    getPhone(contactUri),
                    null,
                    message,
                    null,
                    null);

            Toast.makeText(this, getString(R.string.mnsjEnviado), Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, getString(R.string.primeroSeleccioneUnContacto), Toast.LENGTH_LONG).show();


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                /*
                Capturar el valor de la Uri
                 */
                contactUri = intent.getData();
                /*
                Procesar la Uri
                 */
                renderContact(contactUri);
            }
        }
    }

    private String getPhone(Uri uri) {
        /*
        Variables temporales para el id y el telefono
         */
        String id = null;
        String phone = null;

        /************* PRIMERA CONSULTA ************/
        /*
        Obtener el _ID del contacto
         */
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{Contacts._ID},
                null,
                null,
                null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        /*
        Sentencia WHERE para especificar que solo deseamos
        numeros de telefonia movil
         */
        String selectionArgs =
                Phone.CONTACT_ID + " = ? AND " +
                        Phone.TYPE + "= " +
                        Phone.TYPE_MOBILE;

        /*
        Obtener el numero telefonico
         */
        Cursor phoneCursor = getContentResolver().query(
                Phone.CONTENT_URI,
                new String[]{Phone.NUMBER},
                selectionArgs,
                new String[]{id},
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }

    private String getName(Uri uri) {

        /*
        Valor a retornar
         */
        String name = null;

         /*
        Obtener una instancia del Content Resolver
         */
        ContentResolver contentResolver = getContentResolver();

        /*
        Consultar el nombre del contacto
         */
        Cursor c = contentResolver.query(
                uri,
                new String[]{Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if (c.moveToFirst()) {
            name = c.getString(0);
        }

        /*
        Cerramos el cursor
         */
        c.close();

        return name;
    }

    /**
     * private String getNombreUsuario{
     * FileInputStream archivoUsuario = new FileInputStream();
     * Pattern pat = Pattern.compile("Nombre=");
     * Matcher mat = pat.matcher(cadena);
     * if (mat.matches()) {
     * System.out.println("SI");
     * } else {
     * System.out.println("NO");
     * }
     * <p/>
     * }
     */
    private Bitmap getPhoto(Uri uri) {
        /*
        Foto del contacto y su id
         */
        Bitmap photo = null;
        String id = null;

        /************* CONSULTA ************/
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /*
        Usar el metodo de clase openContactPhotoInputStream()
         */
        try {
            Uri contactUri = ContentUris.withAppendedId(
                    Contacts.CONTENT_URI,
                    Long.parseLong(id));

            InputStream input = Contacts.openContactPhotoInputStream(
                    getContentResolver(),
                    contactUri);

            if (input != null) {
                /*
                Dar formato tipo Bitmap a los bytes del BLOB
                correspondiente a la foto
                 */
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) { /* Manejo de errores */ }
        Toast.makeText(this, "No se puede mostrar la foto del contacto", Toast.LENGTH_LONG).show();
        return photo;
    }

}
