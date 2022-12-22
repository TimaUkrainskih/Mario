package Screens;

import Scenes.Hud;
import Sprites.Enemy;
import Sprites.Mario;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import com.brentaureli.mariobros.MarioBros;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {

    private MarioBros game;

    private TextureAtlas atlas;
    private Mario player;
//    private Goomba goomba;

    private Music music;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;

    private TiledMap map;

    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemDefs;
//   private  ExtendViewport backViewport;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;


        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(MarioBros.V_WIDTH / MarioBros.WORLD_SIZE, MarioBros.V_HEIGHT / MarioBros.WORLD_SIZE,gamecam);
//        gamecam = (OrthographicCamera) gamePort.getCamera();
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.WORLD_SIZE);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -40), true);
        b2dr = new Box2DDebugRenderer();


        creator = new B2WorldCreator(this);


        player = new Mario(this);
//        goomba = new Goomba(this,5.32f,0.32f);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

        items = new Array<>();
        itemDefs = new LinkedBlockingQueue<>();

    }

    public void spawnItem(ItemDef itemd) {
        itemDefs.add(itemd);
    }

    public void handleSpawn(){
        if(!itemDefs.isEmpty()){
            ItemDef iDef = itemDefs.poll();
            if(iDef.type == Mushroom.class){

                items.add(new Mushroom(this,iDef.position.x,iDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.jump();
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt) {

        handleInput(dt);
        handleSpawn();

        world.step(1 / 60f, 6, 2);
//        goomba.update(dt);
        player.update(dt);

        for (Enemy enemy : creator.getGoombas()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.WORLD_SIZE) {
                enemy.b2body.setActive(true);
            }
        }

        for(Item item:items){
            item.update(dt);
        }


        hud.update(dt);
        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
//        goomba.draw(game.batch);
        player.draw(game.batch);
        for (Enemy enemy : creator.getGoombas()) {
            enemy.draw(game.batch);
        }

        for(Item item: items){
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height,true);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
