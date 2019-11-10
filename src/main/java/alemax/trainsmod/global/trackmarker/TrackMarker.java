package alemax.trainsmod.global.trackmarker;

import alemax.trainsmod.util.TrackType;
import net.minecraft.util.math.BlockPos;

public class TrackMarker {

    private BlockPos pos;

    public String channel;
    public float angle;
    public byte height;
    public TrackType trackType;

    public TrackMarker(BlockPos pos) {
        this.pos = pos;
    }

    public void setStandardValues(String name) {
        this.channel = name;
        this.angle = 0;
        this.height = 5;
        this.trackType = TrackType.CONCRETE;
    }


    public BlockPos getPos() {
        return pos;
    }
}
