package m.gp.paneldomotica;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class verDispositivos extends Activity implements View.OnClickListener{

    /**
     * MQTT variables
     */
    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://192.168.1.68:1883";
    String clientId = MqttClient.generateClientId();


    /**
     * Variables para recycler view
     */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    /**
     * Variables de vista
     */
    TextView tvNombre;
    ImageView ivHabitacion;
    Button btnAtras;
    variablesGlobales vg;

    /**
     * Variables de la clase
     */
    int idHabitacion,imagenHabitacion;
    String nombre, img;
    List<dispositivo> dispositivos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_dispositivos);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * Iniciamos variables globales
         */
        try {
            vg= new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Iniciar mosquitto server
        initMosquitto();

        //**Obtener parametros
        Intent myIntent = getIntent();
        nombre= myIntent.getStringExtra("nombre");
        imagenHabitacion = Integer.parseInt(myIntent.getStringExtra("imagen")) ;
        idHabitacion = Integer.parseInt(myIntent.getStringExtra("idHabitacion")) ;

        btnAtras= findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(this);

        ivHabitacion= findViewById(R.id.ivHabitacion);
        ivHabitacion.setImageResource(imagenHabitacion);

        // Obtener el Recycler
        recycler = findViewById(R.id.listaDispositivos);
        recycler.setHasFixedSize(true);

        //** Filtrar dispositivos
        dispositivos = new ArrayList<>();

        for(int i=0; i<vg.getObjDispositivos().size();i++){

            if(vg.getObjDispositivos().get(i).getFkHabitacion()==idHabitacion){
                dispositivos.add(vg.getObjDispositivos().get(i));
            }

        }

        // Usar un administrador para GridLayout
        //lManager = new LinearLayoutManager(this);
        //recycler.setLayoutManager(lManager);
        recycler.setLayoutManager(new GridLayoutManager(this, 4));

        // Crear un nuevo adaptador
        adapter = new adaptadorDispositivos(dispositivos);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(mqttAndroidClient==null){
                            return;
                        }



                        if(vg.getObjDispositivos().get(position).isStatus().equals("inactivo")){

                            //**Mandar mensaje a servidor
                            if(mqttAndroidClient.isConnected()) {

                                //** Cambiar estado del foco ON
                                RelativeLayout rlContenedor= view.findViewById(R.id.rlContenedor);
                                rlContenedor.setBackgroundResource(R.drawable.btnon);

                                publishMessage(vg.getObjDispositivos().get(position).getTopico(), "1");

                                Log.d("Topico vg ", "topico "+vg.getObjDispositivos().get(position).getTopico());
                                Log.d("Topico dispositivo ", "topico "+dispositivos.get(position).getTopico());

                                vg.getObjDispositivos().get(position).setStatus("activo");
                                vg.getObjDispositivos().get(position).setImagen(R.drawable.btnon);

                            }
                            else
                                Toast.makeText(verDispositivos.this,"¡Servidor desconectado!",Toast.LENGTH_SHORT).show();

                        }
                        else {

                            if(mqttAndroidClient.isConnected()) {

                                //** Cambiar estado del foco OFF

                                RelativeLayout rlContenedor= view.findViewById(R.id.rlContenedor);
                                rlContenedor.setBackgroundResource(R.drawable.btnoff);

                                publishMessage(vg.getObjDispositivos().get(position).getTopico(),"0");

                                vg.getObjDispositivos().get(position).setStatus("inactivo");
                                vg.getObjDispositivos().get(position).setImagen(R.drawable.btnoff);

                            }else
                                Toast.makeText(verDispositivos.this,"¡Servidor desconectado!",Toast.LENGTH_SHORT).show();


                        }

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        tvNombre= findViewById(R.id.tvNombreHabitacion);
        tvNombre.setText(nombre);
    }


    public void initMosquitto(){

        //**Iniciar conexion a servidor mosquitto
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    Log.d("MQTT>>>","Reconectado... "+serverUri);
                    // Because Clean Session is true, we need to re-subscribe
                } else {
                    Log.d("MQTT>>>","Conectado a: "+serverUri);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("MQTT>>>","Conexion perdida: "+serverUri);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("MQTT>>>","Mensaje entrante: "+ new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("MQTT>>>","¡Entrega completa! ");
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
        //**Finaliza conexion a mosquitto
    }

    public void publishMessage(String topicoRecived,String mensaje){

        String topico = topicoRecived;

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(mensaje.getBytes());
            mqttAndroidClient.publish(topico, message);

            if(!mqttAndroidClient.isConnected()){
                Log.d("MQTT>>>", mqttAndroidClient.getBufferedMessageCount() + " mensaje en buffer.");
            }

        } catch (MqttException e) {
            System.err.println("Error de publicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        try {

            mqttAndroidClient.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAtras:
                finish();
                break;
                default:

                    break;
        }
    }

}
