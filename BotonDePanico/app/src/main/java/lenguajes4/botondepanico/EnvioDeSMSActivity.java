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
import java.util.StringTokenizer;


//Para obtener contacto
import android.provider.ContactsContract.CommonDataKinds.Phone;

//Para leer archivo usuario


public class EnvioDeSMSActivity extends Activity {

    public static final int PICK_CONTACT_REQUEST = 1;
    private Uri contactUri;
    private String message;
    private String messageFalseAlarm;

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
            return false;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_de_sms);
        Bundle b = this.getIntent().getExtras();
        this.message =b.getString("MENSAJE DE ALERTA");
        this.messageFalseAlarm = b.getString("MENSAJE FALSA ALARMA");
        TextView messageBody = (TextView) findViewById(R.id.messageBody);
        messageBody.setText(this.message);
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
               message = this.message;//getString(R.string.mnsjAlerta)
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
                setContactoEnviado(contactUri);

            }
        }
    }
    public void setContactoEnviado(Uri uri){
        String nombre = getName(uri);
        String telefono = getPhone(uri);
        String info_user = "Nombre="+ nombre + "\n" + "Telefono="+ telefono;
        ArchivosDeDatos guardar = new ArchivosDeDatos();
        if (guardar.guardarDatos(info_user, getString(R.string.usuariosEnviados))) {
            Toast.makeText(this, getString(R.string.msjeContactoEnviadoGuardado), Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, getString(R.string.msjecontactoEnviadoNoGuardado), Toast.LENGTH_LONG).show();
        }
    }
    public void getContactoEnviado(View v){
        // Recuperamos el contacto al cual le enviamos
        String info_user ;
        ArchivosDeDatos guardar = new ArchivosDeDatos();

        // Si trajo los datos del archivo separo los datos
        if ((info_user=guardar.leerDatos(getString(R.string.usuariosEnviados)))!="") {
            dividirNombreYTelefono(info_user);

                Toast.makeText(this, getString(R.string.msjeContactoEnviadoGuardado), Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, getString(R.string.msjecontactoEnviadoNoGuardado), Toast.LENGTH_LONG).show();
        }

        // Seteamos el nuevo mensaje
        TextView messageBody = (TextView) findViewById(R.id.messageBody);
        messageBody.setText(this.messageFalseAlarm);
        // enviamos el mensaje
        sendMessage(messageBody);

    }
    public String dividirNombreYTelefono(String texto) {
        TextView contactName = (TextView) findViewById(R.id.contactName);
        TextView contactPhone = (TextView) findViewById(R.id.contactPhone);

        StringTokenizer st = new StringTokenizer(texto, "\n");
        String datos= "";
        while(st.hasMoreTokens()) {
            String dato = st.nextToken();
            StringTokenizer st2 = new StringTokenizer(dato, "=");
           while(st2.hasMoreTokens()) {
                String clave = st2.nextToken();
                String valor = st2.nextToken();
               datos = datos + "\n" + valor;
                /*
        Setear valores
         */
               if (clave.indexOf("Telefono")>=0){
                   contactPhone.setText(valor);
               }else if (clave.indexOf("Nombre")>=0) {
                   contactName.setText(valor);
               }

            }
        }
        return datos;
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

        } catch (IOException iox) { /* Manejo de errores */
            Toast.makeText(this, getString(R.string.mnsjSinFoto), Toast.LENGTH_LONG).show();
        }
        return photo;
    }

}
