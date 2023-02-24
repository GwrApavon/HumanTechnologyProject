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
    ArrayList<Button> buttons = new ArrayList<>();

    public Adapter(Context context, ArrayList<Button> buttons) {
        this.context = context;
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int i) {
        return buttons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater shown = LayoutInflater.from(context);
        View element = shown.inflate(R.layout.elemento, viewGroup, false);
        ImageView image = element.findViewById(R.id.imagen);
        Uri uriImage = Uri.parse(buttons.get(i).getImage());
        image.setImageURI(uriImage);

        TextView title = element.findViewById(R.id.titulo);
        title.setText(buttons.get(i).getTitle());

        TextView audio = element.findViewById(R.id.audio);
        audio.setText(buttons.get(i).getAudio());

        TextView num_button = element.findViewById(R.id.num_button);
        num_button.setText(String.valueOf(buttons.get(i).getColor()));

        return element;
    }


}
