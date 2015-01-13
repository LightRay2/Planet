package com.mygdx.game.GameModel;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Т on 12.01.2015.
 */
//тут надо быть внимательным, т к все методы возвращают что-то. void- процедур нет
public class Point3 {

    public double x, y, z;
    Point3() {
        x=y=z=0;
    }

    Point3(double x, double y, double z)  {
        this.x=x; this.y=y; this.z=z;
    }

    public Vector3 toVector(){
        return new Vector3((float)x, (float)y,(float)z);
    }

    Point3 Copy(){
        return new Point3(x,y,z);
    }

    Point3 Add(Point3 p){
        return new Point3(p.x+x, p.y+y,p.z+z);
    }

    Point3 Sub(Point3 p){
        return new Point3(x-p.x, y-p.y,z-p.z);
    }

    Point3 Mult(double k){
        return new Point3(x*k, y*k,z*k);
    }

    Point3 Div(double k){
        return new Point3(x/k,y/k,z/k);
    }

    double Scalar(Point3 p){
        return x * p.x + y * p.y + z * p.z;
    }

    Point3 Vector(Point3 p){
        return  new Point3
                (
                        y * p.z - z * p.y,
                        z * p.x - x * p.z,
                        x * p.y - y * p.x
                );
    }


    double length()
    {
        return Math.sqrt(this.Scalar(this));
    }

    double distTo(Point3 p)
    {
        return (this.Sub(p)).length();
    }

    double distToLine(Point3 A, Point3 B){
        double d = A.distTo(B);
        double s = this.Sub(A).Vector(this.Sub(B)).length();
        return s / d;
    }

    Point3 normalize(){
        return this.Mult (1.0 / length());
    }

    //------дальше не переделывал, но можно в будущем что-то почерпнуть
/*
    Point getH(const Point & A, const Point & B) const
    {
        Point C = *this;
        Point v = B - A;
        Point u = C - A;
        double k = v % u / v.length();
        v = v.normalize(k);
        Point H = A + v;
        return H;
    }
    Point rotate(Point normal) const
    {
        Point v = *this;
        if (!doubleEqual(v % normal, 0) || !doubleEqual(normal.length(), 1))
        {
            throw "no axe";
        }
        return v * normal;
    }
    Point rotate(double alpha, const Point & normal) const
    {
        return rotate(normal, cos(alpha), sin(alpha) );
    }
    Point rotate(const Point & normal, double cosa, double sina) const
    {
        Point v = *this;
        Point u = v.rotate(normal);
        Point w = v * cosa + u * sina;
        return w;
    }
    Point rotatePoint(Point O, Point axe, double cosa, double sina)
    {
        Point P = *this;
        Point v = P - O;
        double proj = axe % v / axe.length();
        Point Z = axe.normalize(proj);
        Point u = v - Z;
        u = u.rotate(axe, cosa, sina);
        Point res = O + Z + u;
        return res;
    }
    Point rotatePoint2(Point O, Point axe, double cosa, double sina)
    {
        double proj = axe % (*this - O);
        Point Z = axe.normalize(proj);
        return O + Z + (*this - O - Z).rotate(O, cosa, sina);
    }
    Point rotatePoint(Point O, Point axe, double alpha)
    {
        return rotatePoint(O, axe, cos(alpha), sin(alpha));
    }
    boolean isZero() const
    {
        return doubleEqual(x, 0) && doubleEqual(y, 0) && doubleEqual(z, 0);
    }
    boolean isOnLine(const Point & A, const Point & B) const
    {
        return ((A - *this) * (B - *this)).isZero();
    }
    boolean isInSegment(const Point & A, const Point & B) const
    {
        return isOnLine(A, B) && doubleLessOrEqual((A - *this) % (B - *this), 0);
    }
    boolean isInSegmentStrictly(const Point & A, const Point & B) const
    {
        return isOnLine(A, B) && doubleLess((A - *this) % (B - *this), 0);
    }
    double getAngle() const
    {
        return atan2(y, x);
    }
    double getAngle(Point u) const
    {
        Point v = *this;
        return atan2((v * u).length(), v % u);
    }
    boolean isOnPlane(const Point & A, const Point & B, const Point & C) const // Triple product is a value of parallelipiped
    {
        return doubleEqual((A - *this) * (B - *this) % (C - *this), 0);
    }
};
int getIntersection
        (
        const Point & A,
        const Point & B,
        const Point & C,
        const Point & D,
        Point & O
        )
        {
        if (!doubleEqual((B - A) * (C - A) % (D - A), 0))
        {
        throw "It's not plane";
        }
        if (doubleEqual(((A - B) * (C - D)).length(), 0))
        {
        if (A.isOnLine(C, D)) return 2;
        return 0;
        }
        Point normal = ((A - B) * (C - B)).normalize();
        Point v = B - A;
        double s1 = (C - A) * (D - A) % normal;
        double s2 = (D - B) * (C - B) % normal;
        double s = s1 + s2;
        v = v / s;
        v = v * s1;
        O = A + v;
        return 1;
        }
        int getIntersection
        (
        const Point & A,
        const Point & B,
        const Point & C,
        const Point & D,
        const Point & E,
        Point & O
        )
        {
        Point v = B - A;
        double V1 = (C - A) * (D - A) % (E - A);
        double V2 = (D - B) * (C - B) % (E - B);
        double V = V1 + V2;
        v = v / V;
        if (doubleEqual(V, 0))
        {
        if (A.isOnPlane(C, D, E)) return 2;
        return 0;
        }
        v = v * V1;
        O = A + v;
        return 1;
        }
        boolean getIntersection
        (
        const Point & A,
        const Point & nA,
        const Point & B,
        const Point & nB,
        Point & P,
        Point & Q
        )
        {
        Point n = nA * nB;
        if (n.isZero()) return false;
        Point v = n * nA;
        double k = (B - A) % nB / (v % nB);
        v = v * k;
        P = A + v;
        Q = P + n;
        return true;
        }*/

}