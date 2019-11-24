package alemax.trainsmod.block.blockentity;

import alemax.trainsmod.init.TMBlockEntities;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityTrackSuper extends BlockEntity {

    public int trackDisplayList;
    public int railbedDisplayList;


    public BlockEntityTrackSuper() {
        super(TMBlockEntities.BLOCK_ENTITY_TRACK_SUPER);
        trackDisplayList = 0;
        railbedDisplayList = 0;
    }

    @Override
    public double getSquaredRenderDistance() {
        return 65536;
    }



}
