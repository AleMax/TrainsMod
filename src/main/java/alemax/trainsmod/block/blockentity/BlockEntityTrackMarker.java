package alemax.trainsmod.block.blockentity;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.init.TMBlockEntities;
import alemax.trainsmod.util.TrackType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BlockEntityTrackMarker extends BlockEntity {

    private TrackMarker trackMarker;


    public BlockEntityTrackMarker() {
        super(TMBlockEntities.BLOCK_ENTITY_TRACK_MARKER);



        //TrackMarkerInstances.OVERWORLD.getTrackMarker(pos);

    }

    @Override
    public boolean onBlockAction(int int_1, int int_2) {
        System.out.println(int_1 + "\t" + int_2);
        return true;
    }


    @Override
    public double getSquaredRenderDistance() {
        return 4096.0D;
    }


}
