package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by mac on 21/11/18.
 */

public class alertDialog {



    public static void dialogoActualizacion(Activity activity){


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_actualizacion, null);

        builder.setView(v);
        builder.setCancelable(false);

        Button btnActualizar = v.findViewById(R.id.btnActualizar);
        Button btnCancelar= v.findViewById(R.id.btnCancelar);

        final AlertDialog ad = builder.show();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        btnActualizar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bdCrearBaseDatos bd = new bdCrearBaseDatos(activity);
                        bd.clear();

                        alarmaPIN alarmaPIN= new alarmaPIN();
                        alarmaPIN.initWs(activity, true);

                        Toast.makeText(activity, "Datos actualizados correctamente...", Toast.LENGTH_SHORT).show();

                        ad.dismiss();
                    }
                }
        );

        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                }
        );

    }

    public static void dialogoSpot(Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_spot, null);

        builder.setView(v);
        builder.setCancelable(false);

        final AlertDialog ad = builder.show();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


}
