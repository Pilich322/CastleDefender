package ru.itcube.PilichevDeveloper.manager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationManager {
    private static final HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
    private static final HashMap<String, Texture[]> textures = new HashMap<>();

    public static void loadAnimation(String key, String[] framePaths, float frameDuration, boolean looped) {
        Texture[] frames = new Texture[framePaths.length];
        TextureRegion[] regions = new TextureRegion[framePaths.length];

        for (int i = 0; i < framePaths.length; i++) {
            frames[i] = new Texture(framePaths[i]);
            regions[i] = new TextureRegion(frames[i]);
        }
        System.out.println("говно"+key+ " " + frames.length);

        Animation<TextureRegion> animation = new Animation<>(frameDuration, regions);
        animation.setPlayMode(looped ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        animations.put(key, animation);
        textures.put(key, frames);
    }

    public static Animation<TextureRegion> get(String key) {
        return animations.get(key);
    }

    public static void dispose() {
        for (Texture[] frameSet : textures.values()) {
            for (Texture tex : frameSet) {
                tex.dispose();
            }
        }
        animations.clear();
        textures.clear();
    }
}
