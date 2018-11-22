package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class verHabitacion extends Activity implements  View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Variables de vista
     */
    Button btnAtras;
    Switch swTema;
    RelativeLayout contenedor;
    variablesGlobales vg;

    /**
     * Variables de la clase
     */
    //Context context;

    /**
     * Variables para recycler view
     */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_habitacion);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * Variables globales
         */
        try {
             vg= new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        contenedor= findViewById(R.id.contenedor);

        btnAtras= findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(this);

        swTema= findViewById(R.id.swTema);

        swTema.setOnCheckedChangeListener(this);

        //*Temporal obtener informacion


        // Obtener el Recycler
        recycler = findViewById(R.id.listaHabitaciones);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        //lManager = new LinearLayoutManager(this);
        //recycler.setLayoutManager(lManager);

        // Usar un administrador para GridLayout
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        // Crear un nuevo adaptador
        adapter = new adaptadorHabitaciones(vg.getObjHabitacion());
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        Intent i= new Intent(getApplication(), verDispositivos.class);
                        i.putExtra("nombre", vg.getObjHabitacion().get(position).getNombre());
                        i.putExtra("imagen", ""+vg.getObjHabitacion().get(position).getImagen());
                        i.putExtra("idHabitacion", ""+vg.getObjHabitacion().get(position).getId());
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnAtras:
                finish();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){

            swTema.setText("Claro");
            // contenedor.setBackgroundColor(Color.YELLOW);
            contenedor.setBackgroundResource(R.drawable.fondob);

        }else {

            swTema.setText("Oscuro");
            //  contenedor.setBackgroundColor(Color.BLUE);
            contenedor.setBackgroundResource(R.drawable.fondo);
        }

    }
}
