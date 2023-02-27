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

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_BLUETOOTH = 0;
    private static final int PERMISSION_BLUETOOTH_ADMIN = 0;
    private static final int PERMISSION_BLUETOOTH_CONNECT = 0;
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
        Bluetooth = if it's not active, it gives permissions to activate it
        Settings = Opens settings activity (not yet implemented)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, ButtonSettings.class);
            intent.putExtra("ID", 0);
            startActivity(intent);
        }
        if (id == R.id.action_bluetooth) {

            AskForPermissionBluetooth();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        ListView lista = (ListView) findViewById(R.id.lista);
        DBButtons dbButtons = new DBButtons(MainActivity.this);
        Adapter adapter = new Adapter(this, dbButtons.buttonList());
        lista.setAdapter(adapter);
    }

    /*
        Asks for permissions to access bluetooth
     */
    private void AskForPermissionBluetooth() {

        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
        ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED){
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED){
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
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
        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_CONNECT);
        }

         */
    }

    /*
        After accepting permissions, it checks if it actually has permissions to access bluetooth
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_BLUETOOTH)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            }
            else {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
                ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");

                ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH
                        );
                    }
                });
                AD = ADBuilder.create();
                AD.show();
            }
        }
        if (requestCode == PERMISSION_BLUETOOTH_ADMIN) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            }
            else
            {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
                ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");

                ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN
                        );
                    }
                });
                AD = ADBuilder.create();
                AD.show();
            }
        }
        if (requestCode == PERMISSION_BLUETOOTH_CONNECT)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            }
            else
            {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
                ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");

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
    }
}