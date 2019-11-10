package alemax.trainsmod.global.trackmarker;


import net.minecraft.util.math.BlockPos;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;

public class TrackMarkerHandler {

    public List<TrackMarker> trackMarkers;

    public TrackMarkerHandler() {
        this.trackMarkers = new ArrayList<TrackMarker>();
    }

    public void addTrackMarker(TrackMarker trackMarker) {
        trackMarkers.add(trackMarker);
    }

    public boolean removeTrackMarker(BlockPos pos) {
        boolean found = false;
        for(int i = 0; i < trackMarkers.size(); i++) {
            if(pos.equals(trackMarkers.get(i).getPos())) {
                trackMarkers.remove(i);
                i--;
                found = true;
            }
        }
        return found;
    }

    public TrackMarker getTrackMarker(BlockPos pos) {
        for(TrackMarker marker : trackMarkers) {
            if(pos.equals(marker.getPos()))
                return marker;
        }
        return null;
    }

}
