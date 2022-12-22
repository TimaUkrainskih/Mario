package Screens;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.brentaureli.mariobros.MarioBros;

public class Brick extends InteractiveTileObject {



    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);

//        BodyDef bdef = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();
//
//        bdef.type = BodyDef.BodyType.StaticBody;
//        bdef.position.set((bounds.getX() + bounds.getWidth() / 2)/ MarioBros.PPM,(bounds.getY() + bounds.getHeight()/2)/MarioBros.PPM);
//        body = world.createBody(bdef);
//        shape.setAsBox(bounds.getWidth()/2/MarioBros.PPM,bounds.getHeight()/2/MarioBros.PPM);
//        fdef.shape = shape;
//        body.createFixture(fdef);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick","Collision");



        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
