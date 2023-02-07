package com.example.humantechnologyproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.humantechnologyproject.databinding.ActivityMain2Binding;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private boolean esStorage = true;
    private static final int CODIGO_PERMISOS_ALMACENAMIENTO = 1;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private static final int PICK_AUDIO = 1;
    private static final int PICK_IMAGE = 100;

    Uri imageUri;
    Uri audioUri;
    ImageView foto_gallery;
    ImageView audioButton;

    private Object result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        //Seleccionar foto:
        foto_gallery = (ImageView)findViewById(R.id.addImagen);
        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirPermisosStorage();
            }
        });
        //Seleccionar audio2:
        audioButton = (ImageView)findViewById(R.id.ivAudio);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirPermisosStorage();
            }
        });
        //Seleccionar boton:
        Spinner sBoton = (Spinner) findViewById(R.id.sBoton);
        ArrayList<String> coloresBoton = new ArrayList<>();
        coloresBoton.add("Azul");
        coloresBoton.add("Rojo");
        coloresBoton.add("Amarillo");
        coloresBoton.add("Verde");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, coloresBoton);
        sBoton.setAdapter(adapter);
        sBoton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String colorSeleccionado = (String) sBoton.getSelectedItem();
                TextView rBoton = (TextView) findViewById(R.id.resultadoBoton);
                rBoton.setText("El color seleccionado es: "+colorSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Opciones avanzadas:
        Button bAvanzada = (Button) findViewById(R.id.bAvanzado);
        bAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tAudio = (TextView) findViewById(R.id.tiempoAudio);
                tAudio.setVisibility(View.VISIBLE);
                TextView tPantalla = (TextView) findViewById(R.id.tiempoPantalla);
                tPantalla.setVisibility(View.VISIBLE);
            }
        });

        //toolbar back button
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

//Seleccionar foto:
//Visto: https://es.stackoverflow.com/questions/41707/cargar-una-imagen-desde-la-galeria-android
//       https://stackoverflow.com/questions/71082372/startactivityforresult-is-deprecated-im-trying-to-update-my-code
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
        /**
        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        imageUri = gallery.getData();
                        foto_gallery.setImageURI(imageUri);
                    }
                });
         */
    }
//Seleccionar foto:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
        else {
            if(resultCode == RESULT_OK && requestCode == PICK_AUDIO) {
                audioUri = data.getData();
                String enlace = data.getDataString();

                TextView rAudio = (TextView) findViewById(R.id.resultadoAudio);
                rAudio.setText("Enlace: " + enlace);
            }
        }
    }

    //https://stackoverflow.com/questions/16806905/android-which-implicit-intent-for-picking-audio-files
    private void takeAudio(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, PICK_AUDIO);

    }

    private void pedirPermisosStorage() {
        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity2.this);
        ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            MainActivity2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PERMISOS_ALMACENAMIENTO) {
            /* Resultado de la solicitud para permiso de cámara
             Si la solicitud es cancelada por el usuario, el método .lenght sobre el array
             'grantResults' devolverá null.*/

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(esStorage) {
                    openGallery();
                }
                else {
                    takeAudio();
                }

            } else {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(MainActivity2.this);
                ADBuilder.setMessage("Para conectar la botonera, necesario utilizar el bluetooth de tu dispositivo. Permite que 'SerrAlertas' pueda acceder al bluetooth.");

                ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                MainActivity2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO
                        );
                    }
                });
                AD = ADBuilder.create();
                AD.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //back arrow function
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        //Adds a new Button to the DB
        if (id == R.id.save) {
            //MainActivity.insertarDatos();
        }
        return super.onOptionsItemSelected(item);
    }

}


