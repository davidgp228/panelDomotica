package m.gp.paneldomotica;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class verRed extends Activity {
    /**
     * variables para listview
     */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    List<red> items;

    /**
     * variables para configurar red
     */
    WifiManager wifi;
    String wifis[]; //** Arreglo que almacena las redes obtenidas del sistema
    WifiScanReceiver wifiReciever;
    String ssid="",key="";

    /**
     * Variables de la clase
     */
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_red);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //*** Iniciamos el contexto de la actividad verRed
        context= getApplicationContext();

        // Obtener el Recycler
        recycler = findViewById(R.id.listaRedes);
        recycler.setHasFixedSize(true);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycler,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        ssid = items.get(position).getSsid();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(verRed.this);
                        alertDialog.setTitle("Nueva conexion");
                        alertDialog.setMessage("Conectarse a "+ ssid );

                        // Set up the input
                        final EditText input = new EditText(verRed.this);

                        //Centrar texto
                        input.setGravity(Gravity.CENTER);
                        input.setHint("Contraseña");

                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );

                        alertDialog.setView(input);

                        alertDialog.setNegativeButton("Conectar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // DO SOMETHING HERE

                                key = input.getText().toString();

                                if(key.equals(""))
                                {
                                    Toast.makeText(verRed.this,"Falta contraseña",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                WifiConfiguration wifiConfig = new WifiConfiguration();
                                wifiConfig.SSID = String.format("\"%s\"", ssid);
                                wifiConfig.preSharedKey = String.format("\"%s\"", key);

                                //remember id
                                int netId = wifi.addNetwork(wifiConfig);
                                wifi.disconnect();
                                wifi.enableNetwork(netId, true);
                                wifi.reconnect();

                                dialog.cancel();

                            }

                        });

                        alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog dialog = alertDialog.create();
                        dialog.show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        //Inicia escaneo de redes
        WifiManeger();
    }

    /**
     * Metodo que inicia el escaneo de redes, utilizando la clase WifiScanReceiver, que actualiza de forma
     * periodica las redes que va encontrando
     */
    public void WifiManeger(){

        wifi= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //Validar que wifi este activado
        if(wifi.isWifiEnabled()==false){
            Toast.makeText(context,"Wifi esta desactivado... Habilitando",Toast.LENGTH_SHORT).show();
            wifi.setWifiEnabled(true);
        }

        wifiReciever = new WifiScanReceiver();
        wifi.startScan();
        this.wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        context.registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    /**
     * Clase que implementa un BloadcatReceiver que permite que la aplicacion escuche cuando hay un cambio de red
     * y actualice la informacion en la lista de redes "items" que implementa una vista recycler view con card view
     */
    private class WifiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //**Iniciacion de las vistas
            items = new ArrayList();

            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];

            for(int i = 0; i < wifiScanList.size(); i++){
                String [] split=(wifiScanList.get(i)).toString().split(",");
                // wifis[i] = ((wifiScanList.get(i)).toString());
                wifis[i]=split[0].split(":")[1].trim();
                items.add(new red( split[0].split(":")[1].trim()));

            }

            // Usar un administrador para GridLayout
            recycler.setLayoutManager(new LinearLayoutManager(context));

            // Crear un nuevo adaptador
            adapter = new adaptadorRed(items);
            recycler.setAdapter(adapter);

        }
    }
}
