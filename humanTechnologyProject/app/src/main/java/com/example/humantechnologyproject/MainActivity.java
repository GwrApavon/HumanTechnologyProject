package com.example.humantechnologyproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.example.humantechnologyproject.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

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
        /*
        ImageButton b1 = (ImageButton) findViewById(R.id.edit_button);
        b1.setOnClickListener(this);


        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(this);

         */
    }

/*
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button:
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.edit_button:
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
                break;

        /*
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.edit_button:

                    break;
        }
    }
*/




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

        return super.onOptionsItemSelected(item);
    }
}