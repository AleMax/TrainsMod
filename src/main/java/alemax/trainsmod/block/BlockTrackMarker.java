package alemax.trainsmod.block;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.init.TMItemGroups;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        super.onPlaced(world_1, blockPos_1, blockState_1, livingEntity_1, itemStack_1);
        if(world_1.isClient()) {
            if(TrackMarker.test.equals(""))
                TrackMarker.test = "CLIIEEEEENT";
            System.out.println("Client: " + TrackMarker.test);
        } else {
            if(TrackMarker.test.equals(""))
                TrackMarker.test = "SERVER";
            System.out.println("Server: " + TrackMarker.test);
        }

    }
}
