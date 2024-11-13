package com.fyzermc.factionscore.util.location;

import java.util.Objects;

public class SerializedLocation {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SerializedLocation(double x, double y, double z) {
        this("world", x, y, z, 0, 0);
    }

    public SerializedLocation(double x, double y, double z, float yaw, float pitch) {
        this("world", x, y, z, yaw, pitch);
    }

    public SerializedLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public <U extends LocationParser<T>, T> T parser(U parser) {
        return parser.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializedLocation that = (SerializedLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0 &&
                Float.compare(that.yaw, yaw) == 0 &&
                Float.compare(that.pitch, pitch) == 0 &&
                Objects.equals(worldName, that.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, y, z, yaw, pitch);
    }
}
