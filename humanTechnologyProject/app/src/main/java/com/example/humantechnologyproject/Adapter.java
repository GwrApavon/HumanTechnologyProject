package com.example.humantechnologyproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context context;
    ArrayList<Button> datoes = new ArrayList<>();

    public Adapter(Context context, ArrayList<Button> datoes) {
        this.context = context;
        this.datoes = datoes;
    }

    @Override
    public int getCount() {
        return datoes.size();
    }

    @Override
    public Object getItem(int i) {
        return datoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private Bitmap getImageView(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        return decoded;
    }

    //Poner la imagen con un tamaño marcado
    private Object getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //Si el tamaño es adecuado:
        if(width <= maxSize && height <= maxSize) {
            return bitmap;
        }
        //Calculo de nuevo tamaño:
        float bitmapRatio = (float) width / (float) height;
        if(bitmapRatio > 1) {
            width = maxSize;
            height = (int) (height * bitmapRatio);
        }
        else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mostrado = LayoutInflater.from(context);
        View elemento = mostrado.inflate(R.layout.elemento, viewGroup, false);
        ImageView imagen = elemento.findViewById(R.id.imagen);
        Uri uriFoto = Uri.parse(datoes.get(i).getImagen());
        imagen.setImageURI(uriFoto);
        /*
        try {
            //Crear un imageView a partir de una ruta:
            File file = new File(datos.get(i).getImagen());
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
            imagen.setImageBitmap(getImageView((Bitmap) getResizedBitmap(bitmap, 1024)));
        }catch(IOException e) {
            e.printStackTrace();
        }
        */
        TextView titulo = elemento.findViewById(R.id.titulo);
        titulo.setText(datoes.get(i).getTitulo());

        TextView audio = elemento.findViewById(R.id.audio);
        audio.setText(datoes.get(i).getAudio());

        TextView num_button = elemento.findViewById(R.id.num_button);
        num_button.setText(String.valueOf(datoes.get(i).getColor()));

        //ImageButton edit_button = (ImageButton) elemento.findViewById(R.id.edit_button);
        //edit_button.setImageResource(datos.get(i).getEdit_button());
        return elemento;
    }


}
