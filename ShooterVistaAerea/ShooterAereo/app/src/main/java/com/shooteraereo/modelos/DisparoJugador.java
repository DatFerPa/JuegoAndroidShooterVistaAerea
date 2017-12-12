package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.graficos.Sprite;

/**
 * Created by Fernando on 23/11/2017.
 */

public class DisparoJugador extends Modelo {

    public static double VELOCIDAD_BASE = 8;
    public static double velocidad_Actual = 10;

    private Sprite sprite;
    public double velocidadX;
    public double velocidadY;

    public DisparoJugador(Context context, double xInicial, double yInicial, float posicionDisparoX, float posicionDiparoY){
        super(context, xInicial, yInicial, 20, 20);

        setVelocidadDisparo(posicionDisparoX,posicionDiparoY);

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        inicializar();
    }

    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.disparo1),
                ancho, altura,
                24, 4, true);
    };

    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);


    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }


    public void setVelocidadDisparo(float posicionDisparoX,float posicionDisparoY){
        /*
            vector unitario
         */
        float vecX = (posicionDisparoX)/(float)((Math.sqrt(Math.pow((double)posicionDisparoX,2)+Math.pow((double)posicionDisparoY,2))));
        float vecY = (posicionDisparoY)/(float)((Math.sqrt(Math.pow((double)posicionDisparoX,2)+Math.pow((double)posicionDisparoY,2))));

        float posicionXCalculada = 10*vecX;
        float posicionYCalculada = 10*vecY;

        velocidadY = posicionYCalculada;
        velocidadX = posicionXCalculada;

    }

}
