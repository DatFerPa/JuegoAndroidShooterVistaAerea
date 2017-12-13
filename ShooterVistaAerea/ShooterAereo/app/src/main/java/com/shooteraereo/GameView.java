package com.shooteraereo;

/**
 * Created by Fernando on 18/11/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shooteraereo.gestores.GestorAudio;
import com.shooteraereo.modelos.Jugador;
import com.shooteraereo.modelos.Nivel;
import com.shooteraereo.modelos.controles.BarraVida;
import com.shooteraereo.modelos.controles.BotonBomba;
import com.shooteraereo.modelos.controles.BotonDisparar;
import com.shooteraereo.modelos.controles.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;


    //controles
    private Pad pad;
    private BotonDisparar botonDisparar;
    private BotonBomba botonBomba;
    private BarraVida barraVida;;

    public GestorAudio gestorAudio;

    public static int pantallaAncho;
    public static int pantallaAlto;

    private Nivel nivel;
    public int numeroNivel = 0;

    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;

        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
        inicializarGestorAudio(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        try {
            procesarEventosTouch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch() throws Exception {
        boolean pulsacionPadMover = false;
        boolean pulsacionDisparo = false;

        for(int i=0; i < 6; i++){
            if(accion[i] != NO_ACTION ) {


                if(accion[i] == ACTION_DOWN){
                    if(nivel.nivelPausado)
                        nivel.nivelPausado = false;
                        if(nivel.win){
                            nivelCompleto();
                        }
                        if(nivel.gameOver){
                            numeroNivel = 0;
                            inicializar();
                        }
                }


                if (pad.estaPulsado(x[i], y[i])) {

                    float orientacionX = pad.getOrientacionX(x[i]);
                    float orientacionY = pad.getOrientacionY(y[i]);
                    System.out.println("Orientaciones : " + orientacionX + "  --  "+orientacionY);
                    // Si almenosuna pulsacion está en el pad
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        nivel.posicionJugadorX = orientacionX;
                        nivel.posicionJugadorY = orientacionY;
                    }else{
                        pulsacionPadMover= false;
                    }
                }

                if(botonDisparar.estaPulsado(x[i],y[i])){
                    float orientacionX = botonDisparar.getOrientacionX(x[i]);
                    float orientacionY = botonDisparar.getOrientacionY(y[i]);


                    //System.out.println("nivel pulsado : " + nivel.botonDisparando);
                    if (accion[i] != ACTION_UP) {
                        nivel.botonDisparando = true;
                        //System.out.println("nivel pulsado : " + nivel.botonDisparando);
                        nivel.posicionDisparoX = orientacionX;
                        nivel.posicionDisparoY = orientacionY;
                    }else{
                        nivel.botonDisparando = false;
                        System.out.println("nivel pulsado : " + nivel.botonDisparando);
                    }
                }

                if(botonBomba.estaPulsado(x[i],y[i])){
                    if(accion[i] == ACTION_DOWN){
                        nivel.botonBombaPulsado = true;
                    }

                }


            }
        }
        if(!pulsacionPadMover) {
            nivel.posicionJugadorX = 0;
            nivel.posicionJugadorY = 0;
        }





    }

    protected void inicializar() throws Exception {
        nivel = new Nivel(context, numeroNivel);
        nivel.gameView = this;
        pad = new Pad(context);
        botonDisparar = new BotonDisparar(context);
        botonBomba = new BotonBomba(context);
        barraVida = new BarraVida(context);
    }

    protected void inicializar(Jugador jugador) throws Exception {
        nivel = new Nivel(context, numeroNivel,jugador);
        nivel.gameView = this;
        pad = new Pad(context);
        botonDisparar = new BotonDisparar(context);
        botonBomba = new BotonBomba(context);
        barraVida = new BarraVida(context);
    }

    public void actualizar(long tiempo) throws Exception {
        if(!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
            barraVida.modificarValorVida(nivel.jugador.vida);
        }
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        if(!nivel.nivelPausado) {
            pad.dibujar(canvas);
            botonDisparar.dibujar(canvas);
            botonBomba.dibujar(canvas);
            barraVida.dibujarEnPantalla(canvas);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void nivelCompleto() throws Exception{
        if(numeroNivel < 1){
            numeroNivel++;
        }else{
            numeroNivel = 0;
        }
        inicializar(nivel.jugador);
    }

    public void inicializarGestorAudio(Context context) {
        gestorAudio = GestorAudio.getInstancia(context, R.raw.musicaambiente);
        gestorAudio.reproducirMusicaAmbiente();
        gestorAudio.registrarSonido(GestorAudio.KILL_IT,
                R.raw.kill_it);
        gestorAudio.registrarSonido(GestorAudio.VICTORY,
                R.raw.flawless_victory);
        gestorAudio.registrarSonido(GestorAudio.LOSER,
                R.raw.loser);
        gestorAudio.registrarSonido(GestorAudio.DISPARO_JUGADOR,
                R.raw.player_shot);
        gestorAudio.registrarSonido(GestorAudio.DISPARO_ENEMIGO,
                R.raw.soldier_shoot);
        gestorAudio.registrarSonido(GestorAudio.BOMBA,
                R.raw.bomb);
        gestorAudio.registrarSonido(GestorAudio.ENEMIGO_PERSIGUE_ATAQUE,
                R.raw.ataque_siguiendo);
    }

}