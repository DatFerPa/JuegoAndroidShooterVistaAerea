package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Fernando on 12/12/2017.
 */

public class EnemigoPersigue extends Modelo{

    public static final String MOVIENDO = "moviendo";
    public static final String ATACANDO = "atacando";
    public static final String MURIENDO = "muriendo";

    public double velocidadX = 0;
    public double velocidadY = 0;

    private int tiempoMoviemiento = 0;

    private int tiempoAtaque = 30;
    private int tiempoActual = 0;

    private int radioAtaque = 200;

    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<>();

    public EnemigoPersigue(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        this.x = x;
        this.y = y -altura/2;

        inicializar();
    }

    public void inicializar(){

        Sprite moviendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_enemigo_activo),
                ancho,altura,6,6,true);
        sprites.put(MOVIENDO,moviendo);

        Sprite atacando = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_enemigo_ataque),
                ancho,altura,6,6,true);
        sprites.put(ATACANDO,atacando);

        Sprite muriendo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_enemigo_muerte),
                ancho,altura,6,6,false);
        sprites.put(MURIENDO,muriendo);

        sprite = moviendo;

    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY);
    }

    @Override
    public void actualizar(long tiempo){
        boolean finsprite = sprite.actualizar(tiempo);
        sprite = sprites.get(MOVIENDO);
    }


}
