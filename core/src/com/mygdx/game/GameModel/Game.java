package com.mygdx.game.GameModel;

import com.badlogic.gdx.math.Vector3;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lost on 09.12.14.
 */



public class Game {

    enum PlayerAction{turnLeft, turnRight, turnUp, turnDown, turnClockwise, turnCounterClockwise,
        selectLaserGun, selectFastGun, selectSlowGun,
        shoot
    }

    GameParams gameParams = new GameParams();
    Driver driver = new Driver();

    ArrayList<Planet> planets;
    ArrayList<Shell> shells;
    ArrayList<Ship> ships;
    public boolean gameStateFly=false;

    Ship controlled;

    public int stateChangedTime=0;
    public int score = 0;

    //единицы измерения - масса 1 = массе снаряда,
    //а планета массой 1 имеет радиус 1.
    //начало координат - рисуем параллелепипед на бумаге, это левая нижняя ближняя точка
    public Game(){
        ArrayList gunset = new ArrayList();
    ships = new ArrayList<Ship>();
        shells = new ArrayList<Shell>();
        planets = new ArrayList<Planet>();
        //создаем корабли в разных концах параллелепипеда
        //первый едет за плоскость, второй из-за плоскости
        //пушки у обоих на правом борту
        ships.add(initShip());
       /* ships.add(new Ship(10, gameParams.worldSize.Copy()
                , new Point3(0,0,-gameParams.shipSpeed),
                1, createGunset() ));*/

        shells = new ArrayList<Shell>();

        //добавляем по 5 планет в 4 параллелограмма (для равномерности)
        planets = new ArrayList<Planet>();
        double x = gameParams.worldSize.x/2,
                y = gameParams.worldSize.y/2,
                z = gameParams.worldSize.z;

        addPlanets(new Point3(0,0,0), new Point3(x,y,z), 10 );
        addPlanets(new Point3(x,0,0), new Point3(x,y,z), 10 );
        addPlanets(new Point3(0,y,0), new Point3(x,y,z), 10 );
        addPlanets(new Point3(x,y,0), new Point3(x,y,z), 10 );
    }

    Ship initShip(){
        return new Ship(10, new Point3(gameParams.worldSize.x/2,gameParams.worldSize.y/2,-gameParams.worldSize.z)
                , new Point3(0,0,1),
                0, createGunset() );
    }

    void AddPlayerAction(int teamNumber, PlayerAction action )
    {
        ships.get(teamNumber).actionQueue.add(action);
    }

    public void changeState(){
        if(gameStateFly){
            ships.clear();
            ships.add(initShip());
            mouseFromCenter(lastx,lasty);
        }
        else{
            ships.get(0).speed = new Point3(0,0,4);
        }
        gameStateFly = !gameStateFly;
        stateChangedTime=0;
    }

    double lastx=0.5,lasty=0.5;
    public void mouseFromCenter(double x, double y){
        if(!gameStateFly) {
            ships.get(0).position.x = gameParams.worldSize.x * (1-x);
            ships.get(0).position.y = gameParams.worldSize.y * (1-y);
        }
        lastx=x;
        lasty=y;
    }

    //тут все действия, которые происходят каждый тик
    public void UpdateWorld(){
        stateChangedTime++;
        //осуществляем накопившиеся действия
        //todo

        //двигаем снаряды и корабли
        if(gameStateFly) {
            driver.MoveShips(ships, planets);
            Planet p = collision(ships.get(0));
            if(p!=null){
                planets.remove(p);
                changeState();
                score++;
            }
        }
       // driver.MoveShells(shells,planets);

        //уничтожаем все, что далеко или столкнулось
        //todo
    }

    int cameraRarius=1500;
    public ArrayList<Vector3> getCameraInfo(){
        ArrayList<Vector3> res = new ArrayList<Vector3>();

        Ship s = ships.get(0);
        Vector3 point = s.position.toVector();
        Vector3 speed = s.speed.toVector().nor();
        point.add(speed.scl(-300));
        speed.rotate(90,0,0,1);
        Vector3 shift = new Vector3(point);
        shift.add(s.speed.toVector());
        res.add(point);
        res.add(shift);
        return res;
    }

    public ArrayList<Sprite3D> getSpritesToDraw(){
        ArrayList<Sprite3D> r = new ArrayList<Sprite3D>();
       /* for(int i = 0; i < ships.size();i++){
            Ship p = ships.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    gameParams.shipSize.Copy(),
                    0));
        }*/
        for(int i = 0; i < planets.size();i++){
            Planet p = planets.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    new Point3(p.radius*2,p.radius*2,p.radius*2), new Point3(0,0,0),
                    p.colorNum));
        }

        r.add(new Sprite3D(ships.get(0).position.Copy(), new Point3(50,50,50), ships.get(0).speed.normalize(), gameParams.planetColorCount));
       /* for(int i = 0; i < shells.size();i++){
            Shell p = shells.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    new Point3(p.radius*2,p.radius*2,p.radius*2),
                    1));
        }*/

        return r;
    }

    Planet collision(Ship s){
        for(int i =0 ;i < planets.size();i++){
            Planet p = planets.get(i);
            if(p.position.Sub(s.position).length() < p.radius + 30)
                return p;
        }
        return null;
    }

    private ArrayList<Gun> createGunset(){
        //пока разница только в расположении относительно корабля
        ArrayList<Gun> gunset = new ArrayList<Gun>();
        gunset.add(new Gun(Gun.GunType.laser, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(-gameParams.shipSize.x/3, gameParams.shipSize.y/2, 0)
        ));
        gunset.add(new Gun(Gun.GunType.fast, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(0, gameParams.shipSize.y/2, 0)
        ));
        gunset.add(new Gun(Gun.GunType.slow, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(gameParams.shipSize.x/3, gameParams.shipSize.y/2, 0)
        ));
        return gunset;
    }

    private void addPlanets(Point3 startPoint, Point3 parallSize, int count  ){
        Random rand = new Random();
        int maxLoopEntries = 10000; //чтоб бесконечно не крутился
        while(count > 0 && maxLoopEntries>0){
            maxLoopEntries--;

            Point3 randPoint = new Point3(
                    rand.nextDouble()*parallSize.x,
                    rand.nextDouble()*parallSize.y,
                    rand.nextDouble()*parallSize.z
            );

            double newRadius = gameParams.smallestPlanetRadius + rand.nextDouble()*(gameParams.rangePlanetRadius);

            Point3 newPosition = startPoint.Add(randPoint);

            boolean canBeCreatedHere=true;
            for(int i = 0; i < planets.size();i++){
                if(newPosition.distTo(planets.get(i).position) <= newRadius + planets.get(i).radius)
                {
                    canBeCreatedHere=false;
                    break;
                }
            }

            if(canBeCreatedHere){
                count--;
                planets.add(new Planet(newPosition, newRadius
                        , rand.nextInt(gameParams.planetColorCount)));
            }
        }
    }

}