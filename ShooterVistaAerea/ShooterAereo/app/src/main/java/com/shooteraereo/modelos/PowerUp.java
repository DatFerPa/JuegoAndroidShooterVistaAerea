package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Fernando on 05/12/2017.
 */

public abstract class PowerUp extends Modelo{


    public PowerUp(Context context, double x, double y) {
        super(context, x, y, 32, 32);


        inicializar();
    }

    protected abstract void inicializar();

    public abstract void accion(Jugador jugador);

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);

    }

}
