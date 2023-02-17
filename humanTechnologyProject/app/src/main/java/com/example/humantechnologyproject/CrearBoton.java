package com.example.humantechnologyproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.humantechnologyproject.databinding.ActivityMain2Binding;
import com.example.humantechnologyproject.db.DBButtons;

import java.util.ArrayList;

public class CrearBoton extends AppCompatActivity {
    private static final int CODIGO_PERMISOS_ALMACENAMIENTO = 1;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private static final int PICK_AUDIO = 1;
    private static final int PICK_IMAGE = 100;
    boolean permisosDados = false;

    //Campos boton:
    EditText enterTitle, ScreenTime, AudioTime;
    ImageView addImage, idAudio;
    Spinner buttonColor;
    Datos button;
    int id = 0;

    Uri imageUri;
    Uri audioUri;

    //valores para crear el boton:
    String imagePath = "";
    String audioPath = "";
    String color = "";
    String titulo = "";
    int screentime = 0;
    int soundtime = 0;

    private Object result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enterTitle = findViewById(R.id.enterTitle);
        ScreenTime = findViewById(R.id.ScreenTime);
        AudioTime = findViewById(R.id.AudioTime);
        addImage = findViewById(R.id.addImage);
        idAudio = findViewById(R.id.idAudio);
        buttonColor = findViewById(R.id.buttonColor);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //Seleccionar foto:
        ImageSelect();

        //Seleccionar audio2:
        audioSelect();

        //Seleccionar boton:
        Spinner buttonColor = (Spinner) findViewById(R.id.buttonColor);
        ArrayList<String> coloresBoton = new ArrayList<>();
        coloresBoton.add("Azul");
        coloresBoton.add("Rojo");
        coloresBoton.add("Amarillo");
        coloresBoton.add("Verde");
        colorSelectionSpinner(buttonColor, coloresBoton);

        //Opciones avanzadas:
        showAdvacedOptions();

