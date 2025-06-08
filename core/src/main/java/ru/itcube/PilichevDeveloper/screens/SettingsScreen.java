package ru.itcube.PilichevDeveloper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.itcube.PilichevDeveloper.utils.GameResources;
import ru.itcube.PilichevDeveloper.Main;
import ru.itcube.PilichevDeveloper.components.ButtonView;
import ru.itcube.PilichevDeveloper.components.ImageView;
import ru.itcube.PilichevDeveloper.components.TextView;
import ru.itcube.PilichevDeveloper.manager.MemoryManager;

public class SettingsScreen extends ScreenAdapter {

    Main main;

    ImageView blackoutImageView,backgroundView;
    ButtonView returnButton;
    TextView titleTextView,musicSettingView,soundSettingView,clearSettingView;

    public SettingsScreen(Main main) {
        this.main = main;

        backgroundView = new ImageView(0,0,GameResources.BACKGROUND_MENU_IMG_PATH);
        titleTextView = new TextView(main.largeWhiteFont, 256, 956, "Settings");
        blackoutImageView = new ImageView(85, 365, 450,550,GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        clearSettingView = new TextView(main.commonWhiteFont, 173, 599, "clear records");

        musicSettingView = new TextView(
            main.commonWhiteFont,
            173, 717,
            "music: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );

        soundSettingView = new TextView(
            main.commonWhiteFont,
            173, 658,
            "sound: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );

        returnButton = new ButtonView(
            280, 447,
            160, 70,
            main.commonBlackFont,
            GameResources.BUTTON_SHORT_BG_IMG_PATH,
            "return"
        );

    }

    @Override
    public void render(float delta) {

        handleInput();

        main.camera.update();
        main.batch.setProjectionMatrix(main.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        main.batch.begin();

        backgroundView.draw(main.batch);
        titleTextView.draw(main.batch);
        blackoutImageView.draw(main.batch);
        returnButton.draw(main.batch);
        musicSettingView.draw(main.batch);
        soundSettingView.draw(main.batch);
        clearSettingView.draw(main.batch);

        main.batch.end();
    }

    void handleInput() {
        if (Gdx.input.justTouched()) {
            main.touch = main.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(main.touch.x, main.touch.y)) {
                main.setScreen(main.menuScreen);
            }
            if (clearSettingView.isHit(main.touch.x, main.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                clearSettingView.setText("clear records (cleared)");
            }
            if (musicSettingView.isHit(main.touch.x, main.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                main.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(main.touch.x, main.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                main.audioManager.updateSoundFlag();
            }
        }
    }

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }
}
