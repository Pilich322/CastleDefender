package  ru.itcube.PilichevDeveloper.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ru.itcube.PilichevDeveloper.utils.GameResources;
import  ru.itcube.PilichevDeveloper.utils.GameSettings;

public class BulletObject extends GameObject{

    public boolean wasHit;

    public BulletObject(int x, int y, int width, int height, String texturePath, World world, Vector2 velocity) {
        super(texturePath, x, y, width, height, GameSettings.BULLET_BIT, world);
        body.destroyFixture(body.getFixtureList().first());

        PolygonShape shape = new PolygonShape();
        float scaleFactor = 1f;
        shape.setAsBox(width * scaleFactor / 2f * GameSettings.SCALE, height * scaleFactor / 2f * GameSettings.SCALE);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        fdef.filter.categoryBits = GameSettings.BULLET_BIT;
        fdef.filter.maskBits = GameSettings.ENEMY_BIT;

        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData(this);

        shape.dispose();
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
