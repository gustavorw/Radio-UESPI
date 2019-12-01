package com.gustavorw.app.Player;

public class ConfPlayer {
    /*
    link da documentação da lib: https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html
     */

    //Controle minimo de buffer customizado, mas pode usar os padroes da lib
    public static final int MIN_BUFFER_DURATION = 3000;
    public static final int MAX_BUFFER_DURATION = 5000;
    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;

    //URL do streaming
    public static final String PLAYER_URL = "http://rfmbrasil.servemp3.com:8000/radio-uespi.ogg";
    //public static final String PLAYER_URL = "http://listen.42fm.ru:8000/stealkill-3.0.ogg";

}
