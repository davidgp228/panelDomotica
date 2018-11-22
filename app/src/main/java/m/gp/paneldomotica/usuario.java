package m.gp.paneldomotica;

/**
 * Created by mac on 20/11/18.
 */

public class usuario {

    private int idcontrato;
    private String fechaRegistro;

    public usuario(int idcontrato, String fechaRegistro){

        this.idcontrato= idcontrato;
        this.fechaRegistro= fechaRegistro;

    }

    public int getIdcontrato() {
        return idcontrato;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }
}
