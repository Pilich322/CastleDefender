package ru.itcube.PilichevDeveloper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

import ru.itcube.PilichevDeveloper.manager.AnimationManager;
import ru.itcube.PilichevDeveloper.manager.MemoryManager;
import ru.itcube.PilichevDeveloper.objects.CastleObject;
import ru.itcube.PilichevDeveloper.objects.EnemyObject;
import ru.itcube.PilichevDeveloper.utils.GameResources;
import ru.itcube.PilichevDeveloper.utils.GameSession;
import ru.itcube.PilichevDeveloper.utils.GameSettings;
import ru.itcube.PilichevDeveloper.utils.GameState;
import ru.itcube.PilichevDeveloper.Main;
import ru.itcube.PilichevDeveloper.components.ButtonView;
import ru.itcube.PilichevDeveloper.components.ImageView;
import ru.itcube.PilichevDeveloper.components.RecordsListView;
import ru.itcube.PilichevDeveloper.components.TextView;
import ru.itcube.PilichevDeveloper.manager.ContactManager;
import ru.itcube.PilichevDeveloper.objects.BulletObject;

public class GameScreen extends ScreenAdapter {

    Main main;
    GameSession gameSession;
    CastleObject castleObject;

    ArrayList<EnemyObject> enemyArray;
    ArrayList<BulletObject> bulletArray;

    ContactManager contactManager;
    AnimationManager animationManager;


    // PLAY state UI
    ImageView backgroundView, topBlackoutView;
    TextView scoreTextView, levelTextView, expirienceTextView, healthTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;

    public GameScreen(Main main) {
        this.main = main;
        gameSession = new GameSession();
        enemyArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        castleObject = new CastleObject(
            GameResources.CASTLE_IMG_PATH,
            GameSettings.SCREEN_WIDTH / 2,
            GameSettings.SCREEN_HEIGHT / 20,
            GameSettings.SCREEN_WIDTH,
            GameSettings.SCREEN_HEIGHT / 8,
            main.world
        );
        contactManager = new ContactManager(main.world);
        animationManager = new AnimationManager();
        backgroundView = new ImageView(0, 0, GameResources.BACKGROUND_GAME_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        topBlackoutView = new ImageView(0, 1145, 140, GameResources.BLACKOUT_TOP_IMG_PATH);
        healthTextView = new TextView(main.commonWhiteFont, 100, 1235);
        scoreTextView = new TextView(main.commonWhiteFont, 100, 1200);
        levelTextView = new TextView(main.commonWhiteFont, 350, 1235);
        expirienceTextView = new TextView(main.commonWhiteFont, 350, 1200);
        pauseButton = new ButtonView(
            625, 1205,
            46, 54,
            GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(main.largeWhiteFont, 282, 842, "Pause");
        homeButton = new ButtonView(
            138, 695,
            200, 70,
            main.commonBlackFont,
            GameResources.BUTTON_SHORT_BG_IMG_PATH,
            "Home"
        );
        continueButton = new ButtonView(
            393, 695,
            200, 70,
            main.commonBlackFont,
            GameResources.BUTTON_SHORT_BG_IMG_PATH,
            "Continue"
        );

        recordsListView = new RecordsListView(main.commonWhiteFont, 690);
        recordsTextView = new TextView(main.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
            280, 365,
            160, 70,
            main.commonBlackFont,
            GameResources.BUTTON_SHORT_BG_IMG_PATH,
            "Home"
        );
        AnimationManager.loadAnimation("enemy_run", new String[]{
            GameResources.ENEMY_IMG_PATH,
            GameResources.ENEMY_2_IMG_PATH,
            GameResources.ENEMY_3_IMG_PATH,
            GameResources.ENEMY_2_IMG_PATH
        }, 0.1f, true);
        AnimationManager.loadAnimation("enemy_attack", new String[]{
            GameResources.ATTACK_1_IMG_PATH,
            GameResources.ATTACK_2_IMG_PATH,
            GameResources.ATTACK_3_IMG_PATH
        }, 0.15f, false);
        AnimationManager.loadAnimation("enemy_explosion", new String[]{
            GameResources.EXPLOSION_1_IMG_PATH,
            GameResources.EXPLOSION_2_IMG_PATH,
            GameResources.EXPLOSION_3_IMG_PATH,
            GameResources.EXPLOSION_4_IMG_PATH,
            GameResources.EXPLOSION_5_IMG_PATH,
            GameResources.EXPLOSION_6_IMG_PATH,
            GameResources.EXPLOSION_7_IMG_PATH,
            GameResources.EXPLOSION_8_IMG_PATH,
            GameResources.EXPLOSION_9_IMG_PATH
        }, 0.08f, false);
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnEnemy()) {
                int numberOfEnemies = 1 + new Random().nextInt(castleObject.getLevel()+2);
                for (int i = 0; i < numberOfEnemies; i++) {
                    EnemyObject enemyObject = new EnemyObject(
                        GameResources.ENEMY_IMG_PATH,
                        GameSettings.ENEMY_WIDTH,
                        GameSettings.ENEMY_HEIGHT,
                        main.world,
                        castleObject,
                        delta
                    );
                    enemyObject.updateStat(castleObject.getLevel());
                    enemyArray.add(enemyObject);
                }
            }

            if (!castleObject.isAlive()) {
                gameSession.endGame();
                castleObject.setTexture(GameResources.CASTLE_DESTROYED_IMG_PATH);
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateEnemy(delta);
            updateBullets();
            gameSession.updateScore();
            healthTextView.setText("Health: " + castleObject.getHealth());
            scoreTextView.setText("Score: " + gameSession.getScore());
            levelTextView.setText("Level: " + castleObject.getLevel());
            expirienceTextView.setText("Exp: " + castleObject.getExperience() + "/" + castleObject.getMaxExperience());
            main.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            main.touch = main.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(main.touch.x, main.touch.y)) {
                        gameSession.pauseGame();
                    }
                    Vector2 start = new Vector2(GameSettings.SCREEN_WIDTH / 2, GameSettings.SCREEN_HEIGHT / 8);
                    Vector2 target = new Vector2(main.touch.x, main.touch.y);
                    Vector2 velocity = target.sub(start).nor().scl(GameSettings.BULLET_VELOCITY);

                    BulletObject bullet = new BulletObject(
                        (int) start.x,
                        (int) start.y,
                        GameSettings.BULLET_WIDTH,
                        GameSettings.BULLET_HEIGHT,
                        GameResources.ARROW_IMG_PATH,
                        main.world,
                        velocity
                    );

                    bulletArray.add(bullet);
                    if (main.audioManager.isSoundOn) main.audioManager.shootSound.play();
                    break;

                case PAUSED:
                    if (continueButton.isHit(main.touch.x, main.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(main.touch.x, main.touch.y)) {
                        main.setScreen(main.menuScreen);
                    }
                    break;

                case ENDED:
                    if (homeButton2.isHit(main.touch.x, main.touch.y)) {
                        main.setScreen(main.menuScreen);
                    }
                    break;
            }

        }
    }

