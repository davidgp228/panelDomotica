package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class alarmaPIN extends Activity implements View.OnClickListener{

    /**
     * Variables de vista
     */
    EditText etpin, etpinconfirmar;


    /**
     * Variables de clase
     */
    Context context;
    bdCrearBaseDatos bd ;
    boolean claseForanea;
    String[] valores = {"1","3","5","10","15"};
    Spinner spTiempo;
    Button btnGuardar;
    JSONArray jsonarray;
    ws async;
    variablesGlobales vg;

    usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_pin);getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context= alarmaPIN.this;


        try {
            vg= new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        bd=new bdCrearBaseDatos(context);
        if(bd.getAlarma().moveToNext()){

            Log.d("BD", "alarma OK");
            Intent i= new Intent(this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
        else{
            Log.d("BD", "alarma empty");
        }


        btnGuardar= findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        etpin= findViewById(R.id.etPIN);
        etpinconfirmar = findViewById(R.id.etPINConfirmar);

        spTiempo= findViewById(R.id.spTiempo);

        spTiempo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_minutos, valores));

        spTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
               // Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGuardar:

                /**
                 * Cargar informacion poblar la base de datos
                 * 1. Obtener habitaciones
                 * 2. Obtener dispostivos
                 */

                if(etpin.getText().toString().equals("")|| etpinconfirmar.getText().toString().equals("")){
                    Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!etpin.getText().toString().equals(etpinconfirmar.getText().toString())){
                    Toast.makeText(this, "Pin no coincide", Toast.LENGTH_SHORT).show();
                    return;
                }


                    /**
                     * Guardar datos de alarma
                     */
                    bd.insertarAlarma(etpin.getText().toString(), spTiempo.getSelectedItem().toString());

                    initWs(context, false);

                break;

                default:

                    break;

        }

    }

    public void initWs(Context cn, boolean claseForanea){


        //**Para que no se inicie el activity
         this.claseForanea= claseForanea;

         //**Cuando se consulta de una clase foranea (Configuracion)
        context= cn;

        bd=new bdCrearBaseDatos(cn);

        async= new ws();

        //**Consultar la llave del contrato para realizar consultas
        /*Cursor cursor= bd.getUsuario();
        if(cursor.moveToNext()){
            vg.setContratoID(cursor);
            //usuario= new usuario(cursor.getInt(cursor.getColumnIndex(bdTablaUsuario.CONTRATOID)),
              //      cursor.getString(cursor.getColumnIndex(bdTablaUsuario.FECHAREGISTRO)));
        }*/



        //**Habitaciones
        async.setContext(cn);
        async.setOperacion("obtenerHabitaciones");
        async.setAlarmaPIN(alarmaPIN.this);
        async.execute();



    }

    public void cargarHabitaciones(String respuestaWS){
        try {

            if(!respuestaWS.equals("")){

                Log.d("Habitaciones", "Starting insert "+respuestaWS);

                jsonarray = new JSONArray(respuestaWS);

                //**Poblar base de datos tabla habitaciones
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    bd.insertarHabitaciones(jsonobject.getString("id_area"),jsonobject.getString("inmueble_fk"),
                            jsonobject.getString("area"),""+R.drawable.habitacionb, jsonobject.getString("num_focos"));
                }

            }

            Log.d("Habitaciones", "OK insert");

            //**Dispositivos
            async= new ws();
            async.setContext(context);
            async.setAlarmaPIN(alarmaPIN.this);
            async.setOperacion("obtenerDispositivos");
            async.execute();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargarDispositivos(String respuestaWS){

        try {

            if(!respuestaWS.equals("")){

                Log.d("Dispositivos", "Starting insert");

                jsonarray = new JSONArray(respuestaWS);

                //**Poblar base de datos tabla habitaciones
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    bd.insertarDispositivos(jsonobject.getString("id_dispositivo"),jsonobject.getString("area_fk"),
                            jsonobject.getString("nombre"),jsonobject.getString("mac"),
                            jsonobject.getString("nserie"),jsonobject.getString("alias"),
                            jsonobject.getString("estado"), jsonobject.getString("t_uso"),
                            jsonobject.getString("vida") , jsonobject.getString("fecha_creacion"));
                }

                Log.d("Dispositivos", "OK insert");
                //**Topicos
                async= new ws();
                async.setContext(context);
                async.setAlarmaPIN(alarmaPIN.this);
                async.setOperacion("obtenerTopicos");
                async.execute();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cargarTopicos(String respuestaWS){

        try {

            if(!respuestaWS.equals("")){

                jsonarray = new JSONArray(respuestaWS);

                //**Poblar base de datos tabla habitaciones
                for(int i=0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    bd.insertarTopicos(jsonobject.getString("id_topicos"),jsonobject.getString("topico"),
                            jsonobject.getString("fecha_creacion"), jsonobject.getString("dispositivos_fk"));

                }

                if(!claseForanea)
                {
                Intent i= new Intent(this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
