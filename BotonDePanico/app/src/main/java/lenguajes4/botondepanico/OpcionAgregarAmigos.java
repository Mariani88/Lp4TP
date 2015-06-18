package lenguajes4.botondepanico;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import dominio.Amigo;


public class OpcionAgregarAmigos extends Activity {

    private List<Amigo> amigos = new LinkedList<Amigo> ();
    private AdaptadorAmigos adaptador;
    private ListView listaAmigos;
    public static final int PICK_CONTACT_REQUEST = 1;
    private Uri contactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_agregar_amigos);

        this.cargarAmigos();

        this.adaptador = new AdaptadorAmigos(this, this.amigos);

        listaAmigos = (ListView)findViewById(R.id.listaDeAmigos);
        listaAmigos.setAdapter(adaptador);

        Button botonAgregarAmigos = (Button)findViewById(R.id.botonAgregarAmigos);


        botonAgregarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAmigo();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opcion_agregar_amigos, menu);
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


    public void cargarAmigo (){


         /*
        Crear un intent para seleccionar un contacto del dispositivo
         */
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        /*
        Iniciar la actividad esperando respuesta a través
        del canal PICK_CONTACT_REQUEST
         */
        startActivityForResult(i, PICK_CONTACT_REQUEST);
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


    private void renderContact(Uri uri) {

        Amigo amigo = new Amigo (getName(uri),getPhone(uri));


        if ( !amigos.contains(amigo) && amigo.getCelular() !=null && amigo.getNombre() !=null) {
            amigos.add(amigo);
            AdaptadorAmigos adaptador = new AdaptadorAmigos(this, amigos);
            listaAmigos.setAdapter(adaptador);
            this.guardarAmigos();
        }
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
        Cursor para recorrer los datos de la consulta
         */
        Cursor c = contentResolver.query(
                uri, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);

        /*
        Consultando el primer y único resultado elegido
         */
        if(c.moveToFirst()){
            name = c.getString(0);
        }

        /*
        Cerramos el cursor
         */
        c.close();

        return name;
    }


    private String getPhone(Uri uri) {
        /*
        Variables temporales para el id y el teléfono
         */
        String id = null;
        String phone = null;

        /************* PRIMERA CONSULTA ************/
        /*
        Obtener el _ID del contacto
         */
        Cursor contactCursor = getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID},
                null, null, null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        /*
        Sentencia WHERE para especificar que solo deseamos
        números de telefonía móvil
         */
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+"= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        /*
        Obtener el número telefónico
         */
        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                selectionArgs
                ,
                new String[]{id},
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }


    private List <Amigo> cargarAmigos (){

        List<Amigo> amigos = new LinkedList<Amigo>();

        File ruta = new File(Environment.getExternalStorageDirectory(), "Notes");
        if (!ruta.exists()) {
            ruta.mkdirs();
        }

        File archivoAmigos = new File (ruta, "amigos.dat");

        if ( !archivoAmigos.exists() ){
            this.guardarAmigos();
        }

        try{
            PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
            prefs.SavePreferences("ConfiguracionInicial", true); // Storing boolean - true/false
            ObjectInputStream ois = new ObjectInputStream( new FileInputStream(archivoAmigos) );
            this.amigos = (List<Amigo>)ois.readObject();
            ois.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch ( ClassNotFoundException e){
            e.printStackTrace();
        }

        return amigos;
    }


    private void guardarAmigos (){

        File ruta = new File(Environment.getExternalStorageDirectory(), "Notes");
        File archivoAmigos = new File (ruta, "amigos.dat");

        try{
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( archivoAmigos));
            oos.writeObject(this.amigos);
            oos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
