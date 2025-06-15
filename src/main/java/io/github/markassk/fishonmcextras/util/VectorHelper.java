package io.github.markassk.fishonmcextras.util;

import net.minecraft.util.math.Vec3d;

public class VectorHelper {
    public static double calculateDistance(Vec3d point1, Vec3d point2) {
        return point1.getHorizontal().distanceTo(point2.getHorizontal());
    }

    public static Vec3d relativize(Vec3d point1, Vec3d point2) {
        return point2.getHorizontal().relativize(point1.getHorizontal());
    }

    public static Vec3d divide(Vec3d point, double value) {
        return new Vec3d(point.x / value, 0.0, point.z / value);
    }

    public static Vec3d rotate(Vec3d origin, Vec3d point, double angleDegrees) {
        double s = Math.sin(Math.toRadians(angleDegrees));
        double c = Math.cos(Math.toRadians(angleDegrees));

        double qx = origin.x + c * (point.x - origin.x) - s * (point.z - origin.z);
        double qz = origin.z + s * (point.x - origin.x) + c * (point.z - origin.z);
        return new Vec3d(qx, point.y, qz);
    }

    public static Vec3d getPoint(Vec3d player, Vec3d target, double multiplier, double angle) {
        double distance = calculateDistance(player, target);
        Vec3d relativePoint = relativize(target, player);
        Vec3d dividedPoint = divide(relativePoint, distance).multiply(multiplier);
        Vec3d unrelativize = dividedPoint.add(target);

        Vec3d rightRotatedPoint = rotate(target, unrelativize, angle);
        return new Vec3d(rightRotatedPoint.x, target.y, rightRotatedPoint.z);
    }
}
