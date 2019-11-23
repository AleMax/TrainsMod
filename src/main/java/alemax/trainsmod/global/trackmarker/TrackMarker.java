package alemax.trainsmod.global.trackmarker;

import alemax.trainsmod.util.TrackType;
import net.minecraft.util.math.BlockPos;

public class TrackMarker {

    public static final int MAX_CHANNEL_LENGTH = 25;

    private BlockPos pos;

    public String channel;
    public float angle;
    public byte height;
    public TrackType trackType;

    public float realAngle;

    public TrackMarker(BlockPos pos) {
        this.pos = pos;
    }

    public void setStandardValues(String name) {
        if(name.length() > MAX_CHANNEL_LENGTH)
            this.channel = name.substring(0, MAX_CHANNEL_LENGTH);
        else
            this.channel = name;

        this.angle = 0;
        this.height = 5;
        this.trackType = TrackType.CONCRETE;

        this.realAngle = 0;
    }


    public BlockPos getPos() {
        return pos;
    }
}
