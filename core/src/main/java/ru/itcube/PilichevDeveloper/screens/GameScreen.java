package  ru.itcube.PilichevDeveloper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.itcube.PilichevDeveloper.objects.CastleObject;
import ru.itcube.PilichevDeveloper.objects.EnemyObject;
import  ru.itcube.PilichevDeveloper.utils.GameResources;
import  ru.itcube.PilichevDeveloper.utils.GameSession;
import  ru.itcube.PilichevDeveloper.utils.GameSettings;
import  ru.itcube.PilichevDeveloper.utils.GameState;
import  ru.itcube.PilichevDeveloper.Main;
import  ru.itcube.PilichevDeveloper.components.ButtonView;
import  ru.itcube.PilichevDeveloper.components.ImageView;
import  ru.itcube.PilichevDeveloper.components.LiveView;
import  ru.itcube.PilichevDeveloper.components.MovingBackgroundView;
import  ru.itcube.PilichevDeveloper.components.RecordsListView;
import  ru.itcube.PilichevDeveloper.components.TextView;
import  ru.itcube.PilichevDeveloper.manager.ContactManager;
import  ru.itcube.PilichevDeveloper.objects.BulletObject;
import  ru.itcube.PilichevDeveloper.objects.TrashObject;

public class  GameScreen extends ScreenAdapter {

    Main main;
    GameSession gameSession;
    //ShipObject shipObject;
    CastleObject castleObject;

    ArrayList<EnemyObject> enemyArray;
    ArrayList<BulletObject> bulletArray;

    ContactManager contactManager;

    // PLAY state UI
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
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

        contactManager = new ContactManager(main.world);
        enemyArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

//        shipObject = new ShipObject(
//            GameSettings.SCREEN_WIDTH / 2, 150,
//            GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
//            GameResources.SHIP_IMG_PATH,
//            main.world
//        );
        castleObject = new CastleObject(
            GameResources.CASTLE_IMG_PATH,
            GameSettings.SCREEN_WIDTH / 2,
            GameSettings.SCREEN_HEIGHT / 20,
            GameSettings.SCREEN_WIDTH,
            GameSettings.SCREEN_HEIGHT / 8,
            main.world
        );

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        scoreTextView = new TextView(main.commonWhiteFont, 50, 1215);
        pauseButton = new ButtonView(
            605, 1200,
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

    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                EnemyObject enemyObject = new EnemyObject(
                    GameResources.TRASH_IMG_PATH,GameSettings.TRASH_WIDTH,
                    GameSettings.TRASH_HEIGHT,
                    main.world,castleObject
                );
                enemyArray.add(enemyObject);
            }

//            if (shipObject.needToShoot()) {
//                BulletObject laserBullet = new BulletObject(
//                    shipObject.getX(), shipObject.getY() + shipObject.height / 2,
//                    GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
//                    GameResources.BULLET_IMG_PATH,
//                    main.world
//                );
//                bulletArray.add(laserBullet);
//                if (main.audioManager.isSoundOn) main.audioManager.shootSound.play();
//            }

//            if (!shipObject.isAlive()) {
//                gameSession.endGame();
//                recordsListView.setRecords(MemoryManager.loadRecordsTable());
//            }

            updateEnemy();
            updateBullets();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            //liveView.setLeftLives(shipObject.getLiveLeft());

            main.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            main.touch = main.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(main.touch.x, main.touch.y)) {
                        gameSession.pauseGame();
                    }
                   // shipObject.move(main.touch);
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
        //shipObject.draw(main.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(main.batch);
        topBlackoutView.draw(main.batch);
        scoreTextView.draw(main.batch);
        liveView.draw(main.batch);
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

    private void updateEnemy() {
        for (int i = 0; i < enemyArray.size(); i++) {
            EnemyObject enemy = enemyArray.get(i);


            enemy.update(castleObject.getPosition());
            boolean hasToBeDestroyed = !enemyArray.get(i).isAlive();

            if (!enemyArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (main.audioManager.isSoundOn) main.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                main.world.destroyBody(enemyArray.get(i).body);
                enemyArray.remove(i--);
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

//        if (shipObject != null) {
//            main.world.destroyBody(shipObject.body);
//        }

//        shipObject = new ShipObject(
//            GameSettings.SCREEN_WIDTH / 2, 150,
//            GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
//            GameResources.SHIP_IMG_PATH,
//            main.world
//        );

        bulletArray.clear();
        gameSession.startGame();
    }

}