        //toolbar back button
        backButtonToolbar();
    }

    /*
        Used to select the audio you'll use as a notification alarm for a button
     */
    private void audioSelect() {
        idAudio = findViewById(R.id.idAudio);
        idAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarPermisos()) {
                    SelectAudio();
                }
                else {
                    pedirPermisos();
                }
            }
        });
    }

    /*
        Color selection for a button (depending on the color of the button from the keypad)
     */
    private void colorSelectionSpinner(Spinner buttonColor, ArrayList<String> coloresBoton) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, coloresBoton);
        buttonColor.setAdapter(adapter);
        buttonColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String colorSeleccionado = (String) buttonColor.getSelectedItem();

                TextView rBoton = (TextView) findViewById(R.id.resultadoBoton);
                rBoton.setText("El color seleccionado es: " + colorSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*
        Back button toolbar
        Takes you to the main activity
     */
    private void backButtonToolbar() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /*
        Shows the advanced options for every button
        ScreenTime = time the notification will be on the screen
        AudioTime = time the audio will sound
     */
    private void showAdvacedOptions() {
        Button bAvanzada = (Button) findViewById(R.id.bAvanzado);
        bAvanzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tAudio = (TextView) findViewById(R.id.AudioTime);
                tAudio.setVisibility(View.VISIBLE);
                TextView tPantalla = (TextView) findViewById(R.id.ScreenTime);
                tPantalla.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
        Used to select the Image you'll use for a button
     */
    private void ImageSelect() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verificarPermisos()) {
                    openGallery();
                }
                else {
                    pedirPermisos();
                }
            }
        });
    }

    /*
        Toolbar options
        Home(back) = close the activity and returns you to the previous
        Save = saves the button in the database
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //back arrow function
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to previous activity (if there is any)
        }
        //Adds a new Button to the DB
        if (id == R.id.save) {
            //Seleccionar titulo:

            enterTitle = findViewById(R.id.enterTitle);
            titulo = enterTitle.getText().toString();

            buttonColor = findViewById(R.id.buttonColor);
            color = buttonColor.getSelectedItem().toString();

            ScreenTime = findViewById(R.id.ScreenTime);
            screentime = Integer.parseInt("" + ScreenTime.getText());

            AudioTime = findViewById(R.id.AudioTime);
            soundtime = Integer.parseInt("" + AudioTime.getText());


            DBButtons dbButtons = new DBButtons(this);


            long comprobacion = dbButtons.insertarBoton(
                    titulo,
                    imagePath,
                    audioPath,
                    color,
                    screentime,
                    soundtime);

            if (comprobacion > 0) {
                Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_SHORT).show();
                //FieldCleaner();
            } else {
                Toast.makeText(this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();
                //FieldCleaner();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




    /*
        Allows you to pick a photo from the gallery
     */
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    /*
        Sets ImageView with the photo picked from the gallery
        or
        Sest TextView with "Audio Seleccionado" once you picked an audio from your storage
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imagePath = imageUri.toString();
            addImage.setImageURI(imageUri);
        }
        else {
            if(resultCode == RESULT_OK && requestCode == PICK_AUDIO) {
                audioUri = data.getData();
                String enlace = "";
                enlace = data.getDataString();
                audioPath = data.getDataString();
                TextView rAudio = (TextView) findViewById(R.id.resultadoAudio);
                if(!enlace.equals("")) {
                    rAudio.setText("Audio seleccionado");
                }
            }
        }
    }

   /*
        Allows you to select an Audio from the storage
    */
    private void SelectAudio(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, PICK_AUDIO);

    }

    /*
        Asks for permissions to access the storage
     */
    private void pedirPermisos() {
        AlertDialog AD;
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(CrearBoton.this);
        ADBuilder.setMessage("Permite que 'SerrAlertas' pueda acceder al almacenamiento.");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            CrearBoton.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO
                    );
                }
            });
            AD = ADBuilder.create();
            AD.show();
        }
    }

    /*
        After accepting permissions, it checks if it actually has permissions to access the storage
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PERMISOS_ALMACENAMIENTO) {
            /* Resultado de la solicitud para permiso de cámara
             Si la solicitud es cancelada por el usuario, el método .lenght sobre el array
             'grantResults' devolverá null.*/

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisosDados = true;

            } else {
                AlertDialog AD;
                AlertDialog.Builder ADBuilder = new AlertDialog.Builder(CrearBoton.this);
                ADBuilder.setMessage("Permite que 'SerrAlertas' pueda acceder al almacenamiento");

                ADBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                CrearBoton.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO
                        );
                    }
                });
                AD = ADBuilder.create();
                AD.show();
            }
        }
    }


    /*
        Verifies if it has permissions to access the storage
     */
    private boolean verificarPermisos() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(CrearBoton.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            return true;
        } else {
            return false;
        }
    }

    /*
        Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_a2, menu);
        return true;
    }
}


/* COSAS HECHAS A MITAD PARA
    Bundle extras = getIntent().getExtras();
        id = extras.getInt("ID");
        if(id > 0){
            DBButtons dbButtons = new DBButtons(this);
            button = dbButtons.buttonView(id);

            if(button != null){
                enterTitle = findViewById(R.id.enterTitle);
                ScreenTime = findViewById(R.id.ScreenTime);
                AudioTime = findViewById(R.id.AudioTime);
                addImage = findViewById(R.id.addImage);
                idAudio = findViewById(R.id.idAudio);
                buttonColor = findViewById(R.id.buttonColor);

                binding = ActivityMain2Binding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                setSupportActionBar(binding.toolbar);

                Uri uriFoto = Uri.parse(button.getImagen());
                addImage.setImageURI(uriFoto);
                String audioPath = button.getAudio();
                String color = button.getColor();
                enterTitle.setText(button.getTitulo());
                if(button.getScreenTime() > 0){
                    ScreenTime.setText(button.getScreenTime());
                }
                if(button.getAudioTime() > 0){
                    AudioTime.setText(button.getAudioTime());
                }
            }
        }
 */

