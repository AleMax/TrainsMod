package alemax.trainsmod.global.tracknetwork;

import net.minecraft.util.math.Vec3d;

public class TrackPointEnd extends TrackPoint {

    private TrackPoint previous;
    private TrackPointEnd next; //The next here is the one which is "outside" the other one, should be another End Point

    public TrackPointEnd(Vec3d pos) {
        this(pos, null);
    }

    public TrackPointEnd(Vec3d pos, TrackPoint previous) {
        this.pos = pos;
        this.previous = previous;
        this.next = null;
    }

    public void setNextTrackPoint(TrackPointEnd next) {
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