    private void draw() {
        main.camera.update();
        main.batch.setProjectionMatrix(main.camera.combined);
        ScreenUtils.clear(Color.CLEAR);
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
        debugRenderer.render(main.world, main.camera.combined);
        main.batch.begin();
        backgroundView.draw(main.batch);
        for (EnemyObject enemy : enemyArray) enemy.draw(main.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(main.batch);
        topBlackoutView.draw(main.batch);
        healthTextView.draw(main.batch);
        scoreTextView.draw(main.batch);
        levelTextView.draw(main.batch);
        expirienceTextView.draw(main.batch);
        pauseButton.draw(main.batch);
        castleObject.draw(main.batch);
        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(main.batch);
            pauseTextView.draw(main.batch);
            homeButton.draw(main.batch);
            continueButton.draw(main.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(main.batch);
            recordsTextView.draw(main.batch);
            recordsListView.draw(main.batch);
            homeButton2.draw(main.batch);
        }
        main.batch.end();
    }

    private void updateEnemy(float delta) {
        for (int i = 0; i < enemyArray.size(); i++) {
            EnemyObject enemy = enemyArray.get(i);
            enemy.update(castleObject.getPosition(), delta);
            if (enemy.isDead()) {
                main.world.destroyBody(enemy.body);
                enemyArray.remove(i--);
                gameSession.destructionRegistration();
                if (main.audioManager.isSoundOn)
                    main.audioManager.playRandomDeathSound();
            }
            if (enemy.isExplosion()) {
                main.world.destroyBody(enemy.body);
                enemyArray.remove(i--);
                gameSession.destructionRegistration();
                if (main.audioManager.isSoundOn)
                    main.audioManager.explosionSound.play(0.2f);
            }
        }
    }

    private void updateBullets() {
        for (int i = 0; i < bulletArray.size(); i++) {
            if (bulletArray.get(i).hasToBeDestroyed()) {
                main.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }

    private void restartGame() {

        for (int i = 0; i < enemyArray.size(); i++) {
            main.world.destroyBody(enemyArray.get(i).body);
            enemyArray.remove(i--);
        }

        if (castleObject != null) {
            main.world.destroyBody(castleObject.body);
        }

        castleObject = new CastleObject(
            GameResources.CASTLE_IMG_PATH,
            GameSettings.SCREEN_WIDTH / 2,
            GameSettings.SCREEN_HEIGHT / 20,
            GameSettings.SCREEN_WIDTH,
            GameSettings.SCREEN_HEIGHT / 8,
            main.world
        );

        bulletArray.clear();
        gameSession.startGame();
    }
}
