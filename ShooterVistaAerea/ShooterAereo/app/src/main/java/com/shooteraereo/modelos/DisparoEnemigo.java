package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.graficos.Sprite;

/**
 * Created by Fernando on 12/12/2017.
 */

public class DisparoEnemigo extends Modelo {

    private Sprite sprite;
    public double velocidadX;
    public double velocidadY;

    public DisparoEnemigo(Context context,  double xInicial, double yInicial, float posicionDisparoX, float posicionDiparoY) {
        super(context, xInicial, yInicial, 20, 20);


        setVelocidadDisparo(posicionDisparoX,posicionDiparoY);

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        inicializar();

    }

    private void inicializar() {
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.disparo2),
                ancho, altura,
                24, 6, true);
    }


    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);


    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }

    private void setVelocidadDisparo(float posicionDisparoX, float posicionDisparoY) {

        float posDisparoX =  posicionDisparoX - (float)x ;
        float posDiisparoY = posicionDisparoY-(float)y;

        float vecX = (posDisparoX)/(float)((Math.sqrt(Math.pow((double)posDisparoX,2)+Math.pow((double)posDiisparoY,2))));
        float vecY = (posDiisparoY)/(float)((Math.sqrt(Math.pow((double)posDisparoX,2)+Math.pow((double)posDiisparoY,2))));

        float posicionXCalculada = 8*vecX ;
        float posicionYCalculada = 8*vecY ;

        velocidadY = posicionYCalculada;
        velocidadX = posicionXCalculada;
    }
}
