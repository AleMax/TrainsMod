package alemax.trainsmod.util;

import net.minecraft.util.math.Vec3d;

public class TrackUtils {

    public static Vec3d[] getLeftRightPoints(Vec3d[] points, double direction) {
        Vec3d[] newPoints = new Vec3d[points.length];
        if(points.length > 0) {
            Vec2d[] currentPoints = {new Vec2d(2 * points[0].x - points[1].x, 2 * points[0].z - points[1].z), new Vec2d(points[0].x, points[0].z) , new Vec2d(points[1].x, points[1].z)};
            Vec2d point = getLeftRightPoint(currentPoints, direction);
            newPoints[0] = new Vec3d(point.x, points[0].y, point.y);
        }
        for(int i = 1; i < points.length - 1; i++) {
            Vec2d[] currentPoints = {new Vec2d(points[i - 1].x, points[i - 1].z), new Vec2d(points[i].x, points[i].z), new Vec2d(points[i + 1].x, points[i + 1].z)};
            Vec2d point = getLeftRightPoint(currentPoints, direction);
            newPoints[i] = new Vec3d(point.x, points[i].y, point.y);
        }
        if(points.length > 1) {
            Vec2d[] currentPoints = {new Vec2d(points[points.length - 2].x, points[points.length - 2].z), new Vec2d(points[points.length - 1].x, points[points.length - 1].z), new Vec2d(2 * points[points.length - 1].x - points[points.length - 2].x, 2 * points[points.length - 1].z - points[points.length - 2].z)};
            Vec2d point = getLeftRightPoint(currentPoints, direction);
            newPoints[points.length - 1] = new Vec3d(point.x, points[points.length - 1].y, point.y);
        }
        return newPoints;
    }

    public static Vec2d getLeftRightPoint(Vec2d[] point, double direction) {
        if(point.length == 3) {
            Vec2d ab = new Vec2d(point[0].x - point[1].x, point[0].y - point[1].y);
            Vec2d bc = new Vec2d(point[2].x - point[1].x, point[2].y - point[1].y);
            double angleDifference = getCounterClockwiseAngle(ab, bc);
            double angle = getAngleBetweenPoints(point[1], point[0]);
            double newAngle = angle - (angleDifference / 2.0);
            return new Vec2d(point[1].x - Math.sin(Math.toRadians(newAngle)) * direction, point[1].y + Math.cos(Math.toRadians(newAngle)) * direction);
        }
        return null;
    }


    public static double getAngleBetweenPoints(Vec2d firstPoint, Vec2d secondPoint) {
        Vec2d direction = new Vec2d(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
        double angle = Math.toDegrees(direction.angle(new Vec2d(0,1)));
        double angleTo90 = (float) Math.toDegrees(direction.angle(new Vec2d(-1, 0)));

        if(angleTo90 > 90) angle = 360 - angle;
        return angle;
    }

    public static double getCounterClockwiseAngle(Vec2d firstVector, Vec2d secondVector) {
        double dot = firstVector.x * secondVector.x + firstVector.y * secondVector.y;
        double det = firstVector.x * secondVector.y - firstVector.y * secondVector.x;
        double angle = Math.toDegrees(Math.atan2(det, dot));
        if(angle < 0) angle += 360;
        return angle;
    }

}
