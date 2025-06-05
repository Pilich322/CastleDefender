package ru.itcube.PilichevDeveloper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.itcube.PilichevDeveloper.utils.GameResources;
import ru.itcube.PilichevDeveloper.Main;
import ru.itcube.PilichevDeveloper.components.ButtonView;
import ru.itcube.PilichevDeveloper.components.MovingBackgroundView;
import ru.itcube.PilichevDeveloper.components.TextView;

public class MenuScreen extends ScreenAdapter {
    Main main;
    MovingBackgroundView backgroundView;
    TextView titleView;
    ButtonView startButtonView;
    ButtonView settingsButtonView;
    ButtonView exitButtonView;

    public MenuScreen(Main main) {
        this.main = main;

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        titleView = new TextView(main.largeWhiteFont, 180, 960, "Space Cleaner");
        startButtonView = new ButtonView(140, 646, 440, 70, main.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "start");
        settingsButtonView = new ButtonView(140, 551, 440, 70, main.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "settings");
        exitButtonView = new ButtonView(140, 456, 440, 70, main.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "exit");
    }

    @Override
    public void render(float delta) {

        handleInput();

        main.camera.update();
        main.batch.setProjectionMatrix(main.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        main.batch.begin();

        backgroundView.draw(main.batch);
        titleView.draw(main.batch);
        exitButtonView.draw(main.batch);
        settingsButtonView.draw(main.batch);
        startButtonView.draw(main.batch);
        backgroundView.move();
        main.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            main.touch = main.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startButtonView.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(main.gameScreen);
            }
            if (exitButtonView.isHit(main.touch.x, main.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(main.settingsScreen);
            }
        }
    }
}
