package  ru.itcube.PilichevDeveloper.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import  ru.itcube.PilichevDeveloper.utils.GameResources;
public class AudioManager {

    public boolean isSoundOn;
    public boolean isMusicOn;

    public Music backgroundMusic;
    public Sound shootSound;
    public Sound explosionSound;
    private final List<Sound> deathSounds;

    public AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BACKGROUND_MUSIC_PATH));
        shootSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.SHOOT_SOUND_PATH));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.DESTROY_SOUND_PATH));
        deathSounds = new ArrayList<>();
        backgroundMusic.setVolume(0.2f);
        backgroundMusic.setLooping(true);
        initializeDeathSounds();
        updateSoundFlag();
        updateMusicFlag();
    }

    private void initializeDeathSounds(){
        deathSounds.add(Gdx.audio.newSound(Gdx.files.internal(GameResources.DEATH_1_SOUND_PATH)));
        deathSounds.add(Gdx.audio.newSound(Gdx.files.internal(GameResources.DEATH_2_SOUND_PATH)));
    }
    public void playRandomDeathSound() {
        if (!deathSounds.isEmpty()) {
            int index = MathUtils.random(0, deathSounds.size() - 1);
            deathSounds.get(index).play(0.2f);
        }
    }
    public void updateSoundFlag() {
        isSoundOn = MemoryManager.loadIsSoundOn();
    }

    public void updateMusicFlag() {
        isMusicOn = MemoryManager.loadIsMusicOn();

        if (isMusicOn) backgroundMusic.play();
        else backgroundMusic.stop();
    }

}
