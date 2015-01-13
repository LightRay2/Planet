package com.mygdx.game.GameModel;

/**
 * Created by Т on 12.01.2015.
 */
public class Sprite3D {
    public Point3 position, size,direction; //координата центра
    public int spriteNumber; //0-корабль, 1 - шар

    public Sprite3D(Point3 position, Point3 size, Point3 direction, int spriteNumber) {
        this.position = position;
        this.size = size;
        this.spriteNumber = spriteNumber;
        this.direction = direction;
    }
}