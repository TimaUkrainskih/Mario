package Tools;

import Screens.Brick;
import Screens.Coin;
import Screens.PlayScreen;
import Sprites.Goomba;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;

public class B2WorldCreator {

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    private Array<Goomba> goombas;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();


        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.WORLD_SIZE, (rect.getY() + rect.getHeight() / 2) / MarioBros.WORLD_SIZE);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2 / MarioBros.WORLD_SIZE, rect.getHeight() / 2 / MarioBros.WORLD_SIZE);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.WORLD_SIZE, (rect.getY() + rect.getHeight() / 2) / MarioBros.WORLD_SIZE);
            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2 / MarioBros.WORLD_SIZE, rect.getHeight() / 2 / MarioBros.WORLD_SIZE);
            fdef.shape = shape;

            fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fdef);


        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {


            new Brick(screen, object);
        }
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {


            new Coin(screen, object);
        }

        goombas = new Array<>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.WORLD_SIZE, rect.getY() / MarioBros.WORLD_SIZE));
        }
    }
}
