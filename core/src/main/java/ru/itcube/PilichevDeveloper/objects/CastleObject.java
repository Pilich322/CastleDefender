package ru.itcube.PilichevDeveloper.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.math.Rectangle;

import ru.itcube.PilichevDeveloper.utils.GameSettings;

public class CastleObject extends GameObject{
    int health,level, currentExperience;
    int startHealth = 100,maxExperience =100;
    public CastleObject(String texturePath, int x, int y, int width, int height, World world) {
        super(texturePath, x, y, width, height, GameSettings.CASTLE_BIT, world);
        world.destroyBody(body);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x * GameSettings.SCALE, y * GameSettings.SCALE);
        body = world.createBody(bodyDef);
        setCollide();
        body.setLinearDamping(100);
        health = startHealth;
        level = 1;
        currentExperience = 0;
        System.out.println("Fixtures count: " + body.getFixtureList().size);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    private void setCollide(){
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
        PolygonShape collider = new PolygonShape();
        collider.setAsBox(
            width / 2f * GameSettings.SCALE,
            height / 2f * GameSettings.SCALE
        );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = collider;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = cBits;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        collider.dispose();
    }

    public void lvlUp(){
        level++;
        health = startHealth + (startHealth*level)/10;
        maxExperience = maxExperience + (maxExperience*level)/10;
    }

    public int getExperience(){
        return currentExperience;
    }

    public void setCurrentExperience(int exp){
        currentExperience += exp;
        checkLevelUp();
    }

    private void checkLevelUp(){
        if(currentExperience>=maxExperience){
            currentExperience-=maxExperience;
            lvlUp();
        }
    }
}
