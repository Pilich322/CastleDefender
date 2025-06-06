package ru.itcube.PilichevDeveloper.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ru.itcube.PilichevDeveloper.manager.AnimationManager;
import ru.itcube.PilichevDeveloper.utils.GameSettings;

public class EnemyObject extends GameObject {

    public enum State {
        RUNNING,
        PREPARING_EXPLOSION,
        EXPLODING,
        DEAD
    }

    private State currentState;
    private float stateTime;


    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> prepareAnimation;
    private Animation<TextureRegion> explosionAnimation;
    private static final int paddingHorizontal = 10;
    private int health;
    private int damage = 10;
    private int experience = 10;


    public EnemyObject(String texturePath, int width, int height, World world, CastleObject castleObject, float delta) {
        super(
            texturePath,
            width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
            GameSettings.SCREEN_HEIGHT + height / 2,
            width, height,
            GameSettings.ENEMY_BIT,
            world
        );
        body.destroyFixture(body.getFixtureList().first());

        PolygonShape shape = new PolygonShape();
        float scaleFactor = 0.6f;
        shape.setAsBox(width * scaleFactor / 2f * GameSettings.SCALE, height * scaleFactor / 2f * GameSettings.SCALE);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        fdef.filter.categoryBits = GameSettings.ENEMY_BIT;

        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData(this);

        shape.dispose();
        currentState = State.RUNNING;
        update(castleObject.getPosition(), delta);

        stateTime = 0f;

        runAnimation = AnimationManager.get("enemy_run");
        prepareAnimation = AnimationManager.get("enemy_attack");
        explosionAnimation = AnimationManager.get("enemy_explosion");
        health = 10;

    }

    public void update(Vector2 castlePosition, float delta) {
        stateTime += delta;

        switch (currentState) {
            case RUNNING:
                Vector2 direction = new Vector2(castlePosition.x - getX(), castlePosition.y - getY()).nor();
                body.setLinearVelocity(direction.scl(GameSettings.ENEMY_VELOCITY));
                break;

            case PREPARING_EXPLOSION:
                body.setLinearVelocity(0, 0); // стоим на месте
                if (prepareAnimation.isAnimationFinished(stateTime)) {
                    currentState = State.EXPLODING;
                    stateTime = 0;
                }
                break;

            case EXPLODING:
                body.setLinearVelocity(0, 0);
                if (explosionAnimation.isAnimationFinished(stateTime)) {
                    currentState = State.DEAD;
                    stateTime = 0;
                    health = 0;
                }
                break;

            case DEAD:
                // Можно пометить для удаления
                break;
        }

    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame;

        switch (currentState) {
            case RUNNING:
                currentFrame = runAnimation.getKeyFrame(stateTime, true);
                break;
            case PREPARING_EXPLOSION:
                currentFrame = prepareAnimation.getKeyFrame(stateTime, false);
                break;
            case EXPLODING:
                currentFrame = explosionAnimation.getKeyFrame(stateTime, false);
                break;
            default:
                return; // НЕ рисуем, если мёртв
        }
        batch.draw(currentFrame, getX(), getY(), width, height);
    }

    public void startExplosionPreparation() {
        if (currentState == State.RUNNING) {
            currentState = State.PREPARING_EXPLOSION;
            stateTime = 0;
        }
    }

    public boolean isDead() {
        if (health > 0)
            return false;
        else {
            currentState = State.DEAD;
            return true;
        }

    }

    @Override
    public void hit() {
        if (currentState == State.RUNNING)
            health--;
    }

    public int getExperience() {
        return experience;
    }

    public int getHealth() {
        return health;
    }

    public void setDamage(int lvl) {
        damage = (damage * lvl);
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isExplosion() {
        return currentState == State.EXPLODING;
    }
}
