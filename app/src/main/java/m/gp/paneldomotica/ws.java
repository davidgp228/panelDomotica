package m.gp.paneldomotica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by mac on 05/11/18.
 */

public class ws extends AsyncTask<String, Void, String> {

    private Context context;
    private String idCliente,usuario, contrasena, operacion;
    private ProgressDialog progressDialog;

    private LottieAnimationView ivAnimation;

    /**
     * clases
     */
    alarmaPIN alarmaPIN;
    verConfiguracion verConfiguracion;
    login login;
    MainActivity mainActivity;


    @Override
    protected void onPreExecute() {

        Log.d("onPreExecute", "OK");

        if(!operacion.equals("buscarActualizaciones"))
        {
            if(ivAnimation!=null)
                ivAnimation.setVisibility(View.VISIBLE);
       /* progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Validando...");
        progressDialog.setMessage("Un momento.");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();*/
        }

    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = null;
            HttpURLConnection conn = null;

            switch (operacion) {
                case "login":

                    url = new URL("http://192.168.1.123/domoticaRest/domoticaRest/restapi/loginPanel?usuario="+usuario+"&contrasena="+contrasena);

                    // crear conexion
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (conn.getResponseCode() == 200) {

                        InputStream responseBody = conn.getInputStream();

                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));

                        JSONObject jsonObject= new JSONObject(streamReader.readLine());

                        Log.d("Connection","Success");

                        return jsonObject.getString("login");

                    }
                    else {
                        // Error handling code goes here
                        Log.d("Connection","Error");
                    }

                    conn.disconnect();

                    break;
                case "obtenerHabitaciones":

                    url = new URL("http://192.168.1.123/domoticaRest/domoticaRest/restapi/habitacionesPanel?token="+variablesGlobales.contratoID);

                    Log.d("Habitaciones", "contrato "+variablesGlobales.contratoID);

                    // crear conexion
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (conn.getResponseCode() == 200) {

                        InputStream responseBody = conn.getInputStream();

                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));

                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = streamReader.readLine()) != null)
                            responseStrBuilder.append(inputStr);

                        //**Retorna el json en String
                        return responseStrBuilder.toString();

                    }
                    else {
                        // Error handling code goes here
                        Log.d("Connection","Error");
                    }

                    conn.disconnect();

                    break;
                case "obtenerDispositivos":


                    url = new URL("http://192.168.1.123/domoticaRest/domoticaRest/restapi/dispositivosPanel?token="+variablesGlobales.contratoID);


                    Log.d("Dispositivos", "contrato "+variablesGlobales.contratoID);
                    // crear conexion
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (conn.getResponseCode() == 200) {

                        InputStream responseBody = conn.getInputStream();

                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));

                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = streamReader.readLine()) != null)
                            responseStrBuilder.append(inputStr);

                        //**Retorna el json en String
                        return responseStrBuilder.toString();

                    }
                    else {
                        // Error handling code goes here
                        Log.d("Connection","Error dispositivo");
                    }

                    conn.disconnect();

                    break;

                case "obtenerTopicos":


                    url = new URL("http://192.168.1.123/domoticaRest/domoticaRest/restapi/topicosPanel?token="+variablesGlobales.contratoID);

                    Log.d("Topicos", "contrato "+variablesGlobales.contratoID);

                    // crear conexion
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (conn.getResponseCode() == 200) {

                        InputStream responseBody = conn.getInputStream();

                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));

                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = streamReader.readLine()) != null)
                            responseStrBuilder.append(inputStr);

                        //**Retorna el json en String
                        return responseStrBuilder.toString();

                    }
                    else {
                        // Error handling code goes here
                        Log.d("Connection","Error topicos");
                    }

                    conn.disconnect();

                    break;

                case "buscarActualizaciones":

                    url = new URL("http://192.168.1.123/domoticaRest/domoticaRest/restapi/actualizacionesPanel?token="+variablesGlobales.contratoID);

                    // crear conexion
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "my-rest-app-v0.1");

                    if (conn.getResponseCode() == 200) {

                        InputStream responseBody = conn.getInputStream();

                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));

                        JSONObject jsonObject= new JSONObject(streamReader.readLine());

                        Log.d("Connection","Success");

                        return jsonObject.getString("actualizacion");

                    }
                    else {
                        // Error handling code goes here
                        Log.d("Connection","Error");
                    }

                    conn.disconnect();

                    break;

                default:

                    break;
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {

       // if (progressDialog!=null)
        //    progressDialog.dismiss();

        if(ivAnimation!=null)
        ivAnimation.setVisibility(View.INVISIBLE);

            switch (operacion){
                case "login":
                    login.validar(result);
                    break;
                case "buscarActualizaciones":
                    mainActivity.resultadoActualizaciones(result);
                    break;
                case "obtenerHabitaciones":
                    Log.d("obtenerHabitaciones","Resultado "+result);
                    alarmaPIN.cargarHabitaciones(result);
                    break;
                case "obtenerDispositivos":
                    Log.d("obtenerDispositivos","Resultado "+result);
                    alarmaPIN.cargarDispositivos(result);
                    break;
                case "obtenerTopicos":
                    Log.d("obtenerTopicos","Resultado "+result);
                    alarmaPIN.cargarTopicos(result);
                    break;
                default:

                    break;
            }


    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setIvAnimation(LottieAnimationView ivAnimation) {this.ivAnimation = ivAnimation;}

    public void setAlarmaPIN(m.gp.paneldomotica.alarmaPIN alarmaPIN) {
        this.alarmaPIN = alarmaPIN;
    }

    public void setLogin(m.gp.paneldomotica.login login) {this.login = login;}

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /* StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);


                    JSONArray jsonarray = new JSONArray(responseStrBuilder.toString());

                    for(int i=0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String parametro       = jsonobject.getString("login");
                        Log.d("Valores ",">>"+parametro);
                    }*/

}
