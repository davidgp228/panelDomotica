package m.gp.paneldomotica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class login extends Activity implements View.OnClickListener {

    /**
     * MQTT variables
     */
    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://broker.mqttdashboard.com:1883";
    String clientId = MqttClient.generateClientId();

    /**
     *  Variables de vista
     */
    Button btnEntrar;
    TextView tvCaptcha;
    EditText etcontrasena, etusuario;

    /**
     * Variables de la clase
     */
    Context context;
    String usuario="",contrasena="";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    bdCrearBaseDatos bd = new bdCrearBaseDatos(this);
    variablesGlobales vg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context= login.this;


        /**
         * variables gloabales
         */
        try {
            vg= new variablesGlobales();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Crear base de datos
        try {
            bd.createDataBase();
            bd.openDataBase();

            cargarUsuario();
        }
        catch (IOException e){
            Toast.makeText(this,"Database failed::: "+e,Toast.LENGTH_SHORT).show();
        }


        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(this);

        tvCaptcha= findViewById(R.id.tvCaptcha);

        vg.setTypeface(Typeface.createFromAsset(getAssets(), "komikax.ttf"));
        tvCaptcha.setTypeface(vg.getTypeface());

        etusuario= findViewById(R.id.etUsuario);
        etcontrasena = findViewById(R.id.etContrasena);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnEntrar:

                if(etcontrasena.getText().toString().equals("")||etusuario.getText().toString().equals("")){
                    Toast.makeText(this, "Faltan ingresar datos", Toast.LENGTH_SHORT).show();

                }
                else {

                    usuario= etusuario.getText().toString();
                    contrasena= etcontrasena.getText().toString();
                    limpiarCampos();


                        ws async= new ws();

                        async.setContext(login.this);
                        async.setLogin(login.this);
                        async.setUsuario(usuario);
                        async.setContrasena(contrasena);
                        async.setOperacion("login");
                        async.execute();

                }

                break;
                default:

                    break;
        }

    }

    public void validar(String res){

        if(res.equals("0")){
            Toast.makeText(this, "Numero de contrato o clave incorrectos",Toast.LENGTH_SHORT).show();

        }
        else if(res.equals("-1")){
            Toast.makeText(this, "¡ Error servidor !",Toast.LENGTH_SHORT).show();
        }
        else if(res.equals("")){
            Toast.makeText(this, "¡ Error conexion null !",Toast.LENGTH_SHORT).show();
        }
        else if( Integer.parseInt(res)>0){

            /**
             * Cargar usuario
             */
            String currentDateandTime = sdf.format(new Date());
            bd.insertarUsuario(res, currentDateandTime);
            cargarUsuario();
            initActivity();
        }
        else {
            Toast.makeText(this, "¡ Error conexion !",Toast.LENGTH_SHORT).show();
        }
    }

    public void limpiarCampos(){
        etusuario.setText("");
        etcontrasena.setText("");
    }

    private void cargarUsuario(){

        Cursor cursor=bd.getUsuario();
        if(cursor.moveToNext()){

            vg.setContratoID(cursor);
            Log.d("BD", "Usuarios OK  ");
            initActivity();

        }
        else{
            Log.d("BD", "Usuarios empty");
        }

    }

    public void initActivity(){

        Intent i= new Intent(this, alarmaPIN.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
