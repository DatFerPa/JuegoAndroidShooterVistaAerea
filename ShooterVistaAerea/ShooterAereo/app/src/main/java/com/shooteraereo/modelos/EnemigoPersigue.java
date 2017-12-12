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

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;

    public static final String MOVIENDO = "moviendo";
    public static final String ATACANDO = "atacando";
    public static final String MURIENDO = "muriendo";

    public static final double VELOCIDAD_BASE = 3;
    public double velocidadX = 0;
    public double velocidadY = 0;

    private int tiempoMoviemiento = 0;

    private int tiempoAtaque = 10;
    private int tiempoActual = 0;

    private int radioAtaque = 50;

    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<>();
    private boolean atacando = false;

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
        if ( estado == INACTIVO && finsprite == true){
            estado = ELIMINAR;
        }
        if (estado == INACTIVO){
                sprite = sprites.get(MURIENDO);
        }else {
            if(atacando){
                sprite = sprites.get(ATACANDO);
            }else {
                sprite = sprites.get(MOVIENDO);
            }
        }
    }

    public void actualizarTiempoMovimiento(double jugadorX, double jugadorY){
        if(this.estado == ACTIVO) {
            if (tiempoMoviemiento % 20 == 0 ) {
                    generarVelocidad(jugadorX, jugadorY);
            }
            ++tiempoMoviemiento;
        }
    }

    public boolean ataque(){
        if(tiempoActual >= tiempoAtaque && atacando){
            tiempoActual = 0;
            return true;
        }else{
            tiempoActual++;
            return false;
        }
    }

    private void generarVelocidad(double jugadorX, double jugadorY) {

        float posDisparoX =  (float)jugadorX - (float)x ;
        float posDiisparoY = (float)jugadorY-(float)y;

        double moduloVector = (Math.sqrt(Math.pow((double)posDisparoX,2)+Math.pow((double)posDiisparoY,2)));
        if(moduloVector < radioAtaque){
            velocidadY = 0;
            velocidadX = 0;
            atacando = true;
        }else {
            atacando = false;

            float vecX = (posDisparoX) / (float) ((Math.sqrt(Math.pow((double) posDisparoX, 2) + Math.pow((double) posDiisparoY, 2))));
            float vecY = (posDiisparoY) / (float) ((Math.sqrt(Math.pow((double) posDisparoX, 2) + Math.pow((double) posDiisparoY, 2))));

            float posicionXCalculada = 8 * vecX;
            float posicionYCalculada = 8 * vecY;


            velocidadY = posicionYCalculada;
            velocidadX = posicionXCalculada;
        }
    }


}
