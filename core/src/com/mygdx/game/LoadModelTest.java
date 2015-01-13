package com.mygdx.game;

/**
 * Created by Т on 11.01.2015.
 */
//import static com.mygdx.game.models;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameModel.Sprite3D;
import javafx.geometry.BoundingBox;

import java.util.ArrayList;

/**
 * See: http://blog.xoppa.com/loading-models-using-libgdx/
 * @author Xoppa
 */
public class LoadModelTest implements ApplicationListener {
    public PerspectiveCamera cam;
   // public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading,ready;
    LwjglApplicationConfiguration desctopLauncher;
    com.mygdx.game.GameModel.Game game;
    private ArrayList<String> planets ;

    public LoadModelTest(LwjglApplicationConfiguration desctopLauncher ){
        this.desctopLauncher = desctopLauncher;
        this.desctopLauncher.title = "PlanetDestroyer";

    }

    @Override
    public void create () {
        MyInputProcessor inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);

        planetInstances = new ArrayList<ModelInstance>();
        planets = new ArrayList<String>();
        planets.add( "ice.g3dj");
        planets.add( "map.g3dj");
        planets.add( "jungle.g3dj");
        planets.add( "green.g3dj");
        planets.add( "orange.g3dj");
        planets.add( "sun.g3dj");
        planets.add("rocket3.g3dj");
       // planets.add("Wraith Raider Starship.obj");
        game = new com.mygdx.game.GameModel.Game();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(700f, 700f, 700f);
        cam.lookAt(0, 0, 0);
        cam.up.set(0,1,0);
        cam.near = 1f;
        cam.far = 3000f;
        cam.update();

       // camController = new CameraInputController(cam);
      //  Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        for(int i =0; i < planets.size(); i++)
            assets.load(planets.get(i), Model.class);
        loading = true;
        ready=false;
    }
    ArrayList<ModelInstance> planetInstances;
    ArrayList<Model> planetModels;
    private void doneLoading() {
        planetModels = new ArrayList<Model>();
        for(int i =0; i < planets.size(); i++){
            planetModels.add( assets.get(planets.get(i), Model.class));
            planetInstances.add(new ModelInstance(planetModels.get(i)));
            planetInstances.get(i).transform.setToTranslation(-6f + i * 4f, 0, 0);
            planetInstances.get(i).transform.scale(2f, 2f, 2f);
           // instances.add(planetInstances.get(i));
        }


        loading = false;
        ready=true;
    }

    int x = 0;
    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
       // camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
       /* if(ready) {
            instances.clear();
            for (int i = 0; i < planets.size(); i++) {
                instances.add(planetInstances.get(i));
            }
            for (int i = 0; i < planetInstances.size(); i++)
                planetInstances.get(i).transform.translate(0, 0, 0.005f);
        }*/

        if(ready){
            game.UpdateWorld();
            x++;
            ArrayList<Vector3> cameraInfo = game.getCameraInfo();

            cam.position.set(cameraInfo.get(0));
            cam.lookAt(cameraInfo.get(1));
            cam.up.set(0, 1, 0);
            if(game.gameStateFly)
                cam.position.add(0, (float)(100.0 *( game.stateChangedTime > 50? 1:game.stateChangedTime*0.02)), 0);
            cam.update();
            instances.clear();
            ArrayList<Sprite3D> spr = game.getSpritesToDraw();
            for(int i =0; i < spr.size();i++){
                ModelInstance m = new ModelInstance( planetModels.get(spr.get(i).spriteNumber));
                m.transform.setToTranslation(spr.get(i).position.toVector());
                Vector3 size = (spr.get(i).size).toVector();
                m.transform.scale(size.x / 2, size.y / 2, size.z / 2);
                //m.transform.rotate(spr.get(i).direction.toVector(), 90);
                m.transform.rotate(new Vector3(-1,0,0),spr.get(i).direction.toVector());
                instances.add(m);
            }
            desctopLauncher.title = "Planet Destroyer. Your Score --- "+ String.valueOf(game.score);
        }
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

/*
    private Matrix4 modelMatrix(){
        Vector3 vx = new Vector3(), vy = new Vector3(), vz = new Vector3();
        Matrix4 m = new Matrix4();

        vecB.set(vecA).scl(4.f); // if understand correctly, this is what you want
        vz.set(vecA).nor();
        vx.set(vz).crs(0, 1, 0).nor();
        vy.set(vz).crs(vx).nor();
        m.idt();
        m.val[Matrix4.M00] = vx.x; m.val[Matrix4.M01] = vx.y; m.val[Matrix4.M02] = vx.z;
        m.val[Matrix4.M10] = vy.x; m.val[Matrix4.M11] = vy.y; m.val[Matrix4.M12] = vy.z;
        m.val[Matrix4.M20] = vz.x; m.val[Matrix4.M21] = vz.y; m.val[Matrix4.M22] = vz.z;
        m.trn(vecB);
        return m;
    }*/

    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    class MyInputProcessor implements InputProcessor {
        @Override
        public boolean keyDown (int keycode) {
            return false;
        }

        @Override
        public boolean keyUp (int keycode) {
            if(keycode==62)
                game.changeState();
            return false;
        }

        @Override
        public boolean keyTyped (char character) {
            return false;
        }

        @Override
        public boolean touchDown (int x, int y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved (int x, int y) {
            game.mouseFromCenter( (double)x/Gdx.graphics.getWidth(),(double)y/ Gdx.graphics.getHeight());
            return false;
        }

        @Override
        public boolean scrolled (int amount) {
            return false;
        }
    }

}