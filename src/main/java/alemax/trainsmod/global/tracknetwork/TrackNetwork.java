package alemax.trainsmod.global.tracknetwork;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TrackNetwork {

    public List<TrackPoint> trackPoints;

    public TrackNetwork() {
        this.trackPoints = new ArrayList<TrackPoint>();
    }

    public void addTrackPoint(TrackPoint trackPoint) {
        double distance = getDistanceToNearestTrackPoint(trackPoint.getPos());
        if(distance > 0.01)
            trackPoints.add(trackPoint);

    }

    public double getDistanceToNearestTrackPoint(Vec3d pos) {
        if(trackPoints.size() > 0) {
            double distance = Math.sqrt(Math.pow(pos.x - trackPoints.get(0).getPos().x, 2) + Math.pow(pos.y - trackPoints.get(0).getPos().y, 2) + Math.pow(pos.z - trackPoints.get(0).getPos().z, 2));

            for(TrackPoint point : trackPoints) {
                double currentDistance = Math.sqrt(Math.pow(pos.x - point.getPos().x, 2) + Math.pow(pos.y - point.getPos().y, 2) + Math.pow(pos.z - point.getPos().z, 2));
                if(currentDistance < distance) {
                    distance = currentDistance;
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public TrackPoint getNearestTrackPoint(Vec3d pos) {
        if(trackPoints.size() > 0) {
            TrackPoint closest = trackPoints.get(0);
            double distance = Math.sqrt(Math.pow(pos.x - closest.getPos().x, 2) + Math.pow(pos.y - closest.getPos().y, 2) + Math.pow(pos.z - closest.getPos().z, 2));

            for(TrackPoint point : trackPoints) {
                double currentDistance = Math.sqrt(Math.pow(pos.x - point.getPos().x, 2) + Math.pow(pos.y - point.getPos().y, 2) + Math.pow(pos.z - point.getPos().z, 2));
                if(currentDistance < distance) {
                    distance = currentDistance;
                    closest = point;
                }
            }
            return closest;
        }
        return null;
    }


}
