package com.example.humantechnologyproject.Bluetooth;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.example.humantechnologyproject.MainActivity;
import com.example.humantechnologyproject.db.DBHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public BTService() {
    }

    @Override
    public void onCreate() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranque) {
        //Toast.makeText(getBaseContext(), "Intentando conexion a: "+ address, Toast.LENGTH_SHORT).show();
        if (!MainActivity.bConectado) {

            //Get the MAC address from the DeviceListActivty via EXTRA
            if (intent != null) {
                address = intent.getStringExtra("btaddress");
                //Toast.makeText(getBaseContext(), "Arrancando address= " + address, Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getBaseContext(), "intent nulo", Toast.LENGTH_SHORT).show();
            }

            //create device and set the MAC address
            //Toast.makeText(getBaseContext(), "Intentando conexion a: "+ address, Toast.LENGTH_SHORT).show();
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_SHORT).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    MainActivity ma = new MainActivity();
                    ma.AskForPermissionBluetooth();
                }
                btSocket.connect();
                /**
                 * if(!MainActivity.bConectado) {
                 *                     MainActivity ma = new MainActivity();
                 *                     ma.AskForPermissionBluetooth();
                 *                 }
                 *                 else {
                 *                     btSocket.connect();
                 *                 }
                 */
                if (btSocket.isConnected()) {
                    Toast.makeText(getBaseContext(), "conectado", Toast.LENGTH_SHORT).show();
                } else return START_NOT_STICKY;
                MainActivity.bConectado = true;


            } catch (IOException e) {
                try {
                    btSocket.close();
                    MainActivity.bConectado = false;
                    if (!btSocket.isConnected()) {
                        MainActivity.bConectado = false;
                        //Toast.makeText(getBaseContext(), "2 Intentando conexion a: "+ address, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(), "exception: Socket cerrado", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e2) {
                    Toast.makeText(getBaseContext(), "Problemas: no se puede cerrar el socket", Toast.LENGTH_SHORT).show();
                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

        }
        return START_STICKY;
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
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            MainActivity ma = new MainActivity();
            ma.AskForPermissionBluetooth();
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

                    Intent miIntent = new Intent(getApplicationContext(), ShowBluetooth.class);
                    miIntent.setAction(Intent.ACTION_SCREEN_ON);
                    miIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    //miIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    miIntent.putExtra("letra", readMessage);
                    if(isDatosFromDataBase(readMessage)){

                        //getActivity().moveTaskToBack(false);
                        getApplicationContext().startActivity(miIntent);

                    }


                } catch (IOException e) {

                    break;
                }
            }
        }
        public boolean isDatosFromDataBase(String str) {
            DBHelper dbHelper = new DBHelper(BTService.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            boolean encontrado=false;
            Cursor fila = db.rawQuery("select _id,letra,uri,pic,color from acciones where letra='" + str + "'", null);
            if (fila.moveToFirst()) {
                encontrado = true;
            }
            int contador = fila.getCount();
            fila.close();
            db.close();
            return encontrado;
        }
    }
}