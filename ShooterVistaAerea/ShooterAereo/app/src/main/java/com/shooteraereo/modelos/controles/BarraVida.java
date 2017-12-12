package com.shooteraereo.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.shooteraereo.modelos.Modelo;

/**
 * Created by Fernando on 11/12/2017.
 */

public class BarraVida extends Modelo {

    private int valorMaximo;
    private int valorActual;

    public BarraVida(Context context) {
        super(context, 80, 25, 0, 0);
        this.valorActual = 100;
        this.valorMaximo = 100;
    }

    public BarraVida(Context context, int valorMaximo, int valorActual) {
        super(context, 80, 25, 0, 0);
        this.valorActual = valorActual;
        this.valorMaximo = valorMaximo;
    }

    public void modificarValorVida(int vidaActual){
        this.valorActual = vidaActual;
    }

    @Override
    public void dibujarEnPantalla(Canvas canvas){
        Paint linea = new Paint();
        linea.setColor(Color.BLACK);
        linea.setStrokeWidth(30);
        canvas.drawLine((int)x, (int) y - 10, (int) canvas.getWidth()-10, (int) y - 10, linea);

        linea.setColor(Color.RED);
        linea.setStrokeWidth(20);
        canvas.drawLine((int) x + 5, (int) y - 10, (int) canvas.getWidth() - 14, (int) y - 10, linea);

        linea.setColor(Color.GREEN);
        linea.setStrokeWidth(20);
        canvas.drawLine((int) x + 5, (int) y - 10, (int) ((x + 5) + (( canvas.getWidth() - 2) / (valorMaximo + 1)) * valorActual + 1), (int) y - 10, linea);

    }


}
