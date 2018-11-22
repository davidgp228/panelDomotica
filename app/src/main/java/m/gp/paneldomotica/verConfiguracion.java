package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class verConfiguracion extends Activity implements View.OnClickListener{

    /**
     * Variables de vista
     */
    RelativeLayout rlprincipal;
    Context context;
    bdCrearBaseDatos bd = new bdCrearBaseDatos(this);
    variablesGlobales vg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_configuracion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context= verConfiguracion.this;

        try {
            vg = new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        rlprincipal= findViewById(R.id.rlPrincipal);

        ArrayList<View> botones= rlprincipal.getTouchables();

        for(View boton : botones)
            boton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.btnAtras:
                finish();
                break;
            case R.id.btnRed:
                i= new Intent(this, verRed.class);
                startActivity(i);
                break;
            case R.id.btnActualizarDatos:


                    bd.clear();

                    alarmaPIN alarmaPIN= new alarmaPIN();
                    alarmaPIN.initWs(context, true);

                    Toast.makeText(this, "Datos actualizados correctamente...", Toast.LENGTH_SHORT).show();

                break;
            default:

                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
