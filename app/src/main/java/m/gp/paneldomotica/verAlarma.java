package m.gp.paneldomotica;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.util.ArrayList;

public class verAlarma extends Activity implements View.OnClickListener{

    TableLayout tableLayout;

    ImageView ivEstadoAlarma;
    Button btnAtras;

    int imagenes[]={R.drawable.estadouno, R.drawable.estadosdos, R.drawable.estadotres, R.drawable.estadocuatro};
    int indiceImagen=0;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_ver_alarma);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tableLayout= findViewById(R.id.tablePanel);

        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(this);

        ivEstadoAlarma = findViewById(R.id.ivEstadoAlarma);
        ivEstadoAlarma.setOnClickListener(this);

        cargarImagen();

        ArrayList<View> botones = tableLayout.getTouchables();

        mp = MediaPlayer.create(this, R.raw.clica);

        for(View boton : botones)
        {
            boton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivEstadoAlarma:

                cargarImagen();

                break;
            case R.id.btnAtras:
                finish();
                break;
            default:
                mp.start();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void cargarImagen(){

        if(indiceImagen<imagenes.length-1)
            indiceImagen++;
        else
            indiceImagen=0;

        ivEstadoAlarma.setImageResource(imagenes[indiceImagen]);

    }
}
