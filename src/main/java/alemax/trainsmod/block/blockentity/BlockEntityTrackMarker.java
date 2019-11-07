package alemax.trainsmod.block.blockentity;

import alemax.trainsmod.init.TMBlockEntities;
import alemax.trainsmod.util.TrackType;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityTrackMarker extends BlockEntity {

    private String channel;
    private float angle;
    private byte height;
    private TrackType trackType;

    public BlockEntityTrackMarker() {
        super(TMBlockEntities.BLOCK_ENTITY_TRACK_MARKER);
    }


}
