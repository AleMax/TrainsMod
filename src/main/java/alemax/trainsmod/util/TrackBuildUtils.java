package alemax.trainsmod.util;

import alemax.trainsmod.block.TMBlock;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.TrackNetworkInstances;
import alemax.trainsmod.global.tracknetwork.TrackPoint;
import alemax.trainsmod.global.tracknetwork.TrackPointEnd;
import alemax.trainsmod.global.tracknetwork.TrackPointStandard;
import alemax.trainsmod.init.TMBlocks;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.networking.TMPacket;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TrackBuildUtils {

    private static double CIRCLE_INCREMENT_STEPS = 0.01;

    public static void buildStandardTrackOverworld(String channel, World world) {
        List<TrackMarker> foundMarkers = new ArrayList<>();
        for(TrackMarker marker : TrackMarkerInstances.OVERWORLD.trackMarkers) {
            if(marker.channel.equalsIgnoreCase(channel)) {
                foundMarkers.add(marker);
            }
        }

        if(foundMarkers.size() == 2) {
            TrackMarker firstTrackMarker = foundMarkers.get(0);
            TrackMarker secondTrackMarker = foundMarkers.get(1);

            //TODO: Only using X and Z distance, maybe change in the future
            double distance = Math.sqrt(Math.pow(firstTrackMarker.getPos().getX() - secondTrackMarker.getPos().getX(), 2) + Math.pow(firstTrackMarker.getPos().getZ() - secondTrackMarker.getPos().getZ(), 2));
            if(distance > 1.9) {
                List<Vec3d> pointsList = calculateTrack(firstTrackMarker, secondTrackMarker, distance);
                Vec3d[] points = getPointsWithHeight(pointsList, firstTrackMarker, secondTrackMarker);

                Vec3d[] rightPoints = TrackUtils.getLeftRightPoints(points, 1.45);
                Vec3d[] leftPoints = TrackUtils.getLeftRightPoints(points, -1.45);

                ArrayList<BlockPos> trackPositions = new ArrayList<BlockPos>();

                for(int i = 0; i < rightPoints.length - 1; i++) {
                    double maxX = -Double.MAX_VALUE;
                    double minX = Double.MAX_VALUE;
                    double maxZ = -Double.MAX_VALUE;
                    double minZ = Double.MAX_VALUE;
                    if(rightPoints[i].x > maxX) maxX = rightPoints[i].x;
                    if(rightPoints[i].x < minX) minX = rightPoints[i].x;
                    if(rightPoints[i].z > maxZ) maxZ = rightPoints[i].z;
                    if(rightPoints[i].z < minZ) minZ = rightPoints[i].z;
                    if(rightPoints[i+1].x > maxX) maxX = rightPoints[i+1].x;
                    if(rightPoints[i+1].x < minX) minX = rightPoints[i+1].x;
                    if(rightPoints[i+1].z > maxZ) maxZ = rightPoints[i+1].z;
                    if(rightPoints[i+1].z < minZ) minZ = rightPoints[i+1].z;
                    if(leftPoints[i].x > maxX) maxX = leftPoints[i].x;
                    if(leftPoints[i].x < minX) minX = leftPoints[i].x;
                    if(leftPoints[i].z > maxZ) maxZ = leftPoints[i].z;
                    if(leftPoints[i].z < minZ) minZ = leftPoints[i].z;
                    if(leftPoints[i+1].x > maxX) maxX = leftPoints[i+1].x;
                    if(leftPoints[i+1].x < minX) minX = leftPoints[i+1].x;
                    if(leftPoints[i+1].z > maxZ) maxZ = leftPoints[i+1].z;
                    if(leftPoints[i+1].z < minZ) minZ = leftPoints[i+1].z;

                    int maxXInt = (int) Math.round(Math.ceil(maxX));
                    int minXInt = (int) Math.round(Math.floor(minX));
                    int maxZInt = (int) Math.round(Math.ceil(maxZ));
                    int minZInt = (int) Math.round(Math.floor(minZ));

                    ArrayList<Vec3i> eligibleList = new ArrayList<Vec3i>();

                    for(int ix = minXInt; ix < maxXInt + 1; ix++) {
                        for(int iz = minZInt; iz < maxZInt + 1; iz++) {
                            boolean inner = true;
                            if(checkSide(rightPoints[i], rightPoints[i + 1], new Vec2d(ix, iz)) > 0) {
                                inner = false;
                            }
                            else if(checkSide(rightPoints[i + 1], leftPoints[i + 1], new Vec2d(ix, iz)) > 0) inner = false;
                            else if(checkSide(leftPoints[i + 1], leftPoints[i], new Vec2d(ix, iz)) > 0) inner = false;
                            else if(checkSide(leftPoints[i], rightPoints[i], new Vec2d(ix, iz)) > 0) inner = false;
                            else if(inner) eligibleList.add(new Vec3i(ix, 0, iz));
                        }
                    }

                    for(int j = 0; j < eligibleList.size(); j++) {
                        addToBlockList(trackPositions, eligibleList.get(j), points);
                        addToBlockList(trackPositions, new Vec3i(eligibleList.get(j).getX() - 1, 0, eligibleList.get(j).getZ()), points);
                        addToBlockList(trackPositions, new Vec3i(eligibleList.get(j).getX(), 0, eligibleList.get(j).getZ() - 1), points);
                        addToBlockList(trackPositions, new Vec3i(eligibleList.get(j).getX() - 1, 0, eligibleList.get(j).getZ() - 1), points);
                    }

                }


                for(int i = 0; i < trackPositions.size(); i++) {
                    world.setBlockState(trackPositions.get(i), TMBlocks.BLOCK_TRACK.getDefaultState());
                    System.out.println("BUILD BLOCK\t" + trackPositions.get(i).getX() + "\t" + trackPositions.get(i).getZ());
                }

                TMPackets.packetS2CTrackBlockPlacement.send(world.getServer(), trackPositions);

                TrackPoint[] trackPoints = new TrackPoint[points.length];
                trackPoints[0] = new TrackPointEnd(points[0]);
                for(int i = 1; i < (trackPoints.length - 1); i++) {
                    trackPoints[i] = new TrackPointStandard(points[i]);
                }
                trackPoints[trackPoints.length - 1] = new TrackPointEnd(points[trackPoints.length - 1]);

                ((TrackPointEnd) trackPoints[0]).setPrevious(trackPoints[1]);
                for(int i = 1; i < (trackPoints.length - 1); i++) {
                    ((TrackPointStandard) trackPoints[i]).setPrevious(trackPoints[i - 1]);
                    ((TrackPointStandard) trackPoints[i]).setNext(trackPoints[i + 1]);
                }
                ((TrackPointEnd) trackPoints[trackPoints.length - 1]).setPrevious(trackPoints[trackPoints.length - 2]);

                for(int i = 0; i < trackPoints.length; i++) {
                    TrackNetworkInstances.OVERWORLD.addTrackPoint(trackPoints[i]);
                }

                BlockPos mainPos = new BlockPos(points[(int) ((points.length - 1) / 2.0)].x, points[(int) ((points.length - 1) / 2.0)].y, points[(int) ((points.length - 1) / 2.0)].z);

                world.setBlockState(mainPos, TMBlocks.BLOCK_TRACK_SUPER.getDefaultState());
                System.out.println(mainPos.getX() + "\t" + mainPos.getY() + "\t" + mainPos.getZ());

                TMPackets.packetS2CTrackData.send(world.getServer(), mainPos, points);


            } else {
                System.out.println("TO CLOSE TO EACH OTHER");
                //TODO: error message
            }

        } else {
            System.out.println("NOT 2 MARKERS");
            //TODO: error message
        }

    }

    private static void addToBlockList(ArrayList<BlockPos> trackPositions, Vec3i pos, Vec3d[] points) {
        for(BlockPos currentPos : trackPositions) {
            if(pos.getX() == currentPos.getX() && pos.getZ() == currentPos.getZ()) {
                return;
            }
        }
        double nearestDistance = Double.MAX_VALUE;
        double nearestHeight = 0;
        for(Vec3d point : points) {
            double dis = Math.sqrt(Math.pow(point.x - pos.getX(), 2) + Math.pow(point.z - pos.getZ(), 2));
            if(dis < nearestDistance) {
                nearestDistance = dis;
                nearestHeight = point.y;
            }
        }
        trackPositions.add(new BlockPos(pos.getX(), nearestHeight, pos.getZ()));
        if(nearestHeight % 1 < (3.0 / 16.0)) trackPositions.add(new BlockPos(pos.getX(), nearestHeight - 1, pos.getZ()));

    }

    private static List<Vec3d> calculateTrack(TrackMarker firstTrackMarker, TrackMarker secondTrackMarker,  double distance) {
        synchronizeAngle(firstTrackMarker, secondTrackMarker);
        synchronizeAngle(secondTrackMarker, firstTrackMarker);
        byte firstSide = checkSide(firstTrackMarker, secondTrackMarker, distance);
        byte secondSide = checkSide(secondTrackMarker, firstTrackMarker, distance);

        if(firstSide < 0 && secondSide < 0 || firstSide > 0 && secondSide > 0) {

            float radius = 0;
            float circleMiddleDistance = (float) distance;

            Vec2d firstSideVector = getLeftRightVector(firstTrackMarker, firstSide);
            Vec2d secondSideVector = getLeftRightVector(secondTrackMarker, secondSide);
            Vec2d firstMiddlePoint = new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5);
            Vec2d secondMiddlePoint = new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5);

            while(2 * radius < circleMiddleDistance) {
                radius += CIRCLE_INCREMENT_STEPS;
                firstMiddlePoint.add(firstSideVector);
                secondMiddlePoint.add(secondSideVector);
                circleMiddleDistance = (float) Math.sqrt(Math.pow(secondMiddlePoint.x - firstMiddlePoint.x, 2) + Math.pow(secondMiddlePoint.y - firstMiddlePoint.y, 2));
            }
            double firstAngle = getAngleBetween(firstTrackMarker, firstMiddlePoint, secondMiddlePoint);
            double secondAngle = getAngleBetween(secondTrackMarker, secondMiddlePoint, firstMiddlePoint);

            double firstLength = radius * firstAngle;
            double secondLength = radius * secondAngle;
            double addedLength = firstLength + secondLength;
            double scaleFactor = addedLength / Math.round(addedLength);
            double firstScaled = firstLength / scaleFactor - 0.5;
            double secondScaled = secondLength / scaleFactor - 0.5;
            int firstSteps = (int) Math.round(Math.floor(firstScaled));
            int secondSteps = (int) Math.round(Math.floor(secondScaled));

            if(Math.abs(firstScaled - Math.round(firstScaled)) < 0.001 && Math.abs(secondScaled - Math.round(secondScaled)) < 0.001) { //FIX IT (10.5, 10.5)
                firstSteps = (int) Math.round(firstScaled);
                secondSteps = (int) (Math.round(secondScaled) - 1);
            }

            double firstAngleToLastPoint = 0;
            double secondAngleToLastPoint = 0;
            if(firstSide > 0) {
                firstAngleToLastPoint = TrackUtils.getAngleBetweenPoints(firstMiddlePoint, new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5)) + ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
                double x = TrackUtils.getAngleBetweenPoints(firstMiddlePoint, new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5));
                double y = ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
                secondAngleToLastPoint = TrackUtils.getAngleBetweenPoints(secondMiddlePoint, new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5)) + ((secondSteps + 0.5) * scaleFactor) / secondLength * Math.toDegrees(secondAngle);

            } else {
                firstAngleToLastPoint = TrackUtils.getAngleBetweenPoints(firstMiddlePoint, new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5)) - ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
                secondAngleToLastPoint = TrackUtils.getAngleBetweenPoints(secondMiddlePoint, new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5)) - ((secondSteps + 0.5) * scaleFactor) / secondLength * Math.toDegrees(secondAngle);
            }
            if(firstAngleToLastPoint < 0) firstAngleToLastPoint += 360;
            else if(firstAngleToLastPoint > 360) firstAngleToLastPoint -= 360;

            if(secondAngleToLastPoint < 0) secondAngleToLastPoint += 360;
            else if(secondAngleToLastPoint > 360) secondAngleToLastPoint -= 360;

            Vec2d firstLastPoint = new Vec2d(-Math.sin(Math.toRadians(firstAngleToLastPoint)) * radius, Math.cos(Math.toRadians(firstAngleToLastPoint)) * radius);
            Vec2d secondLastPoint = new Vec2d(-Math.sin(Math.toRadians(secondAngleToLastPoint)) * radius, Math.cos(Math.toRadians(secondAngleToLastPoint)) * radius);

            double firstAngleDifference = Math.toDegrees(firstLastPoint.angle(new Vec2d(firstTrackMarker.getPos().getX() + 0.5 - firstMiddlePoint.x, firstTrackMarker.getPos().getZ() + 0.5 - firstMiddlePoint.y)));
            double secondAngleDifference = Math.toDegrees(secondLastPoint.angle(new Vec2d(secondTrackMarker.getPos().getX() + 0.5 - secondMiddlePoint.x, secondTrackMarker.getPos().getZ() + 0.5 - secondMiddlePoint.y)));

            double fad = firstTrackMarker.realAngle - TrackUtils.getAngleBetweenPoints(new Vec2d(firstLastPoint.x + firstMiddlePoint.x, firstLastPoint.y + firstMiddlePoint.y), new Vec2d(secondLastPoint.x + secondMiddlePoint.x, secondLastPoint.y + secondMiddlePoint.y));
            fad = Math.abs(fad);
            if(fad > 180) fad = 360 - fad;

            double sad = secondTrackMarker.realAngle - TrackUtils.getAngleBetweenPoints(new Vec2d(secondLastPoint.x + secondMiddlePoint.x, secondLastPoint.y + secondMiddlePoint.y), new Vec2d(firstLastPoint.x + firstMiddlePoint.x, firstLastPoint.y + firstMiddlePoint.y));
            sad = Math.abs(sad);
            if(sad > 180) sad = 360 - sad;

            ArrayList<Vec2d> firstPoints = new ArrayList<>();
            ArrayList<Vec2d> secondPoints = new ArrayList<>();

            Vec2d currentPoint = new Vec2d(-Math.sin(Math.toRadians(firstTrackMarker.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(firstTrackMarker.realAngle)) * 0.5 * scaleFactor);
            Vec2d aimedPoint = new Vec2d(firstMiddlePoint.x - (firstTrackMarker.getPos().getX() + 0.5) + firstLastPoint.x, firstMiddlePoint.y - (firstTrackMarker.getPos().getZ() + 0.5) + firstLastPoint.y);
            double currentAngle = firstTrackMarker.realAngle;
            double angleSteps = fad / (firstSteps + 1);
            byte incrementFactor = 1;

            if(firstSide < 0) incrementFactor = -1;

            firstPoints.add(new Vec2d(currentPoint));
            for(int i = 0; i < firstSteps; i++) {
                currentAngle = currentAngle + (incrementFactor * angleSteps);
                currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
                currentPoint.y += Math.cos(Math.toRadians(currentAngle));

                firstPoints.add(new Vec2d(currentPoint));
            }

            //TODO: BUGS WITH THE SCALING --> get the points on the circle differently (also in other case)
            double xScale = aimedPoint.x / currentPoint.x;
            double yScale = aimedPoint.y / currentPoint.y;

            for(int i = 0; i < firstPoints.size(); i++) {
                firstPoints.get(i).x *= xScale;
                firstPoints.get(i).y *= yScale;
            }

            currentPoint = new Vec2d(-Math.sin(Math.toRadians(secondTrackMarker.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(secondTrackMarker.realAngle)) * 0.5 * scaleFactor);
            aimedPoint = new Vec2d(secondMiddlePoint.x - (secondTrackMarker.getPos().getX() + 0.5) + secondLastPoint.x, secondMiddlePoint.y - (secondTrackMarker.getPos().getZ() + 0.5) + secondLastPoint.y);
            currentAngle = secondTrackMarker.realAngle;
            angleSteps = sad / (secondSteps + 1);

            secondPoints.add(new Vec2d(currentPoint));
            for(int i = 0; i < secondSteps; i++) {
                currentAngle = currentAngle + (incrementFactor * angleSteps);
                currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
                currentPoint.y += Math.cos(Math.toRadians(currentAngle));

                secondPoints.add(new Vec2d(currentPoint));
            }

            xScale = aimedPoint.x / currentPoint.x;
            yScale = aimedPoint.y / currentPoint.y;

            for(int i = 0; i < secondPoints.size(); i++) {
                secondPoints.get(i).x *= xScale;
                secondPoints.get(i).y *= yScale;
            }

            ArrayList<Vec3d> finalPoints = new ArrayList<>();
            finalPoints.add(new Vec3d(firstTrackMarker.getPos().getX() + 0.5, 0, firstTrackMarker.getPos().getZ() + 0.5));
            for(int i = 0; i < firstPoints.size(); i++) {
                finalPoints.add(new Vec3d(firstTrackMarker.getPos().getX() + 0.5 + firstPoints.get(i).x, 0, firstTrackMarker.getPos().getZ() + 0.5 + firstPoints.get(i).y));
            }

            for(int i = secondPoints.size() - 1; i > -1; i--) {
                finalPoints.add(new Vec3d(secondTrackMarker.getPos().getX() + 0.5 + secondPoints.get(i).x, 0, secondTrackMarker.getPos().getZ() + 0.5 + secondPoints.get(i).y));
            }
            finalPoints.add(new Vec3d(secondTrackMarker.getPos().getX() + 0.5, 0, secondTrackMarker.getPos().getZ() + 0.5));

            return finalPoints;

        } else if(firstSide == 0 && secondSide == 0) {

            int steps = (int) Math.round(distance);
            Vec2d direction = new Vec2d(secondTrackMarker.getPos().getX() - firstTrackMarker.getPos().getX(), secondTrackMarker.getPos().getZ() - firstTrackMarker.getPos().getZ());
            direction.scale(1.0 / steps);
            ArrayList<Vec3d> finalPoints = new ArrayList<>();
            for(int i = 0; i < steps + 1; i++) {
                finalPoints.add(new Vec3d(firstTrackMarker.getPos().getX() + 0.5 + (direction.x * i), 0, firstTrackMarker.getPos().getZ() + 0.5 + (direction.y * i)));
            }
            return finalPoints;

        } else {

            Vec2d intersect = null;
            double firstDistance = 0;
            double secondDistance = 0;
            try {
                intersect = getLineIntersection(new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5), new Vec2d(firstTrackMarker.getPos().getX() + 0.5 - Math.sin(Math.toRadians(firstTrackMarker.realAngle)), firstTrackMarker.getPos().getZ() + 0.5 + Math.cos(Math.toRadians(firstTrackMarker.realAngle))), new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5), new Vec2d(secondTrackMarker.getPos().getX() + 0.5 - Math.sin(Math.toRadians(secondTrackMarker.realAngle)), secondTrackMarker.getPos().getZ() + 0.5 + Math.cos(Math.toRadians(secondTrackMarker.realAngle))));
                firstDistance = Math.sqrt(Math.pow(intersect.x - (firstTrackMarker.getPos().getX() + 0.5), 2) + Math.pow(intersect.y - (firstTrackMarker.getPos().getZ() + 0.5), 2));
                secondDistance = Math.sqrt(Math.pow(intersect.x - (secondTrackMarker.getPos().getX() + 0.5), 2) + Math.pow(intersect.y - (secondTrackMarker.getPos().getZ() + 0.5), 2));
            } catch (Exception e) {

                return null;
            }


            if(secondDistance > firstDistance) {
                Vec2d sideVector = getLeftRightVector(firstTrackMarker, firstSide);
                Vec2d otherLineVector = new Vec2d(Math.sin(Math.toRadians(secondTrackMarker.realAngle)), -Math.cos(Math.toRadians(secondTrackMarker.realAngle)));
                double alpha = 0;
                if(firstSide < 1) alpha = getCounterClockwiseAngle(otherLineVector, sideVector);
                else alpha = getCounterClockwiseAngle(sideVector, otherLineVector);
                alpha *= -1;
                if(alpha < 0) alpha += 360;

                double radius = Math.cos(Math.toRadians(alpha)) * firstDistance / (1 - Math.sin(Math.toRadians(alpha)));
                sideVector.normalize();
                Vec2d middlePoint = new Vec2d(firstTrackMarker.getPos().getX() + 0.5 + sideVector.x * radius, firstTrackMarker.getPos().getZ() + 0.5 + sideVector.y * radius);
                Vec2d perp = getPerpendicularPoint(new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5), new Vec2d(secondTrackMarker.getPos().getX() + 0.5 - Math.sin(Math.toRadians(secondTrackMarker.realAngle)), secondTrackMarker.getPos().getZ() + 0.5 + Math.cos(Math.toRadians(secondTrackMarker.realAngle))), middlePoint);

                //System.out.println(perp.x + "\t" + perp.y);

                double angleDifference = firstTrackMarker.realAngle - (secondTrackMarker.realAngle + 180) % 360;
                angleDifference = Math.abs(angleDifference);
                if(angleDifference > 180) angleDifference = 360 - angleDifference;

                double circleLength = 2 * radius * Math.PI * angleDifference / 360.0;
                double scaleFactor = circleLength / Math.round(circleLength);
                double circleScaled = circleLength / scaleFactor - 0.5;
                int circleSteps = (int) Math.round(Math.floor(circleScaled));

                Vec2d currentPoint = new Vec2d(-Math.sin(Math.toRadians(firstTrackMarker.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(firstTrackMarker.realAngle)) * 0.5 * scaleFactor);
                Vec2d aimedPoint = new Vec2d(perp.x - (firstTrackMarker.getPos().getX() + 0.5), perp.y - (firstTrackMarker.getPos().getZ() + 0.5));
                double currentAngle = firstTrackMarker.realAngle;
                double angleSteps = angleDifference / (circleSteps + 1);
                byte incrementFactor = 1;

                ArrayList<Vec2d> circlePoints = new ArrayList<Vec2d>();

                if(firstSide < 0) incrementFactor = -1;

                circlePoints.add(new Vec2d(currentPoint));
                for(int i = 0; i < circleSteps; i++) {
                    currentAngle = currentAngle + (incrementFactor * angleSteps);
                    currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
                    currentPoint.y += Math.cos(Math.toRadians(currentAngle));

                    circlePoints.add(new Vec2d(currentPoint));
                }

                double xScale = aimedPoint.x / currentPoint.x;
                double yScale = aimedPoint.y / currentPoint.y;

                for(int i = 0; i < circlePoints.size(); i++) {
                    circlePoints.get(i).x *= xScale;
                    circlePoints.get(i).y *= yScale;
                }

                ArrayList<Vec2d> linePoints = new ArrayList<>();

                Vec2d lineDirection = new Vec2d(secondTrackMarker.getPos().getX() + 0.5 - perp.x, secondTrackMarker.getPos().getZ() + 0.5 - perp.y);
                double lineLength = lineDirection.length();
                int steps = (int) Math.round(lineLength);
                if(steps == 0) steps = 1;
                lineDirection.scale(1.0 / steps);
                for(int i = 1; i < steps + 1; i++) {
                    linePoints.add(new Vec2d(perp.x + (lineDirection.x * i), perp.y + (lineDirection.y * i)));
                }

                ArrayList<Vec3d> finalPoints = new ArrayList<>();
                finalPoints.add(new Vec3d(firstTrackMarker.getPos().getX() + 0.5, 0, firstTrackMarker.getPos().getZ() + 0.5));
                for(int i = 0; i < circlePoints.size(); i++) {
                    finalPoints.add(new Vec3d(firstTrackMarker.getPos().getX() + 0.5 + circlePoints.get(i).x, 0, firstTrackMarker.getPos().getZ() + 0.5 + circlePoints.get(i).y));
                }

                for(int i = 0; i < linePoints.size(); i++) {
                    finalPoints.add(new Vec3d(linePoints.get(i).x, 0, linePoints.get(i).y));
                }

                return finalPoints;
            } else {
                Vec2d sideVector = getLeftRightVector(secondTrackMarker, secondSide);
                Vec2d otherLineVector = new Vec2d(Math.sin(Math.toRadians(firstTrackMarker.realAngle)), -Math.cos(Math.toRadians(firstTrackMarker.realAngle)));
                double alpha = 0;
                if(firstSide < 1) alpha = getCounterClockwiseAngle(sideVector, otherLineVector);
                else alpha = getCounterClockwiseAngle(otherLineVector, sideVector);
                alpha *= -1;
                if(alpha < 0) alpha += 360;

                double radius = Math.cos(Math.toRadians(alpha)) * secondDistance / (1 - Math.sin(Math.toRadians(alpha))); //ich hoffe doch..
                sideVector.normalize();
                Vec2d middlePoint = new Vec2d(secondTrackMarker.getPos().getX() + 0.5 + sideVector.x * radius, secondTrackMarker.getPos().getZ() + 0.5 + sideVector.y * radius);
                Vec2d perp = getPerpendicularPoint(new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5), new Vec2d(firstTrackMarker.getPos().getX() + 0.5 - Math.sin(Math.toRadians(firstTrackMarker.realAngle)), firstTrackMarker.getPos().getZ() + 0.5 + Math.cos(Math.toRadians(firstTrackMarker.realAngle))), middlePoint);
                //System.out.println(radius);

                double angleDifference = secondTrackMarker.realAngle - (firstTrackMarker.realAngle + 180) % 360;
                angleDifference = Math.abs(angleDifference);
                if(angleDifference > 180) angleDifference = 360 - angleDifference;

                double circleLength = 2 * radius * Math.PI * angleDifference / 360.0;
                double scaleFactor = circleLength / Math.round(circleLength);
                double circleScaled = circleLength / scaleFactor - 0.5;
                int circleSteps = (int) Math.round(Math.floor(circleScaled));

                Vec2d currentPoint = new Vec2d(-Math.sin(Math.toRadians(secondTrackMarker.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(secondTrackMarker.realAngle)) * 0.5 * scaleFactor);
                Vec2d aimedPoint = new Vec2d(perp.x - (secondTrackMarker.getPos().getX() + 0.5), perp.y - (secondTrackMarker.getPos().getZ() + 0.5));
                double currentAngle = secondTrackMarker.realAngle;
                double angleSteps = angleDifference / (circleSteps + 1);
                byte incrementFactor = 1;

                ArrayList<Vec2d> circlePoints = new ArrayList<Vec2d>();

                if(secondSide < 0) incrementFactor = -1;

                circlePoints.add(new Vec2d(currentPoint));
                for(int i = 0; i < circleSteps; i++) {
                    currentAngle = currentAngle + (incrementFactor * angleSteps);
                    currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
                    currentPoint.y += Math.cos(Math.toRadians(currentAngle));

                    circlePoints.add(new Vec2d(currentPoint));
                }

                double xScale = aimedPoint.x / currentPoint.x;
                double yScale = aimedPoint.y / currentPoint.y;

                for(int i = 0; i < circlePoints.size(); i++) {
                    circlePoints.get(i).x *= xScale;
                    circlePoints.get(i).y *= yScale;
                }

                ArrayList<Vec2d> linePoints = new ArrayList<>();

                Vec2d lineDirection = new Vec2d(firstTrackMarker.getPos().getX() + 0.5 - perp.x, firstTrackMarker.getPos().getZ() + 0.5 - perp.y);
                double lineLength = lineDirection.length();
                int steps = (int) Math.round(lineLength);
                if(steps == 0) steps = 1;
                lineDirection.scale(1.0 / steps);
                for(int i = 1; i < steps + 1; i++) {
                    linePoints.add(new Vec2d(perp.x + (lineDirection.x * i), perp.y + (lineDirection.y * i)));
                }

                ArrayList<Vec3d> finalPoints = new ArrayList<>();
                finalPoints.add(new Vec3d(secondTrackMarker.getPos().getX() + 0.5, 0, secondTrackMarker.getPos().getZ() + 0.5));
                for(int i = 0; i < circlePoints.size(); i++) {
                    finalPoints.add(new Vec3d(secondTrackMarker.getPos().getX() + 0.5 + circlePoints.get(i).x, 0, secondTrackMarker.getPos().getZ() + 0.5 + circlePoints.get(i).y));
                }

                for(int i = 0; i < linePoints.size(); i++) {
                    finalPoints.add(new Vec3d(linePoints.get(i).x, 0, linePoints.get(i).y));
                }

                ArrayList<Vec3d> flippedFinalPoints = new ArrayList<>();

                for(int i = finalPoints.size() - 1; i > -1; i--) {
                    flippedFinalPoints.add(finalPoints.get(i));
                }

                return flippedFinalPoints;
            }

        }
    }

    private static void synchronizeAngle(TrackMarker firstTrackMarker, TrackMarker secondTrackMarker) {
        Vec2d firstVector = new Vec2d(-Math.sin(Math.toRadians(firstTrackMarker.angle)), Math.cos(Math.toRadians(firstTrackMarker.angle)));
        Vec2d toSecondVector = new Vec2d(secondTrackMarker.getPos().getX() - firstTrackMarker.getPos().getX(), secondTrackMarker.getPos().getZ() - firstTrackMarker.getPos().getZ());

        if(Math.toDegrees(firstVector.angle(toSecondVector)) < 90) {
            firstTrackMarker.realAngle = firstTrackMarker.angle;
        } else {
            firstTrackMarker.realAngle = (firstTrackMarker.angle + 180) % 360;
        }
    }

    private static byte checkSide(TrackMarker firstTrackMarker, TrackMarker secondTrackMarker, double distance) {
        //Setup all points
        Vec2d linePoint1 = new Vec2d(firstTrackMarker.getPos().getX() + 0.5, firstTrackMarker.getPos().getZ() + 0.5);
        Vec2d line1_2 = new Vec2d(-distance * Math.sin(Math.toRadians(firstTrackMarker.realAngle)), distance * Math.cos(Math.toRadians(firstTrackMarker.realAngle)));
        Vec2d linePoint2 = new Vec2d(linePoint1);
        linePoint2.add(line1_2);
        Vec2d leftPoint = new Vec2d(linePoint1.x + line1_2.y, linePoint1.y - line1_2.x);
        Vec2d point = new Vec2d(secondTrackMarker.getPos().getX() + 0.5, secondTrackMarker.getPos().getZ() + 0.5);

        //Calculate the real stuff
        double sidePoint = (point.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (point.y - linePoint1.y) * (linePoint2.x - linePoint1.x); //Formula to compute the side of a point apparently
        double sideLeft = (leftPoint.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (leftPoint.y - linePoint1.y) * (linePoint2.x - linePoint1.x);

        if(sidePoint < -0.001 && sideLeft < 0) return -1;
        if(sidePoint > 0.001 && sideLeft < 0) return 1;
        if(sidePoint < -0.001 && sideLeft > 0) return 1;
        if(sidePoint > 0.001 && sideLeft > 0) return -1;
        return 0;
    }

    private static byte checkSide(Vec3d firstPoint, Vec3d secondPoint, Vec2d point) {
        //Setup all points
        Vec2d line1_2 = new Vec2d(secondPoint.x - firstPoint.x, secondPoint.z - firstPoint.z);
        Vec2d leftPoint = new Vec2d(firstPoint.x + line1_2.y, firstPoint.z - line1_2.x);

        //Calculate the real stuff
        double sidePoint = (point.x - firstPoint.x) * (secondPoint.z - firstPoint.z) - (point.y - firstPoint.z) * (secondPoint.x - firstPoint.x); //Formula to compute the side of a point apparently
        double sideLeft = (leftPoint.x - firstPoint.x) * (secondPoint.z - firstPoint.z) - (leftPoint.y - firstPoint.z) * (secondPoint.x - firstPoint.x);

        if(sidePoint < -0.001 && sideLeft < 0) return -1;
        if(sidePoint > 0.001 && sideLeft < 0) return 1;
        if(sidePoint < -0.001 && sideLeft > 0) return 1;
        if(sidePoint > 0.001 && sideLeft > 0) return -1;
        //System.out.println("Straight");
        return 0;
    }

    private static Vec2d getLeftRightVector(TrackMarker trackMarker, byte direction) {
        if(direction == 0) return new Vec2d();
        Vec2d line1_2 = new Vec2d(-Math.sin(Math.toRadians(trackMarker.realAngle)), Math.cos(Math.toRadians(trackMarker.realAngle)));
        Vec2d directionVec = new Vec2d(line1_2.y, -line1_2.x);
        directionVec.normalize();
        directionVec.scale(CIRCLE_INCREMENT_STEPS);
        if(direction == 1) directionVec.scale(-1);
        return directionVec;
    }


    private static double getAngleBetween(TrackMarker marker, Vec2d thisMiddlePoint, Vec2d otherMiddlePoint) {
        Vec2d middleLine = new Vec2d(otherMiddlePoint.x - thisMiddlePoint.x, otherMiddlePoint.y - thisMiddlePoint.y);
        Vec2d lineToTE = new Vec2d(marker.getPos().getX() + 0.5 - thisMiddlePoint.x, marker.getPos().getZ() + 0.5 - thisMiddlePoint.y);
        return middleLine.angle(lineToTE);
    }

    private static Vec2d getLineIntersection(Vec2d pointA, Vec2d pointB, Vec2d pointC, Vec2d pointD) {
        double a1 = pointB.y - pointA.y;
        double b1 = pointA.x - pointB.x;
        double c1 = a1*(pointA.x) + b1*(pointA.y);

        double a2 = pointD.y - pointC.y;
        double b2 = pointC.x - pointD.x;
        double c2 = a2*(pointC.x)+ b2*(pointC.y);

        double determinant = a1*b2 - a2*b1;

        if (determinant == 0) return null;
        else {
            double x = (b2*c1 - b1*c2)/determinant;
            double y = (a1*c2 - a2*c1)/determinant;
            return new Vec2d(x, y);
        }
    }

    private static double getCounterClockwiseAngle(Vec2d firstVector, Vec2d secondVector) {
        double dot = firstVector.x * secondVector.x + firstVector.y * secondVector.y;
        double det = firstVector.x * secondVector.y - firstVector.y * secondVector.x;
        double angle = Math.toDegrees(Math.atan2(det, dot));
        if(angle < 0) angle += 360;
        return angle;
    }

    private static Vec2d getPerpendicularPoint(Vec2d linePoint1, Vec2d linePoint2, Vec2d point) {


        double k = ((linePoint2.y - linePoint1.y) * (point.x - linePoint1.x) - (linePoint2.x - linePoint1.x) * (point.y - linePoint1.y)) / (Math.pow(linePoint2.y - linePoint1.y, 2) + Math.pow(linePoint2.x - linePoint1.x, 2));
        double x = point.x - k * (linePoint2.y - linePoint1.y);
        double y = point.y + k * (linePoint2.x - linePoint1.x);

        return new Vec2d(x, y);
    }

    private static Vec3d[] getPointsWithHeight(List<Vec3d> points, TrackMarker firstTrackMarker, TrackMarker secondTrackMarker) {
        Vec3d[] points3d = new Vec3d[points.size()];
        double currentHeight = firstTrackMarker.getPos().getY() + (firstTrackMarker.height / 16.0) + 1;
        double finalHeight = secondTrackMarker.getPos().getY() + (secondTrackMarker.height / 16.0) + 1;
        double heightDifference = (finalHeight - currentHeight) / (points3d.length - 1);

        for(int i = 0; i < points3d.length; i++) {
            points3d[i] = new Vec3d(points.get(i).x, currentHeight + heightDifference * i , points.get(i).z);
        }
        return points3d;
    }


}
