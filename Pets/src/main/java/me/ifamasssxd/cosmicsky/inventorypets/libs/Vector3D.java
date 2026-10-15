package me.ifamasssxd.cosmicsky.inventorypets.libs;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Vector3D {
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Location location) {
        this(location.toVector());
    }

    public Vector3D(Vector vector) {
        if (vector == null)
            throw new IllegalArgumentException("Vector cannot be NULL.");
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public Vector3D add(Vector3D other) {
        if (other == null)
            throw new IllegalArgumentException("other cannot be NULL");
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3D add(double x, double y, double z) {
        return new Vector3D(this.x + x, this.y + y, this.z + z);
    }

    public Vector3D subtract(Vector3D other) {
        if (other == null)
            throw new IllegalArgumentException("other cannot be NULL");
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3D subtract(double x, double y, double z) {
        return new Vector3D(this.x - x, this.y - y, this.z - z);
    }

    public Vector3D multiply(int factor) {
        return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3D multiply(double factor) {
        return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3D divide(int divisor) {
        if (divisor == 0)
            throw new IllegalArgumentException("Cannot divide by null.");
        return new Vector3D(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public Vector3D divide(double divisor) {
        if (divisor == 0.0D)
            throw new IllegalArgumentException("Cannot divide by null.");
        return new Vector3D(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public Vector3D abs() {
        return new Vector3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    public String toString() {
        return String.format("[x: %s, y: %s, z: %s]", new Object[] { Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z) });
    }

    public static final Vector3D ORIGIN = new Vector3D(0.0D, 0.0D, 0.0D);

    public final double x;

    public final double y;

    public final double z;
}
