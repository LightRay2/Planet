package com.mygdx.game.GameModel;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Т on 12.01.2015.
 */
class GameParams{
    Point3 worldSize = new Point3(2000,2000,1000);
    Point3 shipSize=new Point3(10,6,5); //х соответствует длине
    double shipSpeed= 2; //за кадр, надо подобрать
    double fastShellSpeed = 0.8,
            slowShellSpeed = 0.3;

    double smallestPlanetRadius = 20,
            rangePlanetRadius = 150; //размеры будут в этом диапазоне
    int planetColorCount=6; //пока 6 цветов
}
