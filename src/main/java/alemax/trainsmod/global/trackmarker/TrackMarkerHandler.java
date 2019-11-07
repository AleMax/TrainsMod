package alemax.trainsmod.global.trackmarker;


import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TrackMarkerHandler {

    private List<TrackMarker> trackMarkers;

    public TrackMarkerHandler() {

    }

    public void addTrackMarker(TrackMarker trackMarker) {
        trackMarkers.add(trackMarker);
    }

    public void removeTrackMarker(BlockPos trackMarkerPos) {

    }

}
