package  ru.itcube.PilichevDeveloper.manager;

import com.badlogic.gdx.physics.box2d.*;

import ru.itcube.PilichevDeveloper.objects.CastleObject;
import ru.itcube.PilichevDeveloper.objects.EnemyObject;
import  ru.itcube.PilichevDeveloper.utils.GameSettings;
import  ru.itcube.PilichevDeveloper.objects.GameObject;

public class ContactManager {

    World world;
    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                GameObject a = (GameObject) fixA.getUserData();
                GameObject b = (GameObject) fixB.getUserData();
                if (a == null || b == null) return;

                int cDef = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                if ((cDef == GameSettings.BULLET_BIT && cDef2 == GameSettings.ENEMY_BIT) ||
                    (cDef2 == GameSettings.BULLET_BIT && cDef == GameSettings.ENEMY_BIT)) {

                    a.hit();
                    b.hit();
                }

                if ((cDef == GameSettings.ENEMY_BIT && cDef2 == GameSettings.CASTLE_BIT)
                    || (cDef2 == GameSettings.ENEMY_BIT && cDef == GameSettings.CASTLE_BIT)) {

                    if (a instanceof EnemyObject && ((EnemyObject) a).isAlive()) {
                        assert b instanceof CastleObject;
                        ((CastleObject)b).takeDamage(((EnemyObject) a).getDamage());
                        ((EnemyObject) a).startExplosionPreparation();
                    }

                    if (b instanceof EnemyObject && ((EnemyObject) b).isAlive()) {
                        assert a instanceof CastleObject;
                        ((CastleObject)a).takeDamage(((EnemyObject) b).getDamage());
                        ((EnemyObject) b).startExplosionPreparation();
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }
}
