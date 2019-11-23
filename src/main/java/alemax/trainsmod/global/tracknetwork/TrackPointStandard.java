package alemax.trainsmod.global.tracknetwork;

import net.minecraft.util.math.Vec3d;

public class TrackPointStandard extends TrackPoint {

    private TrackPoint previous;
    private TrackPoint next;

    public TrackPointStandard(Vec3d pos) {
        this(pos, null, null);
    }

    public TrackPointStandard(Vec3d pos, int uniqueID) {
        this(pos, null, null);
        this.uniqueID = uniqueID;
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

    public TrackPoint getNext() {
        return next;
    }

    public TrackPoint getPrevious() {
        return previous;
    }

    public void setNext(TrackPoint next) {
        this.next = next;
    }

    public void setPrevious(TrackPoint previous) {
        this.previous = previous;
    }

}
