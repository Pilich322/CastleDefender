package  ru.itcube.PilichevDeveloper.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import  ru.itcube.PilichevDeveloper.utils.GameSettings;

public class BulletObject extends GameObject{

    public boolean wasHit;

    public BulletObject(int x, int y, int width, int height, String texturePath, World world, Vector2 velocity) {
        super(texturePath, x, y, width, height, GameSettings.BULLET_BIT, world);
        body.setLinearVelocity(velocity);
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasHit || (getY() - height / 2 > GameSettings.SCREEN_HEIGHT);
    }


    @Override
    public void hit() {
        wasHit = true;
    }
}
