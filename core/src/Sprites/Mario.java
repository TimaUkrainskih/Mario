package Sprites;

import Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;

public class Mario extends Sprite {

    public enum State {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING,


    }

    public State currentState;
    public State previosState;
    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    private Animation marioRun;
    private Animation marioJump;
    private float stateTimer;
    private boolean runningRigth;


    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));

        this.world = screen.getWorld();
        currentState = State.STANDING;
        previosState = State.STANDING;
        stateTimer = 0;
        runningRigth = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));

        }
        marioRun = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(),i * 16,0,18,28));

        }
        marioJump = new Animation(0.1f,frames);
        frames.clear();
        this.world = screen.getWorld();
        defineMario();
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 16);
        setBounds(0, 0, 16 / MarioBros.WORLD_SIZE, 16 / MarioBros.WORLD_SIZE);
        setRegion(marioStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region= marioStand;
                break;
        }
        if((b2body.getLinearVelocity().x<0 || !runningRigth) && !region.isFlipX()){
            region.flip(true,false);
            runningRigth = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRigth) && region.isFlipX()){
            region.flip(true,false);
            runningRigth = true;
        }

        stateTimer = currentState == previosState ? stateTimer + dt:0;
        previosState =currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previosState == State.JUMPING){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else  if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }else return State.STANDING;
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / MarioBros.WORLD_SIZE, 32 / MarioBros.WORLD_SIZE);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / MarioBros.WORLD_SIZE);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT|MarioBros.OBJECT_BIT|
        MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MarioBros.WORLD_SIZE,8 / MarioBros.WORLD_SIZE),new Vector2(3/MarioBros.WORLD_SIZE,8 / MarioBros.WORLD_SIZE));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 10f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }
}
