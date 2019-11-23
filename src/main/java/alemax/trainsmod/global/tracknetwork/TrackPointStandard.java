package alemax.trainsmod.global.tracknetwork;

import net.minecraft.util.math.Vec3d;

public class TrackPointStandard extends TrackPoint {

    private TrackPoint previous;
    private TrackPoint next;

    public TrackPointStandard(Vec3d pos) {
        this(pos, null, null);
    }

    public TrackPointStandard(Vec3d pos, TrackPoint previous, TrackPoint next) {
        this.pos = pos;
        this.previous = previous;
        this.next = next;
    }

    @Override
    public TrackPoint getNextTrackPoint(TrackPoint previous) {
        return previous == this.previous ? this.next : this.previous; //Returns the next TrackPoint, which isnt the previous one
    }

    @Override
    public TrackPoint getNextTrackPointFromDirection(Vec3d direction) {
        return null;
    }


}
