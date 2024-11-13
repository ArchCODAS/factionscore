package com.fyzermc.factionscore.util;

public class Vector2D {

    protected final double x, z;

    public Vector2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public Vector2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, z + other.z);
    }

    public Vector2D add(double x, double z) {
        return new Vector2D(this.x + x, this.z + z);
    }

    public Vector2D add(int x, int z) {
        return new Vector2D(this.x + x, this.z + z);
    }

    public Vector2D add(Vector2D... others) {
        double newX = x, newZ = z;

        for (Vector2D other : others) {
            newX += other.x;
            newZ += other.z;
        }

        return new Vector2D(newX, newZ);
    }

    public double length() {
        return Math.sqrt(x * x + z * z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2D)) {
            return false;
        }

        Vector2D other = (Vector2D) obj;
        return other.x == this.x && other.z == this.z;
    }

    @Override
    public int hashCode() {
        return ((int) x << 16) ^ (int) z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
}