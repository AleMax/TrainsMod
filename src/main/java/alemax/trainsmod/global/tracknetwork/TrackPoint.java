package alemax.trainsmod.global.tracknetwork;

import net.minecraft.util.math.Vec3d;

public abstract class TrackPoint {

    protected Vec3d pos;
    protected int uniqueID;

    public abstract TrackPoint getNextTrackPoint(TrackPoint previous);

    public abstract TrackPoint getNextTrackPointFromDirection(Vec3d direction);

    public Vec3d getPos() {
        return this.pos;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }
}
