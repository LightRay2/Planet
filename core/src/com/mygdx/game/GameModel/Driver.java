package com.mygdx.game.GameModel;

import java.util.ArrayList;

/**
 * Created by lost on 09.12.14.
 */
//класс, который двигает все объекты
public class Driver {
    double gravityCoeff = 0.003; //тут можно регулировать силу притяжения

    void ExecuteShipTurnCommand(Ship ship){
        //todo
    }

    void MoveShips(ArrayList<Ship> ships, ArrayList<Planet> planets){
        for(int i = 0; i < ships.size(); i++){
            Ship s = ships.get(i);
            Point3 totalAcc = getTotalAccelerationByPlanets(s.position, s.mass, planets);
            s.speed = s.speed.Add(totalAcc);
            s.position = s.position.Add(s.speed);
        }
    }

    void MoveShells(ArrayList<Shell> shells, ArrayList<Planet> planets){
        for(int i = 0; i < shells.size(); i++){
            Shell s = shells.get(i);
            if(s.gunType == Gun.GunType.slow)
                s.speed = s.speed.Add(getTotalAccelerationByPlanets(s.position, s.mass, planets));
            s.position = s.position.Add(s.speed);
        }
    }

    private Point3 getTotalAccelerationByPlanets(Point3 point, double mass, ArrayList<Planet> planets){
        Point3 total = new Point3();
        for(int i = 0; i < planets.size(); i++){
            Planet p = planets.get(i);
            Point3 diff = p.position.Sub(point);
            double dist = diff.length();
            Point3 toAdd = diff.normalize().Mult(gravityCoeff * mass * p.mass / dist / dist);
            total = total.Add(toAdd);
        }
        return total;
    }
}
