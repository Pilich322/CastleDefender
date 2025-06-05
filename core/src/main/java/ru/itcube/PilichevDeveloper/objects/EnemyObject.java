package ru.itcube.PilichevDeveloper.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ru.itcube.PilichevDeveloper.utils.GameSettings;

public class EnemyObject extends GameObject{
    private static final int paddingHorizontal = 10;
    private int health;
    private int damage;
    public EnemyObject(String texturePath, int width, int height, World world, CastleObject castleObject) {
        super(
            texturePath,
            width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
            GameSettings.SCREEN_HEIGHT + height / 2,
            width, height,
            GameSettings.ENEMY_BIT,
            world
        );
        update(castleObject.getPosition());
        health = 10;
    }

    public void update(Vector2 castlePosition) {
        Vector2 direction = new Vector2(castlePosition.x - getX(), castlePosition.y - getY()).nor();
        body.setLinearVelocity(direction.scl(GameSettings.TRASH_VELOCITY));
    }
    public int getHealth(){
        return health;
    }
    public void  setDamage(int lvl){
        damage += (damage*lvl)/10;
    }
    public  int getDamage(){
        return damage;
    }
    public boolean isAlive() {
        return health > 0;
    }
    @Override
    public void hit() {
        health -= 1;
    }
}
