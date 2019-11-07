package alemax.trainsmod.block;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.init.TMItemGroups;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BlockTrackMarker extends TMBlock implements BlockEntityProvider {

    public BlockTrackMarker() {
        super(Settings.of(Material.STONE));
        this.name = "track_marker";
        this.itemGroup = TMItemGroups.ITEM_GROUP_TRAINSMOD;
    }


    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityTrackMarker();
    }
}
