package com.example.humantechnologyproject.Bluetooth;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.humantechnologyproject.Bluetooth.ShowBluetooth;
import com.example.humantechnologyproject.Button;
import com.example.humantechnologyproject.MainActivity;
import com.example.humantechnologyproject.R;
import com.example.humantechnologyproject.db.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Servicio que lanza un <code>thread</code> para el envio y reception de <code>bluetooth</code>
 * @author JSLM
 * @version 1.0, 05/19/22
 */
public class BTService extends Service {
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private String direccionMAC;
    public List<String> letters = new ArrayList<>();

    public BTService() {
    }

    @Override
    public void onCreate() {
        Log.d("btservice", "onCreate");
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
        letters.add("A");
        letters.add("C");
        letters.add("E");
        letters.add("G");
        // Crear una notificación que abra la portada de la aplicación al tocarla
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Para versiones de Android posteriores a Oreo crear la notificación con un canal propio
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mi canal de notificación";
            String description = "Descripción de mi canal de notificación";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("mi_canal_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notification = new Notification.Builder(this, "mi_canal_id")
                    .setContentTitle("SerrAlertas está en segundo plano")
                    .setContentText("Pulsa para abrir la aplicación")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setContentTitle("Serralertas está en segundo plano")
                    .setContentText("Pulsa para abrir la aplicación")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
        }
        startForeground(1, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranque) {
        Log.d("mainactivity", "onStartCommand");
        if (intent != null) {
            direccionMAC = intent.getStringExtra("direccionMAC");
            Toast.makeText(this, "MAC: "+direccionMAC, Toast.LENGTH_SHORT).show();
        }
        BluetoothDevice dispositivo = btAdapter.getRemoteDevice(direccionMAC);
        try {
            btSocket = createBluetoothSocket(dispositivo);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "No conectado", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
            // Establish the Bluetooth socket connection.
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                }
                btSocket.connect();
                if (btSocket.isConnected()) {
                    Toast.makeText(getBaseContext(), "conectado", Toast.LENGTH_SHORT).show();
                } else return START_NOT_STICKY;//no volvera a iniciar
            } catch (IOException e) {
                e.printStackTrace();
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
        return START_STICKY;//volvera a iniciar el intent sin parametros adicionales
    }

    @Override
    public void onDestroy() {

        //this.stopSelf();
        mConnectedThread.stop();
        try {
            btSocket.close();

        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "Otros problemas: no se puede cerrar el socket", Toast.LENGTH_SHORT).show();
        }
        if (!btSocket.isConnected()) {
            MainActivity.bConectado = false;
            //Toast.makeText(getBaseContext(), "onDestroy: Socket cerrado", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getBaseContext(), "Service Destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();

    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (btAdapter.isEnabled()) {

            } else {
                Toast.makeText(getBaseContext(), "El dispositivo no está habilitado", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }



    //create new class for connect thread
    /**
     * Hilo que conecta un socket con el aparato externo para la recepcion bluetooth
     * tiene los atributos mmInStream y mmOutStream que son InputStream y OutputStream respectivamente y usados en la transmision de mensajes entre los dispositivos. En este proyecto solo se utiliza el atributo mmInStream ya que solo se necesitan los mensajes de entrada que son enviados por el dispositivo bluetooth externo.
     * @author JSLM
     * @version 1.0, 05/19/22
     */
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    int id = letters.indexOf(readMessage)+1;
                    Intent miIntent = new Intent(getApplicationContext(), ShowBluetooth.class);
                    miIntent.setAction(Intent.ACTION_SCREEN_ON);
                    miIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    //miIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    miIntent.putExtra("ID", id);
                    if(isDatosFromDataBase(id)){

                        //getActivity().moveTaskToBack(false);
                        getApplicationContext().startActivity(miIntent);

                    }


                } catch (IOException e) {

                    break;
                }
            }
        }
        public boolean isDatosFromDataBase(int id) {
            String TABLE_BUTTONS = "t_buttons";
            boolean found = false;
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Button button = null;
            Cursor cursorBotones;

            cursorBotones = db.rawQuery("SELECT * FROM " + TABLE_BUTTONS + " WHERE id = " + id + " LIMIT 1", null);

            if (cursorBotones.moveToFirst()) {
                found = true;
            }
            cursorBotones.close();
            db.close();
            return found;
        }
    }
}