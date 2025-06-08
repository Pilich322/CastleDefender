package ru.itcube.PilichevDeveloper.utils;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float ENEMY_VELOCITY = 10;
    public static long STARTING_ENEMY_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds
    public static int BULLET_VELOCITY = 40; // in [m/s] - meter per second

    public static final short BULLET_BIT = 1;
    public static final short ENEMY_BIT = 2;
    public static final short CASTLE_BIT = 8;

    // Object sizes

    public static final int ENEMY_WIDTH = 100;
    public static final int ENEMY_HEIGHT = 100;

    public static final int BULLET_WIDTH = 15;
    public static final int BULLET_HEIGHT = 45;



}
