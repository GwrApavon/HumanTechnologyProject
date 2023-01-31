package com.example.humantechnologyproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.humantechnologyproject.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISO_BLUETOOTH = 0;
    private static final int PERMISO_BLUETOOTH_ADMIN = 0;
    private static final int PERMISO_BLUETOOTH_CONNECT = 0;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    ArrayList<Datos> datos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos.add(new Datos(R.drawable.ima1, "DONUTS", "El 157 características nuevas.", 1));

        datos.add(new Datos(R.drawable.ima2, "FROYO", "El 20 7 núcleo Linux 2.6.32.", 2));

        datos.add(new Datos(R.drawable.ima3, "GINGERBREAD", "El 6 de 7basado.", 3));

        datos.add(new Datos(R.drawable.ima4, "HONEYCOMB", "El 22 teléfonos Android.", 4));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ListView lista = (ListView) findViewById(R.id.lista);
        Adaptador adaptador = new Adaptador(this, datos);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        }
        if (id == R.id.action_bluetooth) {

            pedirPermisosBluetooth();
        }

        return super.onOptionsItemSelected(item);
    }

    private void pedirPermisosBluetooth() {
        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity.this);
        ADBuilder.setMessage("Para escanear un producto es necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED){
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISO_BLUETOOTH
                    );
                }
            });
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED){
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN},PERMISO_BLUETOOTH_ADMIN
                    );
                }
            });
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED){

            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT},PERMISO_BLUETOOTH_CONNECT
                    );
                }
            });
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}