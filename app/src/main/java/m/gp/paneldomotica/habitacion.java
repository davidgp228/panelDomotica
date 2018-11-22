package m.gp.paneldomotica;

/**
 * Created by mac on 16/10/18.
 */

public class habitacion {

    private int id;
    private int fkInmueble;
    private int imagen;
    private String nombre;
    private int nFocos;

    public habitacion(int id,int fkInmueble,int imagen, String nombre, int nFocos){
        this.id = id;
        this.fkInmueble= fkInmueble;
        this.imagen = imagen;
        this.nombre = nombre;
        this.nFocos = nFocos;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {return nombre;}

    public int getnFocos() {return nFocos;}

    public int getId() {
        return id;
    }
}
