package com.mygdx.game.GameModel;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;

/**
 * Created by lost on 09.12.14.
 */
class Ship {
    ArrayQueue<Game.PlayerAction> actionQueue;
    double mass;
    Point3 position, speed;
    int team; //0-1
    ArrayList<Gun> guns ;

    Ship(double mass, Point3 position, Point3 speed, int team, ArrayList<Gun> guns) {
        this.mass = mass;
        this.position = position;
        this.speed = speed;
        this.team = team;
        this.guns = guns;
        actionQueue = new ArrayQueue<Game.PlayerAction>(0);
    }

}

class Gun{
    enum GunType{laser, fast, slow}
    GunType gunType;
    Point3 positionFromShipCenter, directionRelatedToShip;
    private int reloadTime; //константа
    private int timeToReload, shells;

    Gun(GunType gunType, int shells, int reloadTime, Point3 directionRelatedToShip, Point3 positionFromShipCenter) {
        this.gunType = gunType;
        this.shells = shells;
        this.timeToReload = this.reloadTime = reloadTime;
        this.directionRelatedToShip = directionRelatedToShip;
        this.positionFromShipCenter = positionFromShipCenter;
    }

    boolean TryToShoot(){
        if(shells > 0 && timeToReload == 0){
            timeToReload = reloadTime;
            shells--;
            return true;
        }
        else
            return false;
    }
}

class Shell{
    Gun.GunType gunType;
    double mass=1,radius=1;
    Point3 position,speed;

    Shell(Gun.GunType gunType, Point3 position, Point3 speed) {
        this.gunType = gunType;
        this.position = position;
        this.speed = speed;
    }
}

class Planet{
    Point3 position;
    double radius;
    double mass;
    int colorNum;

    Planet(Point3 position, double radius, int colorNum) {
        this.position = position;
        this.radius = radius;
        this.mass = radius*radius*radius;
        this.colorNum = colorNum;
    }
}
