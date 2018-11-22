package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    /**
     * MQTT variables
     */
    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://broker.mqttdashboard.com:1883";
    String clientId = MqttClient.generateClientId();

    /**
     * Varibles fecha
     */
    Calendar c = Calendar.getInstance();
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Controladores de vistas
     */
    Button btnAlarma, btnControl, btnConfigurar;
    TextView tvfecha;
    ImageView ivConexion, iv;
    RelativeLayout rlnotificacion;

    /**
     * Variables de clase
     */
    variablesGlobales vg;
    bdCrearBaseDatos bd = new bdCrearBaseDatos(this);
    Cursor cursor;

    /**
     * Objetos temporales
     */
    List<habitacion> objHabitacion = new ArrayList<>();
    List<dispositivo> objDispositivos = new ArrayList<>();
    List<topico> objTopicos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * Instanciar variable globales
         */
        try {
            vg= new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        //byte[] byteData = Base64.decode(data, Base64.DEFAULT);
        /*Bitmap photo=BitmapFactory.decodeByteArray( byteData, 0,
                byteData.length);
        iv= findViewById(R.id.iv);*/
        //iv.setImageBitmap(photo);

       // byte[] decodedString = Base64.decode("data:image/png;base64,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\n", Base64.URL_SAFE);
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        //BitmapDrawable ob = new BitmapDrawable(getResources(), decodedByte);

        //rlcontenedor.setBackgroundDrawable(ob);


        rlnotificacion= findViewById(R.id.rlNotificacion);

        ivConexion = findViewById(R.id.ivConexion);
        Animation firstAnimation = AnimationUtils.loadAnimation(this, R.anim.conexion);
        ivConexion.startAnimation(firstAnimation);

        btnAlarma = findViewById(R.id.btnAlarma);
        btnControl= findViewById(R.id.btnControl);
        btnConfigurar= findViewById(R.id.btnConfiguracion);

        btnAlarma.setOnClickListener(this);
        btnControl.setOnClickListener(this);
        btnConfigurar.setOnClickListener(this);

        tvfecha = findViewById(R.id.tvFecha);

        c.set(Calendar.HOUR, 17);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 2);

        tvfecha.setText(simpleDateFormat.format(c.getTime()));

    }



    @Override
    public void onClick(View v) {

        final Intent[] i = new Intent[1];

        switch (v.getId()){

            case R.id.btnAlarma:
                i[0] = new Intent(this, verAlarma.class);
                startActivity(i[0]);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            break;
            case R.id.btnControl:
                i[0] = new Intent(this, verHabitacion.class);
                startActivity(i[0]);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btnConfiguracion:

                final String[] m_Text = {""};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("PIN de seguridad");

                // Set up the input
                final EditText input = new EditText(this);
                //Centrar texto
                input.setGravity(Gravity.CENTER);
                //
                input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER );
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());

                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        m_Text[0] = input.getText().toString();

                        i[0] = new Intent(getApplicationContext(), verConfiguracion.class);
                        startActivity(i[0]);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                break;
            default:

                break;

        }

    }

    public void initObjetos(){

        if(vg.objHabitacion!=null) {

            vg.objHabitacion.clear();
            if(vg.objDispositivos!=null)
            vg.objDispositivos.clear();
            if(vg.objTopicos!=null)
            vg.objTopicos.clear();

            this.objDispositivos.clear();
            this.objHabitacion.clear();
            this.objTopicos.clear();
        }
        //**Usuario
        cursor=bd.getUsuario();
        if(vg.getContratoID()==0)
        {
            vg.setContratoID(cursor);
        }

        Log.d("initObjetos ","Loading...");
        //**Obtener habitaciones
        cursor= null;
        cursor= bd.getHabitaciones();
        while (cursor.moveToNext()){
            this.objHabitacion.add(new habitacion(cursor.getInt(cursor.getColumnIndex(bdTablaHabitaciones.IDHABITACION)),
                    cursor.getInt(cursor.getColumnIndex(bdTablaHabitaciones.FKINMUEBLE)),
                    R.drawable.habitacionb,
                    cursor.getString(cursor.getColumnIndex(bdTablaHabitaciones.NOMBRE)),
                    cursor.getInt(cursor.getColumnIndex(bdTablaHabitaciones.NFOCOS))));
        }
        vg.setObjHabitacion(new ArrayList<>(this.objHabitacion));
        Log.d("initObjetos ","Habitacion OK");

        //**Obtener Topicos
        cursor= null;
        cursor= bd.getTopico();
        while (cursor.moveToNext()){
          Log.d("Dato topico ",""+cursor.getInt(cursor.getColumnIndex(bdTablaTopicos.IDTOPICO))+" "+
                  cursor.getString(cursor.getColumnIndex(bdTablaTopicos.TOPICO))+" "+
                  cursor.getString(cursor.getColumnIndex(bdTablaTopicos.FECHACREACION))+" "+
                  cursor.getInt(cursor.getColumnIndex(bdTablaTopicos.FKDISPOSITIVO))
                  );
        }
        Log.d("initObjetos ","Topicos OK");

        //**Obtener dispositivos
        cursor= null;
        cursor= bd.getDispositivos();
        while (cursor.moveToNext()){

           Cursor topico= bd.getTopicoDispositivo(cursor.getInt(cursor.getColumnIndex(bdTablaDispositivos.IDDISPOSITIVO)));

           this.objDispositivos.add(new dispositivo(cursor.getInt(cursor.getColumnIndex(bdTablaDispositivos.IDDISPOSITIVO)),
                   cursor.getInt(cursor.getColumnIndex(bdTablaDispositivos.FKHABITACION)),
                   R.drawable.btnoff,
                   cursor.getString(cursor.getColumnIndex(bdTablaDispositivos.NOMBRE)),
                   topico.moveToNext()?topico.getString(topico.getColumnIndex(bdTablaTopicos.TOPICO)):"No topic",
                   cursor.getString(cursor.getColumnIndex(bdTablaDispositivos.ESTADO))
                   ));
        }
        vg.setObjDispositivos(new ArrayList<>(this.objDispositivos));

        Log.d("initObjetos ","Dispositivos OK");

        Log.d("initObjetos ","OK");

    }

    private void buscarActualizaciones(){
        //**Dispositivos
        ws async= new ws();
        async.setContext(MainActivity.this);
        async.setMainActivity(MainActivity.this);
        async.setOperacion("buscarActualizaciones");
        async.execute();
    }

    public void resultadoActualizaciones(String resultado){

        Log.d("Actualizaciones", "Result "+resultado);

        if(resultado.equals("1")){
            alertDialog.dialogoActualizacion(MainActivity.this);
        }
        else
        {
            //rlnotificacion.setVisibility(View.INVISIBLE  );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("On resume", "Nuevos objetos");
        //**Iniciar objetos
        initObjetos();
        //**Buscar actualziaciones
        buscarActualizaciones();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }
}


