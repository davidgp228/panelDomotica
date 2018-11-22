package m.gp.paneldomotica;

/**
 * Created by mac on 01/11/18.
 */

public class topico {

    int id;
    String topico;
    String fechacreacion;
    int fkDispostivo;

    public topico(int id,String topico,String fechacreacion, int fkDispostivo){

        this.id= id;
        this.topico= topico;
        this.fechacreacion=fechacreacion;
        this.fkDispostivo= fkDispostivo;


    }

    public int getFkDispostivo() {
        return fkDispostivo;
    }

    public String getTopico() {
        return topico;
    }

    public int getId() {return id;}

    public String getFechacreacion() {return fechacreacion;}

    public void setFkDispostivo(int fkDispostivo) {
        this.fkDispostivo = fkDispostivo;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

}
