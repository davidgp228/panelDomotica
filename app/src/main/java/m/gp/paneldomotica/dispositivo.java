package m.gp.paneldomotica;

import java.util.List;

/**
 * Created by mac on 16/10/18.
 */

public class dispositivo {

    private int id;
    private int fkHabitacion;
    private int imagen;
    private String nombre;
    private String topico;
    //private List<topico> topicos;
    private String status;

    public dispositivo(int id, int fkHabitacion,int imagen, String nombre ,String topico, String status){
        this.id = id;
        this.fkHabitacion = fkHabitacion;
        this.imagen = imagen;
        this.nombre = nombre;
        this.topico= topico;
        this.status = status;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {return nombre;}

    public int getId() {return id;}

    public int getFkHabitacion() {return fkHabitacion;}

    public String getTopico() {return topico;}

    public String isStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public void setImagen(int imagen) {this.imagen = imagen;}
}
