package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.shooteraereo.GameView;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Fondo extends Modelo {

    public Fondo(Context context, Drawable imagen) {
        super(context,
                GameView.pantallaAncho/2,
                GameView.pantallaAlto/2,
                GameView.pantallaAlto,
                GameView.pantallaAncho );

        this.imagen = imagen;
    }
}
