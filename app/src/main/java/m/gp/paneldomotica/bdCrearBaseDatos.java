package m.gp.paneldomotica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by mac on 07/11/18.
 */

public class bdCrearBaseDatos extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/m.gp.paneldomotica/databases/";
    private static String NOMBRE_BD="bddomot";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    //private static final Stringr     ;
    private static final int VERSION_BASE_DATOS=1;

    public bdCrearBaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context myContext) {
        super(context, name, factory, version);
        this.myContext = myContext;
    }
    bdCrearBaseDatos(Context context)
    {
        super(context, NOMBRE_BD, null, VERSION_BASE_DATOS);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE " + bdTablaUsuario.NOMBRETABLA+ " ( "
                + bdTablaUsuario.CONTRATOID+ " TEXT NOT NULL, "
                + bdTablaUsuario.FECHAREGISTRO + " TEXT NOT NULL );"
        );

        db.execSQL(" CREATE TABLE " + bdTablaHabitaciones.NOMBRETABLA+ " ( "
                + bdTablaHabitaciones.IDHABITACION+ " INTEGER PRIMARY KEY, "
                + bdTablaHabitaciones.FKINMUEBLE + " TEXT NOT NULL, "
                + bdTablaHabitaciones.NOMBRE + " TEXT NOT NULL, "
                + bdTablaHabitaciones.IMAGEN + " TEXT NOT NULL, "
                + bdTablaHabitaciones.NFOCOS + " TEXT NOT NULL );"
        );

        db.execSQL(" CREATE TABLE " + bdTablaAlarma.NOMBRETABLA+ " ( "
                + bdTablaAlarma.IDALARMA+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + bdTablaAlarma.PIN + " TEXT NOT NULL, "
                + bdTablaAlarma.TIEMPO + " TEXT NOT NULL );"
        );

        db.execSQL(" CREATE TABLE " + bdTablaDispositivos.NOMBRETABLA+ " ( "
                + bdTablaDispositivos.IDDISPOSITIVO+ " INTEGER PRIMARY KEY, "
                + bdTablaDispositivos.FKHABITACION + " TEXT NOT NULL, "
                + bdTablaDispositivos.NOMBRE + " TEXT NOT NULL, "
                + bdTablaDispositivos.MAC + " TEXT NOT NULL, "
                + bdTablaDispositivos.NUMEROSERIE + " TEXT NOT NULL, "
                + bdTablaDispositivos.ALIAS + " TEXT NOT NULL, "
                + bdTablaDispositivos.ESTADO + " TEXT NOT NULL, "
                + bdTablaDispositivos.TIEMPOUSO + " TEXT NOT NULL, "
                + bdTablaDispositivos.VIDA + " TEXT NOT NULL, "
                + bdTablaDispositivos.FECHACREACION + " TEXT NOT NULL );"
        );

        db.execSQL(" CREATE TABLE " + bdTablaTopicos.NOMBRETABLA+ " ( "
                + bdTablaTopicos.IDTOPICO+ " INTEGER PRIMARY KEY, "
                + bdTablaTopicos.TOPICO + " TEXT NOT NULL, "
                + bdTablaTopicos.FECHACREACION + " TEXT NOT NULL, "
                + bdTablaTopicos.FKDISPOSITIVO + " TEXT NOT NULL );"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // Si existe, no haemos nada!
            Log.d("Base Datos","Exist OK");
        } else {
            Log.d("Base Datos","Exist NOT");
            this.getReadableDatabase();

           /* try {
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copiando database");
            }*/
        }
    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + NOMBRE_BD;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            // Base de datos no creada todavia
        }

        if (checkDB != null) {

            checkDB.close();
        }

        return checkDB != null ? true : false;

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + NOMBRE_BD;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    ///---------------------

    public long insertarUsuario(String contratoid,String fecharegistro )
    {
        ContentValues cv = new ContentValues();

        cv.put(bdTablaUsuario.CONTRATOID,contratoid);
        cv.put(bdTablaUsuario.FECHAREGISTRO, fecharegistro);


        SQLiteDatabase sd =getWritableDatabase();
        long result = sd.insert(bdTablaUsuario.NOMBRETABLA,null,cv);

        return result;
    }

    public long insertarAlarma(String pin,String tiempo )
    {
        ContentValues cv = new ContentValues();

        cv.put(bdTablaAlarma.PIN,pin);
        cv.put(bdTablaAlarma.TIEMPO, tiempo);


        SQLiteDatabase sd =getWritableDatabase();
        long result = sd.insert(bdTablaAlarma.NOMBRETABLA,null,cv);

        return result;
    }

    public long insertarHabitaciones (String idhabitacion,String fkinmueble,String nombrearea,String imagen, String nfocos )
    {
        ContentValues cv = new ContentValues();

        cv.put(bdTablaHabitaciones.IDHABITACION,idhabitacion);
        cv.put(bdTablaHabitaciones.FKINMUEBLE, fkinmueble);
        cv.put(bdTablaHabitaciones.NOMBRE, nombrearea);
        cv.put(bdTablaHabitaciones.IMAGEN, imagen);
        cv.put(bdTablaHabitaciones.NFOCOS, nfocos);

        SQLiteDatabase sd =getWritableDatabase();
        long result = sd.insert(bdTablaHabitaciones.NOMBRETABLA,null,cv);

        return result;
    }

    public long insertarDispositivos (String iddispositivo,String fkhabitacion,String nombre,String mac, String numeroserie, String alias, String estado, String tiempouso, String vida, String fechacreacion )
    {
        ContentValues cv = new ContentValues();

        cv.put(bdTablaDispositivos.IDDISPOSITIVO,iddispositivo);
        cv.put(bdTablaDispositivos.FKHABITACION, fkhabitacion);
        cv.put(bdTablaDispositivos.NOMBRE, nombre);
        cv.put(bdTablaDispositivos.MAC, mac);
        cv.put(bdTablaDispositivos.NUMEROSERIE, numeroserie);
        cv.put(bdTablaDispositivos.ALIAS, alias);
        cv.put(bdTablaDispositivos.ESTADO, estado);
        cv.put(bdTablaDispositivos.TIEMPOUSO, tiempouso);
        cv.put(bdTablaDispositivos.VIDA, vida);
        cv.put(bdTablaDispositivos.FECHACREACION, fechacreacion);

        SQLiteDatabase sd =getWritableDatabase();
        long result = sd.insert(bdTablaDispositivos.NOMBRETABLA,null,cv);

        return result;
    }

    public long insertarTopicos (String idtopico,String topico,String fechacreacion,String fkdispositivo )
    {
        ContentValues cv = new ContentValues();

        cv.put(bdTablaTopicos.IDTOPICO,idtopico);
        cv.put(bdTablaTopicos.TOPICO, topico);
        cv.put(bdTablaTopicos.FECHACREACION, fechacreacion);
        cv.put(bdTablaTopicos.FKDISPOSITIVO, fkdispositivo);

        SQLiteDatabase sd =getWritableDatabase();
        long result = sd.insert(bdTablaTopicos.NOMBRETABLA,null,cv);

        return result;
    }

    //**Consultas
    public Cursor getUsuario(){
        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaUsuario.NOMBRETABLA ,null);

        return c;
    }

    public Cursor getAlarma(){
        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaAlarma.NOMBRETABLA ,null);

        return c;
    }

    public Cursor getTopicoDispositivo(int fkDispositivo){

        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaTopicos.NOMBRETABLA+ " WHERE "+ bdTablaTopicos.FKDISPOSITIVO+"="+ fkDispositivo ,null);

        return c;
    }

    public Cursor getHabitaciones(){
        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaHabitaciones.NOMBRETABLA ,null);

        return c;
    }

    public Cursor getDispositivos(){
        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaDispositivos.NOMBRETABLA ,null);

        return c;
    }

    public Cursor getTopico(){
        SQLiteDatabase sd = getWritableDatabase();

        Cursor c=null;

        c= sd.rawQuery("select * from "+ bdTablaTopicos.NOMBRETABLA ,null);

        return c;
    }

    //**Delete
    public void clear(){
        SQLiteDatabase sd =getWritableDatabase();
        Cursor c=null;

        sd.delete(bdTablaHabitaciones.NOMBRETABLA ,"", null);
        Log.d("Delete ", "Habitaciones ok ");

        sd.delete(bdTablaDispositivos.NOMBRETABLA ,"", null);
        Log.d("Delete ", "dispositivos ok");

        sd.delete(bdTablaTopicos.NOMBRETABLA ,"", null);
        Log.d("Delete ", "topicos ok");
    }
}
