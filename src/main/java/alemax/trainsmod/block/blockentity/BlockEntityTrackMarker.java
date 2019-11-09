package alemax.trainsmod.block.blockentity;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.init.TMBlockEntities;
import alemax.trainsmod.util.TrackType;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityTrackMarker extends BlockEntity {

    public BlockEntityTrackMarker() {
        super(TMBlockEntities.BLOCK_ENTITY_TRACK_MARKER);

    }

    @Override
    public boolean onBlockAction(int int_1, int int_2) {
        System.out.println(int_1 + "\t" + int_2);
        return true;
    }

}
