package com.example.humantechnologyproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.humantechnologyproject.databinding.ActivityMainBinding;
import com.example.humantechnologyproject.db.DBButtons;
import com.example.humantechnologyproject.db.DBHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    String addressMAC = "98:D3:61:F6:82:98";
    public static boolean bConnected = false;
    private static final int PERMISSION_BLUETOOTH = 0;
    private static final int PERMISSION_BLUETOOTH_ADMIN = 0;
    private static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ListView list = findViewById(R.id.lista);
        DBButtons dbButtons = new DBButtons(MainActivity.this);
        Adapter adapter = new Adapter(this, dbButtons.buttonList());
        list.setAdapter(adapter);

        /*
            Click listener for every element of the list,
            takes you to the edit activity (same as create)
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                DBButtons dbButtons = new DBButtons(MainActivity.this);
                List<Button> buttons = dbButtons.buttonList();
                if (pos < buttons.size()) {
                    Button button = buttons.get(pos);
                    int id = button.getId();
                    Intent intent = new Intent(MainActivity.this, ButtonSettings.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
            }
        });
        setSupportActionBar(binding.toolbar);
    }

    /*
        Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
        Toolbar options:
        Add = Takes you to the button creation activity
        Bluetooth = if it's not active, it asks for permissions and activates it
        Info = Opens info activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, ButtonSettings.class);
            intent.putExtra("ID", 0);
            startActivity(intent);
        }
        if (id == R.id.action_bluetooth) {

            //AskForPermissionBluetooth();
            Log.d("mainactivity", "BtnPulsado");
            Intent BTService = new Intent(getApplicationContext(), com.example.humantechnologyproject.Bluetooth.BTService.class);
            BTService.putExtra("addressMAC", addressMAC);
            startService(BTService);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        ListView list = (ListView) findViewById(R.id.lista);
        DBButtons dbButtons = new DBButtons(MainActivity.this);
        Adapter adapter = new Adapter(this, dbButtons.buttonList());
        list.setAdapter(adapter);
    }

    /*
        Asks for permissions to access bluetooth

     */

    public void AskForPermissionBluetooth() {
        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
        ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED){

            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
    }

    /*
        After accepting permissions, it checks if it actually has permissions to access bluetooth
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_BLUETOOTH_CONNECT)
        {
            Toast.makeText(this,"onRequestPermissionsResult", Toast.LENGTH_SHORT).show();

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "PERMISO DE BLUETOOTH CONCEDIDO", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "PERMISO DE BLUETOOTH DENEGADO", Toast.LENGTH_SHORT).show();

            }

        }

    }


    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final String[] BLUETOOTH_PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

}

