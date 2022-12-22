package Screens;

import Sprites.Mario;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.brentaureli.mariobros.MarioBros;

public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / MarioBros.WORLD_SIZE);
        fdef.filter.categoryBits = MarioBros.ITEM_BIT;
        fdef.filter.maskBits = MarioBros.MARIO_BIT|MarioBros.OBJECT_BIT|MarioBros.GROUND_BIT|
                MarioBros.COIN_BIT|MarioBros.BRICK_BIT;


        fdef.shape = shape;

        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
//
        setCenter(body.getPosition().x,body.getPosition().y);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);

    }

    @Override
    public void use(Mario mario) {
        destroy();
    }
}
