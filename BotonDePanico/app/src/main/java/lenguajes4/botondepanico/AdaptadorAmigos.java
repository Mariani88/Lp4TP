package lenguajes4.botondepanico;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dominio.Amigo;
import lenguajes4.botondepanico.R;

import java.util.List;

/**
 * Created by matias on 07/06/15.
 */
class AdaptadorAmigos extends ArrayAdapter<Amigo> {

    private Activity contexto;
    private List<Amigo> amigos;

    public AdaptadorAmigos ( Activity contexto, List<Amigo> amigos){

        super(contexto, R.layout.item_amigos_layout, amigos);
        this.contexto = contexto;
        this.amigos = amigos;
    }


    public View getView(int position,View convertView,ViewGroup parent){

        LayoutInflater inflater = this.contexto.getLayoutInflater( );
        View item = inflater.inflate(R.layout.item_amigos_layout, null);

        TextView campoNombre = (TextView)item.findViewById(R.id.nombreAmigo);
        TextView campoTelefono = (TextView)item.findViewById(R.id.numeroDeCelular);

        campoNombre.setText(amigos.get(position).getNombre());
        campoTelefono.setText(String.valueOf(amigos.get(position).getCelular()));

        return item;
    }
}
