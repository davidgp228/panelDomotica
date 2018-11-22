package m.gp.paneldomotica;

import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by mac on 27/10/18.
 */

public class variablesGlobales {

    /**
     * lista de objetos de la aplicacion poblados desde la base de datos
     */
    public static List<habitacion> objHabitacion;
    public static List<dispositivo> objDispositivos;
    public static List<topico> objTopicos;

    static int contratoID=0;

    Typeface typeface;

    public variablesGlobales() throws MalformedURLException {}

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setObjHabitacion(List<habitacion> objHabitacion) {this.objHabitacion = objHabitacion;}

    public void setObjDispositivos(List<dispositivo> objDispositivos) {this.objDispositivos = objDispositivos;}

    public void setContratoID(Cursor cursor){
        this.contratoID= cursor.getInt(cursor.getColumnIndex(bdTablaUsuario.CONTRATOID));
    }

    public void setObjTopicos(List<topico> objTopicos) {variablesGlobales.objTopicos = objTopicos;}

    public List<habitacion> getObjHabitacion() {
        return objHabitacion;
    }

    public List<dispositivo> getObjDispositivos() {
        return objDispositivos;
    }

    public int getContratoID() {return contratoID;}

    public List<topico> getObjTopicos() {
        return objTopicos;
    }
}
