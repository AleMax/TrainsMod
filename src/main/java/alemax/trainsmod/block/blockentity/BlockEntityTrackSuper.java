package alemax.trainsmod.block.blockentity;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.init.TMBlockEntities;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityTrackSuper extends BlockEntity {

    public int displayList;


    public BlockEntityTrackSuper() {
        super(TMBlockEntities.BLOCK_ENTITY_TRACK_SUPER);
        displayList = 0;
    }


    @Override
    public double getSquaredRenderDistance() {
        return 4096.0D;
    }



}
