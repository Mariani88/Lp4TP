package dominio;


import java.io.Serializable;

public class Amigo implements Serializable{

    private String nombre;
    private String celular;


    public Amigo ( String nombre, String celular){

        this.nombre = nombre;
        this.celular = celular;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getCelular(){
        return this.celular;
    }


    public boolean equals (Object obj){

        boolean iguales = this == obj;

        if (!iguales && obj!= null && obj instanceof Amigo ){

            Amigo obje = (Amigo)obj;

            if (obje.getNombre() != null && obje.getCelular()!=null){
                iguales = this.celular.equals( obje.getCelular() ) && this.nombre.equals( obje.getNombre() );
            }

        }

        return iguales;
    }

    public int hashCode (){
        return this.nombre.hashCode() + this.celular.hashCode();
    }
}





