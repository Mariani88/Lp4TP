package lenguajes4.botondepanico;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ssethson on 26/05/2015.
 */
public class ArchivosDeDatos {
    public boolean guardarDatos(String info, String archivo) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, archivo );
            FileWriter escritor = new FileWriter(gpxfile);

            escritor.append(info);
            escritor.flush();
            escritor.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public String leerDatos(String archivo){
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            BufferedReader br = null;
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, archivo );
            String datos= "";
            String sCurrentLine;

            br = new BufferedReader(new FileReader(gpxfile));

            while ((sCurrentLine = br.readLine()) != null) {
               datos = datos + "\n" + sCurrentLine;
            }
            br.close();
            return datos;
        } catch (IOException e) {
            return "";
        }
    }
}
