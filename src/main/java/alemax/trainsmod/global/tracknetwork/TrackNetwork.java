package alemax.trainsmod.global.tracknetwork;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TrackNetwork {

    public List<TrackPoint> trackPoints;

    public TrackNetwork() {
        this.trackPoints = new ArrayList<TrackPoint>();
    }

    public void addTrackMarker(TrackPoint trackPoint) {
        trackPoints.add(trackPoint);
    }

    public TrackMarker getNearestTrackMarker(Vec3d pos) {
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
        }
        return null;
    }


}
